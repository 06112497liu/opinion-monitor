/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

/**
 * 事件新增观点预警POJO
 * @author tjwang
 * @version $Id: EventIncMsgModel.java, v 0.1 2017/11/16 0016 16:34 tjwang Exp $
 */
public class EventIncMsgModel extends MsgModel {

    private String event;

    private String count;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
