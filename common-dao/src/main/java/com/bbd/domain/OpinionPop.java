package com.bbd.domain;

import java.util.Date;

public class OpinionPop {
    private Long id;

    private Long userId;

    private Byte type;

    private Date gmtPopLatest;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getGmtPopLatest() {
        return gmtPopLatest;
    }

    public void setGmtPopLatest(Date gmtPopLatest) {
        this.gmtPopLatest = gmtPopLatest;
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