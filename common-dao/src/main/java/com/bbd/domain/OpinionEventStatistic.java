package com.bbd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OpinionEventStatistic {
    private Long id;

    private Long eventId;

    private Integer infoTotal;

    private String mediaType;

    private String source;

    private String dataType;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm")
    private Date gmtCreate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm")
    private Date gmtModified;

    private Integer warnTotal;

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

    public Integer getInfoTotal() {
        return infoTotal;
    }

    public void setInfoTotal(Integer infoTotal) {
        this.infoTotal = infoTotal;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public Integer getWarnTotal() {
        return warnTotal;
    }

    public void setWarnTotal(Integer warnTotal) {
        this.warnTotal = warnTotal;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}