package com.bbd.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: WarnOpinionTopTenVo.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public class WarnOpinionTopTenVO {

    /**
     * 舆情标题
     */
    private String  title;

    /**
     * 舆情等级
     */
    private Integer level;

    /**
     * 发生时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date    time;

    public WarnOpinionTopTenVO() {
    }

    public WarnOpinionTopTenVO(String title, Integer level, Date time) {
        this.title = title;
        this.level = level;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "WarnOpinionTopTenVo{" +
                "title='" + title + '\'' +
                ", level=" + level +
                ", time=" + time +
                '}';
    }
}
    
    