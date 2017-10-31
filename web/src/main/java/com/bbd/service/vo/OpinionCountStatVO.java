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
    private Integer total;

    /**
     * 一级预警量
     */
    private Integer levelOne;

    /**
     * 二级预警量
     */
    private Integer levelTwo;

    /**
     * 三级预警量
     */
    private Integer levelThree;

    public OpinionCountStatVO() {
    }

    public OpinionCountStatVO(Integer total, Integer levelOne, Integer levelTwo, Integer levelThree) {
        this.total = total;
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.levelThree = levelThree;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(Integer levelOne) {
        this.levelOne = levelOne;
    }

    public Integer getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(Integer levelTwo) {
        this.levelTwo = levelTwo;
    }

    public Integer getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(Integer levelThree) {
        this.levelThree = levelThree;
    }
}
