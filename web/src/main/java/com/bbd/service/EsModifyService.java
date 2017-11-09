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
     * @param operator
     * @param uuid
     * @param fieldMap
     */
    void updateOpinion(UserInfo operator, String uuid, Map<String, Object> fieldMap) throws IOException, ExecutionException, InterruptedException;

    /**
     * 添加舆情操作记录
     * @param recordVO
     */
    void recordOpinionOp(OpinionOpRecordVO recordVO);


}
