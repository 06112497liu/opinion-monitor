package com.bbd.service.vo;

import com.bbd.bean.OpinionWarnTime;
import com.bbd.domain.KeyValueVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: HistoryOpinionDetailVO.java, v0.1 2017/11/23 Liuweibo Exp $$
 */
public class HistoryOpinionDetailVO {
    private String          uuid;

    private String          title;

    private String          summary;

    private String          content;

    private Integer         hot;

    private Integer level;

    private String          link;

    private Integer         similiarCount;

    private Integer         commentCount;

    private Integer         emotion;

    // 词云
    private List<KeyValueVO> keywords;

    private String          website;

    private String          source;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date publishTime;

    private Long            opOwner;

    private Date firstWarnTime;

    /** 操作记录 */
    private List<OpinionOpRecordVO> records;

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
        return keywords;
    }

    public void setKeywords(List<KeyValueVO> keywords) {
        this.keywords = keywords;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Long getOpOwner() {
        return opOwner;
    }

    public void setOpOwner(Long opOwner) {
        this.opOwner = opOwner;
    }

    public Date getFirstWarnTime() {
        return firstWarnTime;
    }

    public void setFirstWarnTime(Date firstWarnTime) {
        this.firstWarnTime = firstWarnTime;
    }

    public List<OpinionOpRecordVO> getRecords() {
        return records;
    }

    public void setRecords(List<OpinionOpRecordVO> records) {
        this.records = records;
    }
}
    
    