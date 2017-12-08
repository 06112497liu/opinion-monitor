package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.constant.EsConstant;
import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.*;
import com.bbd.enums.TransferEnum;
import com.bbd.enums.WarnReasonEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.exception.ErrorCode;
import com.bbd.exception.UserErrorCode;
import com.bbd.service.*;
import com.bbd.service.param.TransferParam;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.service.vo.OpinionTaskListVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Liuweibo
 * @version Id: OpinionTaskServiceImpl.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
@Service
public class OpinionTaskServiceImpl implements OpinionTaskService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private EsModifyService esModifyService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemSettingService settingService;

    @Autowired
    private OpinionEventDao opinionEventDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SystemSettingService systemSettingService;
    
    /**
     * 当前用户待处理舆情列表
     *
     * @param transferType 转发类型: 1/2/3：请示，4/5/6：回复
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getUnProcessedList(Integer transferType, PageBounds pb) {

        Long targeterId = UserContext.getUser().getId();

        PageList<OpinionTaskListVO> result = esQueryService.getUnProcessedList(UserContext.getUser().getId(), transferType, pb);
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        result.forEach(o -> {
            o.setLevel(settingService.judgeOpinionSettingClass(o.getHot(), setting));
            String uuid = o.getUuid();
            Map<String, Object> keyMap = Maps.newHashMap();
            keyMap.put(EsConstant.uuidField, uuid);
            keyMap.put(EsConstant.targeterIdField, targeterId);
            List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 1);
            userService.buildOperatorAndTargeter(list);
            o.setRecords(list);
        });
        return result;
    }



    /**
     * 当前用户转发、解除、监测列表
     *
     * @param opStatus 1. 转发（介入）；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb) {
        PageList<OpinionTaskListVO> result = esQueryService.getProcessedList(opStatus, pb);
        if (Objects.nonNull(opStatus)) {
            if (opStatus == 1) {
                result.forEach(o -> {
                    String uuid = o.getUuid();
                    Map<String, Object> keyMap = Maps.newHashMap();
                    keyMap.put(EsConstant.uuidField, uuid);
                    if(!UserContext.isAdmin())
                        keyMap.put(EsConstant.operatorIdField, UserContext.getUser().getId());
                    List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 1);
                    userService.buildOperatorAndTargeter(list);
                    o.setRecords(list);
                });
            }
            if (opStatus == 3) { // 如果是已监控页面，查询事件的一些信息
                result.forEach(o -> {
                    String uuid = o.getUuid();
                    OpinionEventExample exam = new OpinionEventExample();
                    exam.createCriteria().andUuidEqualTo(uuid);
                    List<OpinionEvent> events = opinionEventDao.selectByExample(exam);
                    OpinionEvent event;
                    if (!events.isEmpty()) {
                        event = events.get(0);
                        o.setMonitorTime(event.getGmtCreate());
                        o.setEventName(event.getEventName());
                        o.setEventID(event.getId());
                    }
                });
            }
        }
        List<WarnSetting> warnSetting = systemSettingService.queryWarnSetting(3);
        result.forEach(o -> {
            Integer hot = o.getHot();
            Integer level = systemSettingService.judgeOpinionSettingClass(hot, warnSetting);
            o.setLevel(level);
        });
        return result;
    }

    /**
     * 转发舆情
     *
     * @param param
     */
    @Override
    public void transferOpinion(TransferParam param) throws IOException, ExecutionException, InterruptedException {

        Date now = new Date();

        // step-1：校验当前用户是否有操作权限
        checkPermission(param.getUuid());

        // step-2：如果是普通用户，是不能转发给管理员（也不能转发给自己）
        Long targeterId = param.getUserId();
        Long operatorId = UserContext.getUser().getId();
        if (targeterId.compareTo(operatorId) == 0) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "不能转发给自己");
        checkOpinionTranferConfine(operatorId);

        // step-3：舆情转发次数不能大于50次
        checkOpinionTranCount(param.getUuid());

        // step-3：修改舆情的状态
        UserInfo operatorUser = UserContext.getUser();
        if (Objects.isNull(operatorUser))
            throw new ApplicationException(UserErrorCode.USER_NO_LOGIN);

        Map<String, Object> map = Maps.newHashMap();
        map.put(EsConstant.opStatusField, 1);
        map.put(EsConstant.opOwnerField, targeterId);
        map.put(EsConstant.transferTypeField, param.getTransferType());
        map.put(EsConstant.recordTimeField, DateUtil.formatDateByPatten(now, "yyyy-MM-dd HH:mm:ss"));
        esModifyService.updateOpinion(operatorUser, param.getUuid(), map);

        // step-4：记录转发记录
            // 构建转发记录对象
        OpinionOpRecordVO recordVO = new OpinionOpRecordVO();
        recordVO.setUuid(param.getUuid());
        recordVO.setOpType(1);
        recordVO.setTransferType(param.getTransferType());
        recordVO.setTransferNote(param.getTransferNote());
        recordVO.setOperatorId(UserContext.getUser().getId());
        recordVO.setTargeterId(targeterId);
        recordVO.setOpTime(now);
        recordVO.setTransferContent(TransferEnum.getDescByCode(param.getTransferType().toString()));

            // 向es中添加转发记录
        esModifyService.recordOpinionOp(recordVO);
    }

    // 舆情转发次数上限为50
    private void checkOpinionTranCount(String uuid) {
        Map<String, Object> keyMap = Maps.newHashMap();
        keyMap.put(EsConstant.uuidField, uuid);
        keyMap.put(EsConstant.opTypeField, 1);
        List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 49);
        if(list.size() >= 49) {
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "舆情转发次数不能超过50次");
        }
    }

    // 普通用户不能转发给管理员
    private void checkOpinionTranferConfine(Long userId) {
        Optional<Account> op = accountService.loadByUserId(userId);
        if(op.isPresent()) {
            Account a = op.get();
            boolean isAdmin = a.getAdmin();
            if(!UserContext.isAdmin() && isAdmin) {
                throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "普通用户不能转发给管理员");
            }
        }
    }

    /**
     * 解除预警
     *
     * @param uuid
     * @param removeReason
     * @param removeNote
     */
    @Override
    public void removeWarn(String uuid, Integer removeReason, String removeNote) throws InterruptedException, ExecutionException, IOException {

        Date now = new Date();

        // step-1：校验当前用户是否有操作资格
        checkPermission(uuid);

        // step-2：修改舆情记录
        UserInfo operator = UserContext.getUser();
        Map<String, Object> map = Maps.newHashMap();
        map.put(EsConstant.removeNoteField, removeNote);
        map.put(EsConstant.opStatusField, 2);
        map.put(EsConstant.opOwnerField, -1); // 解除之后，就没有目标操作者了
        map.put(EsConstant.recordTimeField, DateUtil.formatDateByPatten(now, "yyyy-MM-dd HH:mm:ss"));
        esModifyService.updateOpinion(operator, uuid, map);

        // step-3：记录解除记录
        OpinionOpRecordVO recordVO = new OpinionOpRecordVO();
        recordVO.setOpType(2);
        recordVO.setOpTime(now);
        recordVO.setOperatorId(UserContext.getUser().getId());
        recordVO.setTargeterId(-1L);
        recordVO.setUuid(uuid);
        recordVO.setRemoveReason(removeReason);
        recordVO.setRemoveContent(WarnReasonEnum.getDescByCode(removeReason.toString()));
        recordVO.setRemoveNote(removeNote);
        esModifyService.recordOpinionOp(recordVO);
    }

    /**
     * 查询处于任务舆情中的舆情详情
     *
     * @param uuid
     * @return
     */
    @Override
    public OpinionTaskListVO getTransferDetail(String uuid, Integer type) {
        Long currentUserId = UserContext.getUser().getId();
        // step-1：查询舆情详情
        OpinionEsVO o = esQueryService.getOpinionByUUID(uuid);
        OpinionTaskListVO result = BeanMapperUtil.map(o, OpinionTaskListVO.class);

        // step-2：查询该条舆情的转发记录
        Map<String, Object> map = Maps.newHashMap();
        map.put(EsConstant.uuidField, uuid);
        map.put(EsConstant.opTypeField, 1);
        List<OpinionOpRecordVO> records = esQueryService.getOpinionOpRecordByUUID(map, 50);
        userService.buildOperatorAndTargeter(records);
        OpinionOpRecordVO v = null;
        if (!records.isEmpty()) {
            java.util.Optional<OpinionOpRecordVO> op = null;
            if (type == 1){
                op = records.stream().findFirst();
            } else if (type == 2) {
                op = records.stream().filter(p -> p.getOperatorId().equals(currentUserId)).findFirst();
            }
            if (op != null && op.isPresent()) v = op.get();
        }
        if(v != null) records.add(0, v);
        result.setRecords(records);

        // step-3：设置级别
        List<WarnSetting> settings = systemSettingService.queryWarnSetting(3);
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot(), settings);
        result.setLevel(level);
        return result;
    }

    /**
     * 当前用户任务列表统计
     * @return
     */
    @Override
    public List<KeyValueVO> getTaskSta() {
        List<KeyValueVO> result = null;
        UserInfo user = UserContext.getUser();

        if(user.getAdmin()) {
            result = esQueryService.queryCoutGroupOpStatus();
        } else {
            result = esQueryService.queryCoutGroupOpStatus(user.getId());
        }
        return result;
    }

    // 校验当前用户是否有操作该条舆情的资格
    private void checkPermission(String uuid) {
        UserInfo user = UserContext.getUser();
        // 如果不是超级管理员，判断当前用户是否是该条舆情的待操作者
        if(!UserContext.isAdmin()) {
            OpinionEsVO opinion = esQueryService.getOpinionByUUID(uuid);
            if(Objects.isNull(opinion)) {
                throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "操作对象不存在");
            }
            Long ownerId = opinion.getOpOwner();
            if(Objects.nonNull(ownerId)) {
                if(!Objects.equals(user.getId(), ownerId)) {
                    throw new ApplicationException(UserErrorCode.USER_NO_PERMISSION);
                }
            }
        }
    }
}
