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
     * 舆情操作记录
     */
    private List<OpinionOpRecordVO> records;

    /**
     * 添加监测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date MonitorTime;

    public List<OpinionOpRecordVO> getRecords() {
        return records;
    }

    public void setRecords(List<OpinionOpRecordVO> records) {
        this.records = records;
    }

    public Date getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(Date monitorTime) {
        MonitorTime = monitorTime;
    }
}
    
    