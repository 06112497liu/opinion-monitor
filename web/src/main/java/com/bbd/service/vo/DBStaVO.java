package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: DBStaVO.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class DBStaVO {
    private Integer historyTotal;

    private Integer dayAdd;

    private Integer weekAdd;

    private Integer monthAdd;

    public DBStaVO() {
    }

    public DBStaVO(Integer historyTotal, Integer dayAdd, Integer weekAdd, Integer monthAdd) {
        this.historyTotal = historyTotal;
        this.dayAdd = dayAdd;
        this.weekAdd = weekAdd;
        this.monthAdd = monthAdd;
    }

    public Integer getHistoryTotal() {
        return historyTotal;
    }

    public void setHistoryTotal(Integer historyTotal) {
        this.historyTotal = historyTotal;
    }

    public Integer getDayAdd() {
        return dayAdd;
    }

    public void setDayAdd(Integer dayAdd) {
        this.dayAdd = dayAdd;
    }

    public Integer getWeekAdd() {
        return weekAdd;
    }

    public void setWeekAdd(Integer weekAdd) {
        this.weekAdd = weekAdd;
    }

    public Integer getMonthAdd() {
        return monthAdd;
    }

    public void setMonthAdd(Integer monthAdd) {
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
    
    