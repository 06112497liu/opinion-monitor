package com.bbd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OpinionEventMediaStatistic {
    private Long id;

    private Long eventId;

    private String mediaCode;

    private Integer mediaCount;
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

    public String getMediaCode() {
        return mediaCode;
    }

    public void setMediaCode(String mediaCode) {
        this.mediaCode = mediaCode;
    }

    public Integer getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(Integer mediaCount) {
        this.mediaCount = mediaCount;
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