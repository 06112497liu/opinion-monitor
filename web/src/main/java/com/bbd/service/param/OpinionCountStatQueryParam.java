/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

/**
 *
 * @author tjwang
 * @version $Id: OpinionCountStatQueryParam.java, v 0.1 2017/10/31 0031 10:04 tjwang Exp $
 */
public class OpinionCountStatQueryParam {

    /**
     * 状态：0. 全部; 1. 已处理; 2. 未处理
     */
    private Integer status;

    /**
     * 时间跨度：1. 本日；2. 本周； 3. 本月； 4. 本年； 5. 全部。
     */
    private Integer timeSpan;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(Integer timeSpan) {
        this.timeSpan = timeSpan;
    }
}
