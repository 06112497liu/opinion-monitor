package com.bbd.service;

import com.bbd.service.vo.OpinionTaskListVO;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;

/**
 * 舆情任务服务
 * @author Liuweibo
 * @version Id: OpinionTaskService.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public interface OpinionTaskService {

    /**
     * 当前用户待处理舆情列表
     * @param transferType 转发类型: 1. 请示，2. 回复
     * @return
     */
    PageList<OpinionTaskListVO> getUnProcessedList(Integer transferType, PageBounds pb);

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb);

}
    
    