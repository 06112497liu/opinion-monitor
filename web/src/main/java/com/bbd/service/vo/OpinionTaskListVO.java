package com.bbd.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

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


}
    
    