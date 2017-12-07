package com.bbd.service;

import com.bbd.domain.KeyValueVO;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionTaskListVO;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.elasticsearch.action.support.replication.ReplicationResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 舆情任务服务
 * @author Liuweibo
 * @version Id: OpinionTaskService.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public interface OpinionTaskService {

    /**
     * 当前用户待处理舆情列表
     * @param transferType 转发类型: 1/2/3：请示，4/5/6：回复
     * @return
     */
    PageList<OpinionTaskListVO> getUnProcessedList(Integer transferType, PageBounds pb);

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发（介入）；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb);

    /**
     * 转发舆情
     * @param param
     */
    void transferOpinion(TransferParam param) throws IOException, ExecutionException, InterruptedException;

    /**
     * 解除预警
     * @param uuid
     * @param removeReason
     * @param removeNote
     */
    void removeWarn(String uuid, Integer removeReason, String removeNote) throws InterruptedException, ExecutionException, IOException;

    /**
     * 查询处于任务舆情中的舆情详情
     * @param uuid
     * @param type
     * @return
     */
    OpinionTaskListVO getTransferDetail(String uuid, Integer type);

    /**
     * 当前用户任务列表统计
     * @return
     */
    List<KeyValueVO> getTaskSta();

}
    
    