package com.bbd.service;

import com.bbd.dao.OpinionIncreaseStatisticDao;
import com.bbd.dao.OpinionWarnCountStatisticDao;
import com.bbd.domain.OpinionIncreaseStatistic;
import com.bbd.domain.OpinionWarnCountStatistic;
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
     * @param levelOne
     * @param levelTwo
     * @param levelThree
     * @return
     */
    public Integer warnOpinionCountSta(Integer levelOne, Integer levelTwo, Integer levelThree) {
        int one = getIfNull(levelOne, 0); int two = getIfNull(levelTwo, 0); int three = getIfNull(levelThree, 0);
        OpinionWarnCountStatistic o = new OpinionWarnCountStatistic();
        o.setLevelOne(one);o.setLevelTwo(two);o.setLevelThree(three);
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
    
    