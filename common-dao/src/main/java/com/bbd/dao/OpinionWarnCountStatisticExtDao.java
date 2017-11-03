package com.bbd.dao;

import com.bbd.domain.OpinionWarnCountStatistic;

/**
 * @author Liuweibo
 * @version Id: OpinionWarnCountStatisticExtDao.java, v0.1 2017/11/2 Liuweibo Exp $$
 */
public interface OpinionWarnCountStatisticExtDao {

    /**
     * 获取预警舆情统计信息
     * @param state
     * @param timeSpan
     * @return
     */
    OpinionWarnCountStatistic countWarnOpinionByTimeSpan(Integer state, Integer timeSpan);

}
    
    