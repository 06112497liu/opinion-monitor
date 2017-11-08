package com.bbd.service;

import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.vo.UserInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    void transferOpinion(UserInfo operator, Long opOwnerId, TransferParam param) throws IOException, ExecutionException, InterruptedException;

    /**
     * 添加转发记录
     * @param recordVO
     */
    void recordTransfer(OpinionOpRecordVO recordVO);

    /**
     * 获取某条舆情的转发记录
     * @param keyMap
     * @param size
     * @return
     */
    List<OpinionOpRecordVO> getOpinionOpRecordByUUID(Map<String, Object> keyMap, Integer size);

}
    
    