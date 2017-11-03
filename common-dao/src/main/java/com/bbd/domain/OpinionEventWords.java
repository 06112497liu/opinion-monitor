package com.bbd.domain;

import java.util.Date;

public class OpinionEventWords {
    private Long id;

    private Long eventId;

    private Byte cycle;

    private Date gmtCreate;

    private Date gmtModified;

    private String words;

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

    public Byte getCycle() {
        return cycle;
    }

    public void setCycle(Byte cycle) {
        this.cycle = cycle;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}