package com.bbd.domain;

import java.util.Date;

public class OpinionEventLevelChange {
    private Long id;

    private Long eventId;

    private Byte level;

    private Date gmtLevelChange;

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

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Date getGmtLevelChange() {
        return gmtLevelChange;
    }

    public void setGmtLevelChange(Date gmtLevelChange) {
        this.gmtLevelChange = gmtLevelChange;
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