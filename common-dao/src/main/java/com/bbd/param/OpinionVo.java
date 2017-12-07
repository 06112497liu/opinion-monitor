package com.bbd.param;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: OpinionVo.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
public class OpinionVo {

    private Long id;

    private String uuid;

    private String title;

    private Integer hot;

    private String level;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "OpinionVo{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", hot=" + hot +
                ", level='" + level + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
    
    