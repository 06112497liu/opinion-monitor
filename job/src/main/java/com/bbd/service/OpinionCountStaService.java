package com.bbd.service;

import com.bbd.dao.OpinionIncreaseStatisticDao;
import com.bbd.dao.OpinionWarnCountStatisticDao;
import com.bbd.domain.OpinionIncreaseStatistic;
import com.bbd.domain.OpinionWarnCountStatistic;
import com.bbd.domain.OpinionWarnCountStatisticExample;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 舆情统计任务
 * @author Liuweibo
 * @version Id: OpinionStaService.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OpinionCountStaService {

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
        // step-1：前置校验
        Preconditions.checkNotNull(state, "state不能为空");
        Range<Integer> range = Range.closed(1, 3);
        boolean legal = range.contains(state);
        if(!legal) throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "state必须在[1,3]范围内");
        // step-2：采集前校验当前时间点是否已经采集
        Date d = getHourTime(DateTime.now());
        boolean flag = checkExist(d, state);
        if(flag) return 0;
        // step-3：执行采集
        int one = getIfNull(levelOne, 0); int two = getIfNull(levelTwo, 0); int three = getIfNull(levelThree, 0);
        OpinionWarnCountStatistic o = new OpinionWarnCountStatistic();
        o.setState(state);o.setLevelOne(one);o.setLevelTwo(two);o.setLevelThree(three);
        o.setTotal(one + two + three);
        o.setRecordTime(d);
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

    // 校验预警舆情采集任务是否已经存在某条数据
    private boolean checkExist(Date date, Integer state) {
        OpinionWarnCountStatisticExample example = new OpinionWarnCountStatisticExample();
        example.createCriteria().andStateEqualTo(state).andRecordTimeEqualTo(date);
        List<OpinionWarnCountStatistic> w = warnCountStatisticDao.selectByExample(example);
        return !w.isEmpty();
    }

    // null值处理
    private Integer getIfNull(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    // 获取当前时间的整点
    private Date getHourTime(DateTime dt) {
        int year = dt.getYear();
        int month = dt.getMonthOfYear();
        int day = dt.getDayOfMonth();
        int hour = dt.getHourOfDay();
        DateTime d = new DateTime(year, month, day, hour, 0, 0);
        return new Date(d.getMillis());
    }

}
    
    