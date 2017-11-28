package com.bbd.service.vo;

import com.bbd.job.vo.MsgVO;

import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: OpinionMsgSend.java, v0.1 2017/11/23 Liuweibo Exp $$
 */
public class OpinionMsgSend {
    /** 消息发送json串 */
    private List<MsgVO> sendMsg;

    /** 计算时间 */
    private Date claTime;

    public List<MsgVO> getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(List<MsgVO> sendMsg) {
        this.sendMsg = sendMsg;
    }

    public Date getClaTime() {
        return claTime;
    }

    public void setClaTime(Date claTime) {
        this.claTime = claTime;
    }
}
    
    