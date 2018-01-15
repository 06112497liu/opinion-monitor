package com.bbd.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bbd.bean.OpinionEsVO;
import com.bbd.constant.EsConstant;
import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.*;
import com.bbd.enums.TransferEnum;
import com.bbd.enums.WarnReasonEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.exception.UserErrorCode;
import com.bbd.service.*;
import com.bbd.service.param.TransferParam;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.service.vo.OpinionTaskListVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.bbd.util.StringUtils;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
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
     * 转发舆情
     *
     * @param param
     */
    @Override
    public void transferOpinion(TransferParam param) throws IOException, ExecutionException, InterruptedException {

        Date now = new Date();
        String uuid = param.getUuid();
        Long targeterId = param.getUserId();

        synchronized (this) {
            // step-1：校验是否具有操作权限
            // 不能转发给自己
            notToOneself(targeterId);
            // 不能转发给管理员（管理员除外）
            notToAdmin(targeterId);
            // 转发次数不能操作49次
            notOver49(uuid);
            // 待操作者才能操作该条舆情（管理员除外）
            targeterCanOp(uuid);

            // step-2：修改舆情的状态
            UserInfo operatorUser = UserContext.getUser();
            Map<String, Object> map = Maps.newHashMap();
            map.put(EsConstant.opStatusField, 1);
            map.put(EsConstant.opOwnerField, targeterId);
            map.put(EsConstant.transferTypeField, param.getTransferType());
            map.put(EsConstant.recordTimeField, DateUtil.formatDateByPatten(now, "yyyy-MM-dd HH:mm:ss"));
            esModifyService.updateOpinion(operatorUser, targeterId, uuid, map);
        }

        // step-3：记录转发记录
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
                        o.setIsDelete(event.getIsDelete().intValue());
                        o.setIsFile(event.getFileReason() == null ? 0 : 1);
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

    // 舆情转发次数上限为49
    private void notOver49(String uuid) {
        Map<String, Object> keyMap = Maps.newHashMap();
        keyMap.put(EsConstant.uuidField, uuid);
        keyMap.put(EsConstant.opTypeField, 1);
        List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 49);
        if(list.size() >= 49) {
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "该任务已达转发次数上限，请执行监测或解除！");
        }
    }

    // 普通用户不能转发给管理员
    private void notToAdmin(Long userId) {
        Optional<Account> op = accountService.loadByUserId(userId);
        if(op.isPresent()) {
            Account a = op.get();
            boolean isAdmin = a.getAdmin();
            if(!UserContext.isAdmin() && isAdmin) {
                throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "普通用户不能转发给管理员");
            }
        }
    }

    // 不能转发给自己
    private void notToOneself(Long targeterId) {
        Long operatorId = UserContext.getUser().getId();
        if (targeterId.compareTo(operatorId) == 0) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "不能转发给自己");
    }

    // 待操作者才能操作本条舆情(管理员除外)
    private void targeterCanOp(String uuid) {
        OpinionEsVO opinion = esQueryService.getOpinionByUUID(uuid);
        UserInfo user = UserContext.getUser();
        // 如果不是超级管理员，判断当前用户是否是该条舆情的待操作者
        if(!UserContext.isAdmin()) {
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

    // 非任务中的舆情不能做解除操作
    private void checkHotLevel(Integer hot) {
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3);
        Integer level = systemSettingService.judgeOpinionSettingClass(hot, setting);
        if(level == -1) {
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "非任务中热点舆情不能解除");
        }
    }

    // 校验舆情是否处于任务当中
    private boolean checkOpinionTasking(String uuid) {
        Boolean result = esQueryService.checkOpinionTasking(uuid);
        return result;
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

        synchronized (this) {
            // step-1：校验权限
            OpinionEsVO opinion = esQueryService.getOpinionByUUID(uuid);
            // 待操作者才能操作该条舆情（管理员除外）
            targeterCanOp(uuid);
            // 没有处于任务中的热点舆情不能被解除
            boolean isTasking = checkOpinionTasking(uuid);
            if (!isTasking) {
                checkHotLevel(opinion.getHot());
            }

            // step-2：修改舆情记录
            UserInfo operator = UserContext.getUser();
            Map<String, Object> map = Maps.newHashMap();
            map.put(EsConstant.removeNoteField, removeNote);
            map.put(EsConstant.opStatusField, 2);
            map.put(EsConstant.opOwnerField, -1); // 解除之后，就没有目标操作者了
            map.put(EsConstant.recordTimeField, DateUtil.formatDateByPatten(now, "yyyy-MM-dd HH:mm:ss"));
            esModifyService.updateOpinion(operator, -1L, uuid, map);
        }

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
        result.setRecords(records);

        // step-3：设置级别
        List<WarnSetting> settings = systemSettingService.queryWarnSetting(3);
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot(), settings);
        result.setLevel(level);

        // step-4：解析舆情正文内容
        String content = result.getContent();
        if (StringUtils.isNotEmpty(content) && content.startsWith("[\"")) {
            JSONArray arr = JSONArray.parseArray(content);
            String contentHtml = BusinessUtils.buildContent(new ArrayList(arr));
            result.setContent(contentHtml);
        }
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

}
