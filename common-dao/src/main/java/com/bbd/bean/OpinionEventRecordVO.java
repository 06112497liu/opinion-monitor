/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.Date;

/**
 * 事件、舆情关系记录
 * @author tjwang
 * @version $Id: OpinionEventRecordVO.java, v 0.1 2017/11/13 0013 18:14 tjwang Exp $
 */
public class OpinionEventRecordVO {

    private String id;

    /**
     * 舆情UUID
     */
    private String opinionId;

    /**
     * 事件ID
     */
    private Long   eventId;

    /**
     * 匹配时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date   matchTime;

    /**
     * 匹配时间，忽略分、秒、毫秒
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date   matchTimeTrim;

    @Override
    public int hashCode() {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putString(opinionId, Charsets.UTF_8).putLong(eventId).hash();
        return Math.abs(hc.hashCode());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(String opinionId) {
        this.opinionId = opinionId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Date getMatchTimeTrim() {
        return matchTimeTrim;
    }

    public void setMatchTimeTrim(Date matchTimeTrim) {
        this.matchTimeTrim = matchTimeTrim;
    }
}
