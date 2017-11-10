/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 舆情热度记录
 * 
 * @author tjwang
 * @version $Id: OpinionHotEsVO.java, v 0.1 2017/11/9 0009 13:47 tjwang Exp $
 */
public class OpinionHotEsVO implements EsBase {

    /**
     * 舆情号
     */
    private String  uuid;

    /**
     * 热度
     */
    private Integer hot;

    /**
     * 热点事件
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:00:00")
    private Date    hotTime;

    @Override
    @JSONField(serialize = false)
    public String getEsId() {
        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Date getHotTime() {
        return hotTime;
    }

    public void setHotTime(Date hotTime) {
        this.hotTime = hotTime;
    }
}
