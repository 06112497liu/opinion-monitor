/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
package com.bbd.vo;

import com.alibaba.fastjson.JSONArray;
import com.bbd.domain.KeyValueVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Kafka推送的舆情数据
 * @author tjwang
 * @version $Id: OpinionVO.java, v 0.1 2017年10月31日 下午3:07:22 tjwang Exp $
 */
public class OpinionVO {

    private String     uuid;

    private String     title;

    private String     summary;

    private Float      hot;

    private String     content;

    private String     source;

    private String     link;

    private Integer    similiarCount;

    private Integer    commentCount;

    private Float      emotion;

    private String     keywords;

    private String     keys;

    private String     website;

    private Integer    mediaType;

    private List<Long> events;

    private Integer    flag;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date       publishTime;

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
        //return hot;
        if (hot == null) {
            return 0;
        }
        if (hot < 0) {
            return 0;
        }
        return Float.valueOf(hot).intValue();
    }

    public void setHot(Float hot) {
        this.hot = hot;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        if (emotion == null) {
            return 0;
        }
        return emotion == 0 ? 0 : emotion > 0 ? 1 : -1;
    }

    public void setEmotion(Float emotion) {
        this.emotion = emotion;
    }

    public String getKeywords() {
//        ArrayList<KeyValueVO> result = Lists.newArrayList();
//        if (StringUtils.isEmpty(this.keywords)) {
//            return result;
//        }
//        List<List> list = JSONArray.parseArray(this.keywords, List.class);
//        for (List l : list) {
//            String key = (String)l.get(0);
//            Double val = ((BigDecimal)l.get(1)).doubleValue();
//            KeyValueVO vo = new KeyValueVO();
//            vo.setKey(key);
//            vo.setName(key);
//            vo.setValue(val);
//            result.add(vo);
//        }
//        return result;
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeys() {
        if (keys != null && !keys.equals("")) {
            return Arrays.asList(keys.split(","));
        }
        return Lists.newArrayList();
    }

    public void setKeys(String keys) {
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
}
