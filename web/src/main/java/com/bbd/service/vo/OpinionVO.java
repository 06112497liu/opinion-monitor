

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
    
    private Integer similiarCount;
    
    private Integer emotion;
    
    private String website;

    private String source;

    /**
     * 网站链接
     */
    private String link;

    private Integer mediaType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    
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

    public Integer getSimiliarCount() {
        return similiarCount;
    }

    public void setSimiliarCount(Integer similiarCount) {
        this.similiarCount = similiarCount;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

