/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.vo;

/**
 * 消费舆情条数统计
 * @author tjwang
 * @version $Id: OpinionCountStatVO.java, v 0.1 2017/10/31 0031 9:57 tjwang Exp $
 */
public class OpinionCountStatVO {

    /**
     * 预警总量
     */
    private Long total      = 0L;

    /**
     * 一级预警量
     */
    private Long levelOne   = 0L;

    /**
     * 二级预警量
     */
    private Long levelTwo   = 0L;

    /**
     * 三级预警量
     */
    private Long levelThree = 0L;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(Long levelOne) {
        this.levelOne = levelOne;
    }

    public Long getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(Long levelTwo) {
        this.levelTwo = levelTwo;
    }

    public Long getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(Long levelThree) {
        this.levelThree = levelThree;
    }
}
