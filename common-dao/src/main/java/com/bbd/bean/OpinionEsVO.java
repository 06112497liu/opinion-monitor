/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
package com.bbd.bean;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.bbd.domain.KeyValueVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 保存ES中的全量数据
 *
 * @author tjwang
 * @version $Id: OpinionVo.java, v 0.1 2017年10月31日 下午3:07:22 tjwang Exp $
 */
public class OpinionEsVO implements EsBase {

    /** 1.舆情基本信息 */
    private String          md5;

    private String          uuid;

    private String          title;

    private String          summary;

    private String          content;

    private Integer         hot;

    private Integer         level;

    private String          link;

    private Integer         similiarCount;

    private Integer         commentCount;

    private Integer         emotion;

    private String          keywords;

    private String[]        keys;

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
    private Integer[]       events;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date            publishTime;

    /** 2.操作信息 */

    /**
     *  0. 未操作；1. 转发；2. 已解除； 3. 已监控
     */
    private Integer         opStatus;

    /**
     * 操作记录时间
     */
    private Date            recordTime;

    /**
     * 待操作者
     */
    private Long            opOwner;

    /**
     * 操作者
     */
    private Long[]          operators;

    /**
     * 转发类型
     */
    private Integer         transferType;

    /**
     * 解除备注
     */
    private String          removeNote;

    /**
     * 预警时间
     */
    private OpinionWarnTime warnTime;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @JsonIgnore
    @Override
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

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public List<KeyValueVO> getKeywords() {
        ArrayList<KeyValueVO> result = Lists.newArrayList();
        if (StringUtils.isEmpty(this.keywords)) {
            return result;
        }
        List<List> list = JSONArray.parseArray(this.keywords, List.class);
        for (List l : list) {
            String key = (String)l.get(0);
            Double val = ((BigDecimal)l.get(1)).doubleValue();
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(key);
            vo.setName(key);
            vo.setValue(val);
            result.add(vo);
        }
        result.sort((v1, v2) -> {
            Double d1 = Double.parseDouble(v1.getValue().toString());
            Double d2 = Double.parseDouble(v2.getValue().toString());
            return -Double.compare(d1, d2);
        });
        return result;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public Long getOpOwner() {
        return opOwner;
    }

    public void setOpOwner(Long opOwner) {
        this.opOwner = opOwner;
    }

    public Long[] getOperators() {
        return operators;
    }

    public void setOperators(Long[] operators) {
        this.operators = operators;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public OpinionWarnTime getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(OpinionWarnTime warnTime) {
        this.warnTime = warnTime;
    }

    public String getRemoveNote() {
        return removeNote;
    }

    public void setRemoveNote(String removeNote) {
        this.removeNote = removeNote;
    }
}
