package com.bbd.service;

import com.bbd.dao.OpinionIncreaseStatisticDao;
import com.bbd.dao.OpinionWarnCountStatisticDao;
import com.bbd.domain.OpinionIncreaseStatistic;
import com.bbd.domain.OpinionWarnCountStatistic;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 舆情统计任务
 * @author Liuweibo
 * @version Id: OpinionStaService.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@Service
public class OpinionCountStaJob {

    @Autowired
    private OpinionWarnCountStatisticDao warnCountStatisticDao;

    @Autowired
    private OpinionIncreaseStatisticDao increaseStatisticDao;

    /**
     * 预警舆情不同时间点数量统计
     * @param state 状态，1.全部；2.已处理；3.未处理
     * @param levelOne 1级预警数量
     * @param levelTwo 2级预警数量
     * @param levelThree 3级预警数量
     * @return
     */
    public Integer warnOpinionCountSta(Integer state, Integer levelOne, Integer levelTwo, Integer levelThree) {
        Preconditions.checkNotNull(state, "state不能为空");
        int one = getIfNull(levelOne, 0); int two = getIfNull(levelTwo, 0); int three = getIfNull(levelThree, 0);
        OpinionWarnCountStatistic o = new OpinionWarnCountStatistic();
        o.setState(state);o.setLevelOne(one);o.setLevelTwo(two);o.setLevelThree(three);
        o.setTotal(one + two + three);
        o.setGmtCreate(getHourTime(DateTime.now()));
        Integer result = warnCountStatisticDao.insertSelective(o);
        return result;
    }

    /**
     * 舆情不同时间段数量统计
     * @param count
     * @return
     */
    public Integer opinionCoutSta(Integer count) {
        int num = getIfNull(count, 0);
        OpinionIncreaseStatistic o = new OpinionIncreaseStatistic();
        o.setCount(num);
        o.setRecordTime(getHourTime(DateTime.now()));
        Integer result = increaseStatisticDao.insertSelective(o);
        return result;
    }


    // null值处理
    private Integer getIfNull(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    // 获取当前时间的整分
    private Date getHourTime(DateTime dt) {
        int year = dt.getYear();
        int month = dt.getMonthOfYear();
        int day = dt.getDayOfMonth();
        int hour = dt.getHourOfDay();
        int minute = dt.getMinuteOfHour();
        DateTime d = new DateTime(year, month, day, hour, minute, 0);
        return new Date(d.getMillis());
    }

}
    
    