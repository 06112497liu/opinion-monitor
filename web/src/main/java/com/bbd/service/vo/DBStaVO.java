package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: DBStaVO.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class DBStaVO {
    private Long historyTotal;

    private Long dayAdd;

    private Long weekAdd;

    private Long monthAdd;

    public DBStaVO() {
    }

    public DBStaVO(Long historyTotal, Long dayAdd, Long weekAdd, Long monthAdd) {
        this.historyTotal = historyTotal;
        this.dayAdd = dayAdd;
        this.weekAdd = weekAdd;
        this.monthAdd = monthAdd;
    }

    public Long getHistoryTotal() {
        return historyTotal;
    }

    public void setHistoryTotal(Long historyTotal) {
        this.historyTotal = historyTotal;
    }

    public Long getDayAdd() {
        return dayAdd;
    }

    public void setDayAdd(Long dayAdd) {
        this.dayAdd = dayAdd;
    }

    public Long getWeekAdd() {
        return weekAdd;
    }

    public void setWeekAdd(Long weekAdd) {
        this.weekAdd = weekAdd;
    }

    public Long getMonthAdd() {
        return monthAdd;
    }

    public void setMonthAdd(Long monthAdd) {
        this.monthAdd = monthAdd;
    }

    @Override
    public String toString() {
        return "DBStaVO{" +
                "historyTotal=" + historyTotal +
                ", dayAdd=" + dayAdd +
                ", weekAdd=" + weekAdd +
                ", monthAdd=" + monthAdd +
                '}';
    }
}
    
    