/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

/**
 *
 * @author tjwang
 * @version $Id: OpinionEsQueryVO.java, v 0.1 2017/11/30 0030 17:41 tjwang Exp $
 */
public class OpinionEsQueryVO {

    /** 1.舆情基本信息 */

    private String          uuid;

    private Integer         hot;

    /**
     * 标记是否为消费舆情
     */
    private Integer         flag;
    /**
     *  0. 未操作；1. 转发；2. 已解除； 3. 已监控
     */
    private Integer         opStatus;

    private OpinionWarnTime warnTime;

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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    public OpinionWarnTime getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(OpinionWarnTime warnTime) {
        this.warnTime = warnTime;
    }
}
