/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

/**
 * 事件总体热度等级预警POJO
 * @author tjwang
 * @version $Id: EventMsgModel.java, v 0.1 2017/11/16 0016 16:35 tjwang Exp $
 */
public class EventMsgModel extends MsgModel {

    private String event;

    private String level;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
