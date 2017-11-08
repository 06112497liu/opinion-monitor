package com.bbd.service;

import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.vo.UserInfo;
import org.elasticsearch.action.support.replication.ReplicationResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Liuweibo
 * @version Id: EsModifyService.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public interface EsModifyService {

    /**
     * 转发舆情
     * @param param
     */
    ReplicationResponse.ShardInfo transferOpinion(UserInfo operator, Long opOwnerId, TransferParam param) throws IOException, ExecutionException, InterruptedException;

    /**
     * 添加转发记录
     * @param recordVO
     */
    void recordTransfer(OpinionOpRecordVO recordVO);

}
    
    