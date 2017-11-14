/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 舆情预警时间
 * @author tjwang
 * @version $Id: OpinionWarnTime.java, v 0.1 2017/11/13 0013 18:07 tjwang Exp $
 */
public class OpinionWarnTime {

    /**
     * 此一次达到预警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date firstWarnTime;

    /**
     * 第一次到达一级预警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date firstWarnTimeOne;

    /**
     * 第一次到达二级预警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date firstWarnTimeTwo;

    /**
     * 第一次达到三级预警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date firstWarnTimeThree;

    public Date getFirstWarnTime() {
        return firstWarnTime;
    }

    public void setFirstWarnTime(Date firstWarnTime) {
        this.firstWarnTime = firstWarnTime;
    }

    public Date getFirstWarnTimeOne() {
        return firstWarnTimeOne;
    }

    public void setFirstWarnTimeOne(Date firstWarnTimeOne) {
        this.firstWarnTimeOne = firstWarnTimeOne;
    }

    public Date getFirstWarnTimeTwo() {
        return firstWarnTimeTwo;
    }

    public void setFirstWarnTimeTwo(Date firstWarnTimeTwo) {
        this.firstWarnTimeTwo = firstWarnTimeTwo;
    }

    public Date getFirstWarnTimeThree() {
        return firstWarnTimeThree;
    }

    public void setFirstWarnTimeThree(Date firstWarnTimeThree) {
        this.firstWarnTimeThree = firstWarnTimeThree;
    }
}
