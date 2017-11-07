package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: OpinionTaskListVO.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public class OpinionTaskListVO extends OpinionVO {

    /**
     * 舆情操作记录
     */
    private OpinionOpRecordVO opinionOpRecord;

    public OpinionOpRecordVO getOpinionOpRecord() {
        return opinionOpRecord;
    }

    public void setOpinionOpRecord(OpinionOpRecordVO opinionOpRecord) {
        this.opinionOpRecord = opinionOpRecord;
    }
}
    
    