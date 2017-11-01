package com.bbd.domain;

import java.util.Date;

public class OpinionIncreaseStatistic {
    private Long id;

    private Integer total;

    private Integer dayIncrease;

    private Integer weekIncrease;

    private Integer monthIncrease;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getDayIncrease() {
        return dayIncrease;
    }

    public void setDayIncrease(Integer dayIncrease) {
        this.dayIncrease = dayIncrease;
    }

    public Integer getWeekIncrease() {
        return weekIncrease;
    }

    public void setWeekIncrease(Integer weekIncrease) {
        this.weekIncrease = weekIncrease;
    }

    public Integer getMonthIncrease() {
        return monthIncrease;
    }

    public void setMonthIncrease(Integer monthIncrease) {
        this.monthIncrease = monthIncrease;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}