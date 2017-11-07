package com.bbd.service.vo;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: OpinionOpRecordVO.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public class OpinionOpRecordVO {

    private String uuid;

    /**
     * 操作类型, 1. 转发；2. 解除；3. 监测
     */
    private Integer opType;

    /**
     * 转发类型: 1. 请示，2. 回复
     */
    private Integer transferType;

    /**
     * 转发备注
     */
    private String transferNote;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 目标对象
     */
    private String targeter;

    /**
     * 操作时间
     */
    private Date opTime;

    /**
     * 解除理由
     */
    private String removeReason;

    /**
     * 解除备注
     */
    private String removeNote;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public String getTransferNote() {
        return transferNote;
    }

    public void setTransferNote(String transferNote) {
        this.transferNote = transferNote;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTargeter() {
        return targeter;
    }

    public void setTargeter(String targeter) {
        this.targeter = targeter;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(String removeReason) {
        this.removeReason = removeReason;
    }

    public String getRemoveNote() {
        return removeNote;
    }

    public void setRemoveNote(String removeNote) {
        this.removeNote = removeNote;
    }
}
    
    