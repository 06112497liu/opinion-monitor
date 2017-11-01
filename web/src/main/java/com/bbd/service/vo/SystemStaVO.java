package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: SystemStaVO.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class SystemStaVO {

    private Integer runDays;

    private Integer dayAdd;

    private Integer weekAdd;

    private Integer monthAdd;

    public SystemStaVO(Integer runDays, Integer dayAdd, Integer weekAdd, Integer monthAdd) {
        this.runDays = runDays;
        this.dayAdd = dayAdd;
        this.weekAdd = weekAdd;
        this.monthAdd = monthAdd;
    }

    public SystemStaVO() {
    }

    public Integer getRunDays() {
        return runDays;
    }

    public void setRunDays(Integer runDays) {
        this.runDays = runDays;
    }

    public Integer getDayAdd() {
        return dayAdd;
    }

    public void setDayAdd(Integer dayAdd) {
        this.dayAdd = dayAdd;
    }

    public Integer getWeekAdd() {
        return weekAdd;
    }

    public void setWeekAdd(Integer weekAdd) {
        this.weekAdd = weekAdd;
    }

    public Integer getMonthAdd() {
        return monthAdd;
    }

    public void setMonthAdd(Integer monthAdd) {
        this.monthAdd = monthAdd;
    }

    @Override
    public String toString() {
        return "SystemStaVO{" +
                "runDays=" + runDays +
                ", dayAdd=" + dayAdd +
                ", weekAdd=" + weekAdd +
                ", monthAdd=" + monthAdd +
                '}';
    }
}
    
    