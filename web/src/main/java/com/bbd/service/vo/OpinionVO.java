

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author daijinlong 
 * @version $Id: OpinionVO.java, v 0.1 2017年10月31日 下午3:07:22 daijinlong Exp $ 
 */
public class OpinionVO {
    
    private String uuid;
    
    private String title;
    
    private String summary;
    
    private Integer hot;
    
    private Integer similiar;
    
    private Integer emotion;
    
    private String website;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date calcTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publicTime;
    
    private Integer level;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getSimiliar() {
        return similiar;
    }

    public void setSimiliar(Integer similiar) {
        this.similiar = similiar;
    }

    public Integer getEmotion() {
        return emotion;
    }

    public void setEmotion(Integer emotion) {
        this.emotion = emotion;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }

    public Date getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(Date publicTime) {
        this.publicTime = publicTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}

