package com.bbd.service.param;

/**
 * @author Liuweibo
 * @version Id: OpinionStaReport.java, v0.1 2017/12/8 Liuweibo Exp $$
 */
public class OpinionStaReport {
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

    /**
     * 正面舆情数量
     */
    private Long positive = 0L;

    /**
     * 负面舆情数量
     */
    private Long negative = 0L;

    /**
     * 中性舆情数量
     */
    private Long neutral;

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

    public Long getPositive() {
        return positive;
    }

    public void setPositive(Long positive) {
        this.positive = positive;
    }

    public Long getNegative() {
        return negative;
    }

    public void setNegative(Long negative) {
        this.negative = negative;
    }

    public Long getNeutral() {
        return neutral;
    }

    public void setNeutral(Long neutral) {
        this.neutral = neutral;
    }
}
    
    