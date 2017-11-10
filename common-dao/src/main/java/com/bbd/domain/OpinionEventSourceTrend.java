package com.bbd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OpinionEventSourceTrend {
    private Long id;

    private Long eventId;

    private String sourceType;

    private Integer count;

    private Integer warnCount;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm")
    private Date pickTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm")
    private Date gmtCreate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm")
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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
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