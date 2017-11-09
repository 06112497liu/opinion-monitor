package com.bbd.service.impl;

import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.User;
import com.bbd.enums.TransferEnum;
import com.bbd.enums.WarnReasonEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.*;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.service.vo.OpinionTaskListVO;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final String opOwnerField = "opOwner";
    private final String opStatusField = "opStatus";
    private final String targeterField = "targeter";
    private final String transferTypeField = "transferType";
    private final String operatorsField = "operators";
    private final String uuidField = "uuid";
    private final String removeReasonField = "removeReason";
    private final String removeNoteField = "removeNote";

    /**
     * 当前用户待处理舆情列表
     * @param transferType 转发类型: 1/2/3：请示，4/5/6：回复
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getUnProcessedList(Integer transferType, PageBounds pb) {
        Long userId = UserContext.getUser().getId();
        PageList<OpinionTaskListVO> result = esQueryService.getUnProcessedList(userId, transferType, pb);
        result.forEach(o -> {
            o.setLevel(settingService.judgeOpinionSettingClass(o.getHot()));
            String uuid = o.getUuid();
            String username = UserContext.getUser().getUsername();
            Map<String, Object> keyMap = Maps.newHashMap();
            keyMap.put("uuid", uuid); keyMap.put("targeter", username);
            List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 1);
            o.setRecords(list);
        });
        return result;
    }

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发（介入）；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb) {
        PageList<OpinionTaskListVO> result = esQueryService.getProcessedList(opStatus, pb);
        if(Objects.nonNull(opStatus)) {
            if(opStatus == 1 && !UserContext.isAdmin()) { // 如果是转发列表，并且不是管理员的话，查询当前用户最近一条转发记录
                result.forEach(o -> {
                    String uuid = o.getUuid();
                    String username = UserContext.getUser().getUsername();
                    Map<String, Object> keyMap = Maps.newHashMap();
                    keyMap.put(uuidField, uuid); keyMap.put(operatorsField, username);
                    List<OpinionOpRecordVO> list = esQueryService.getOpinionOpRecordByUUID(keyMap, 1);
                    o.setRecords(list);
                });
            }
            if(opStatus == 3) { // 如果是已监控页面，查询事件的一些信息
                OpinionEventExample exam = new OpinionEventExample();
                opinionEventDao.selectByExample(exam);
            }
        }
        return result;
    }

    /**
     * 转发舆情
     * @param param
     */
    @Override
    public void transferOpinion(TransferParam param) throws IOException, ExecutionException, InterruptedException {
        // step-1：修改舆情的状态
        UserInfo operator = UserContext.getUser();
        if(Objects.isNull(operator)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        User opOwner = userService.queryUserByUserame(param.getUsername()).get();

        Map<String, Object> map = Maps.newHashMap();
        map.put(opStatusField, 1); map.put(opOwnerField, opOwner.getId()); map.put(transferTypeField, param.getTransferType());
        esModifyService.updateOpinion(operator, param.getUuid(), map);

        // step-2：记录转发记录
            // 构建转发记录对象
        OpinionOpRecordVO recordVO = new OpinionOpRecordVO();
        recordVO.setUuid(param.getUuid());
        recordVO.setOpType(1);
        recordVO.setTransferType(param.getTransferType());
        recordVO.setTransferNote(param.getTransferNote());
        recordVO.setOperator(operator.getUsername());
        recordVO.setTargeter(opOwner.getUsername());
        recordVO.setOpTime(new Date());
        recordVO.setTransferContent(TransferEnum.getDescByCode(param.getTransferType().toString()));

            // 向es中添加转发记录
        esModifyService.recordOpinionOp(recordVO);
    }

    /**
     * 解除预警
     * @param uuid
     * @param removeReason
     * @param removeNote
     */
    @Override
    public void removeWarn(String uuid, Integer removeReason, String removeNote) throws InterruptedException, ExecutionException, IOException {
        // step-1：修改舆情记录
        UserInfo operator = UserContext.getUser();
        Map<String, Object> map = Maps.newHashMap();
        map.put(removeNoteField, removeNote); map.put(removeNoteField, removeNote); map.put(opStatusField, 2);
        esModifyService.updateOpinion(operator, uuid, map);

        // step-2：记录解除记录
        OpinionOpRecordVO recordVO = new OpinionOpRecordVO();
        recordVO.setOpTime(new Date());
        recordVO.setOperator(UserContext.getUser().getUsername());
        recordVO.setUuid(uuid);
        recordVO.setRemoveReason(removeReason);
        recordVO.setRemoveContent(WarnReasonEnum.getDescByCode(removeReason.toString()));
        recordVO.setRemoveNote(removeNote);
        esModifyService.recordOpinionOp(recordVO);
    }
}
    
    