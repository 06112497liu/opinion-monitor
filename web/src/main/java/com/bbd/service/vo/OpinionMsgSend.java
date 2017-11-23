package com.bbd.service.vo;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: OpinionMsgSend.java, v0.1 2017/11/23 Liuweibo Exp $$
 */
public class OpinionMsgSend {
    /** 消息发送json串 */
    private String sendMsg;

    /** 计算时间 */
    private Date claTime;

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public Date getClaTime() {
        return claTime;
    }

    public void setClaTime(Date claTime) {
        this.claTime = claTime;
    }
}
    
    