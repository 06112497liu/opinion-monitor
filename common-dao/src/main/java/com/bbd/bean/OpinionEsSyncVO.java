/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 舆情同步到ES中的数据结构 - 操作类型的数据不同步
 *
 * @author tjwang
 * @version $Id: OpinionEsSyncVO.java, v 0.1 2017/11/9 0009 14:26 tjwang Exp $
 */
public class OpinionEsSyncVO implements EsBase {

    /** 1.舆情基本信息 */

    private String          uuid;

    private String          title;

    private String          summary;

    private String          content;

    private Integer         hot;

    private String          link;

    private Integer         similiarCount;

    private Integer         commentCount;

    private Integer         emotion;

    /**
     * 关键词
     */
    private String          keywords;

    /**
     * 词云
     */
    private List<String>    keys;

    private String          website;

    /**
     * 1.新闻；2.微博；3.微信；4.政务；5.网站；6.论坛；7.其他
     */
    private Integer         mediaType;

    /**
     * 来源
     */
    private String          source;

    /**
     * 所属事件
     */
    private List<Long>      events;


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date            publishTime;
    /**
     * 标记是否为消费舆情
     */
    private Integer         flag;

    private OpinionWarnTime warnTime;

    @Override
    @JSONField(serialize = false)
    public String getEsId() {
        return getUuid();
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Long> getEvents() {
        return events;
    }

    public void setEvents(List<Long> events) {
        this.events = events;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public OpinionWarnTime getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(OpinionWarnTime warnTime) {
        this.warnTime = warnTime;
    }
}
