package com.bbd.service.vo;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.bbd.domain.KeyValueVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: OpinionTaskListVO.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public class OpinionTaskListVO extends OpinionVO {

    /**
     * 舆情详情
     */
    private String content;

    /**
     * 舆情操作记录
     */
    private List<OpinionOpRecordVO> records;

    /**
     * 所属事件名称
     */
    private String eventName;

    /**
     * 所属事件id
     */
    private Long eventID;

    /**
     * 词云
     */
    private List<KeyValueVO> keywords;

    /**
     * 关键词
     */
    private String[] keys;

    /**
     * 添加监测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date monitorTime;

    public List<OpinionOpRecordVO> getRecords() {
        return records;
    }

    public void setRecords(List<OpinionOpRecordVO> records) {
        this.records = records;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public Date getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(Date monitorTime) {
        this.monitorTime = monitorTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<KeyValueVO> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeyValueVO> keywords) {
        this.keywords = keywords;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
    
    