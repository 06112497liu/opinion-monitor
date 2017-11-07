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
public class OpinionEsVO {

    /** 1.舆情基本信息 */

    private String   uuid;

    private String   title;

    private String   summary;

    private String   content;

    private Integer  hot;

    private String link;

    private Integer  similiarCount;

    private Integer commentCount;

    private Integer  emotion;

    /**
     * 关键词
     */
    private String[] keyword;

    /**
     * 词云
     */
    private String[] keys;

    private String   website;

    /**
     * 1.新闻；2.微博；3.微信；4.政务；5.网站；6.论坛；7.其他
     */
    private Integer mediaType;

    /**
     * 来源
     */
    private String source;

    /**
     * 所属事件
     */
    private Integer[] events;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date     calcTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date     publishTime;

    /** 2.操作信息 */

    /**
     *  0. 未操作；1. 转发；2. 已解除； 3. 已监控
     */
    private Integer opStatus;

    /**
     * 待操作者
     */
    private Integer opOwner;

    /**
     * 操作者
     */
    private Integer[] operators;

    /**
     * 转发类型
     */
    private Integer transferType;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSimiliarCount() {
        return similiarCount;
    }

    public void setSimiliarCount(Integer similiarCount) {
        this.similiarCount = similiarCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getEmotion() {
        return emotion;
    }

    public void setEmotion(Integer emotion) {
        this.emotion = emotion;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer[] getEvents() {
        return events;
    }

    public void setEvents(Integer[] events) {
        this.events = events;
    }

    public Integer getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    public Integer getOpOwner() {
        return opOwner;
    }

    public void setOpOwner(Integer opOwner) {
        this.opOwner = opOwner;
    }

    public Integer[] getOperators() {
        return operators;
    }

    public void setOperators(Integer[] operators) {
        this.operators = operators;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}
