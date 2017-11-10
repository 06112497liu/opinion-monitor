package com.bbd.domain;

import java.util.Date;

public class OpinionEventWholeTrendStatistic {
    private Long id;

    private Long eventId;

    private Integer infoTotal;

    private Integer warnTotal;

    private Date pickTime;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getInfoTotal() {
        return infoTotal;
    }

    public void setInfoTotal(Integer infoTotal) {
        this.infoTotal = infoTotal;
    }

    public Integer getWarnTotal() {
        return warnTotal;
    }

    public void setWarnTotal(Integer warnTotal) {
        this.warnTotal = warnTotal;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
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