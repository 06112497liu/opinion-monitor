package com.bbd.param;

import com.bbd.domain.SimiliarNews;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: OpinionInfo.java, v0.1 2017/10/27 Liuweibo Exp $$
 */
public class OpinionInfo {
    private Long id;

    private String uuid;

    private String title;

    private String summary;

    private String source;

    private Integer sourceType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    private Integer hot;

    private Integer warnClass;

    private Integer emotion;

    private Integer similiar;

    private String words;

    private Integer commentCount;

    private String content;

    private String link;

    private List<SimiliarNews> similiarNewsList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getWarnClass() {
        return warnClass;
    }

    public void setWarnClass(Integer warnClass) {
        this.warnClass = warnClass;
    }

    public Integer getEmotion() {
        return emotion;
    }

    public void setEmotion(Integer emotion) {
        this.emotion = emotion;
    }

    public Integer getSimiliar() {
        return similiar;
    }

    public void setSimiliar(Integer similiar) {
        this.similiar = similiar;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<SimiliarNews> getSimiliarNewsList() {
        return similiarNewsList;
    }

    public void setSimiliarNewsList(List<SimiliarNews> similiarNewsList) {
        this.similiarNewsList = similiarNewsList;
    }

    @Override
    public String toString() {
        return "OpinionInfo{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", source='" + source + '\'' +
                ", sourceType=" + sourceType +
                ", startTime=" + startTime +
                ", hot=" + hot +
                ", warnClass=" + warnClass +
                ", emotion=" + emotion +
                ", similiar=" + similiar +
                ", words='" + words + '\'' +
                ", commentCount=" + commentCount +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", similiarNewsList=" + similiarNewsList +
                '}';
    }
}
    
    