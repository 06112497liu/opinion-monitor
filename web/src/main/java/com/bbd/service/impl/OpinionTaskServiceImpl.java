package com.bbd.service.impl;

import com.bbd.domain.User;
import com.bbd.service.EsModifyService;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionTaskService;
import com.bbd.service.UserService;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionTaskListVO;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Optional;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    /**
     * 当前用户待处理舆情列表
     * @param transferType 转发类型: 1. 请示，2. 回复
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getUnProcessedList(Integer transferType, PageBounds pb) {
        Long userId = UserContext.getUser().getId();
        PageList<OpinionTaskListVO> result = esQueryService.getUnProcessedList(userId, transferType, pb);
        return result;
    }

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb) {
        PageList<OpinionTaskListVO> result = esQueryService.getProcessedList(opStatus, pb);
        return result;
    }

    /**
     * 转发舆情
     * @param param
     */
    @Override
    public ReplicationResponse.ShardInfo transferOpinion(TransferParam param) throws IOException, ExecutionException, InterruptedException {
        // step-1：修改舆情的状态
        UserInfo operator = UserContext.getUser();
        User opOwner = userService.queryUserByUserame(param.getUsername()).get();
        ReplicationResponse.ShardInfo info = esModifyService.transferOpinion(operator, opOwner.getId(), param);

        // step-2：记录转发记录
        return info;
    }
}
    
    