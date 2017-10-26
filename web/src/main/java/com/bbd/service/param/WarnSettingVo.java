package com.bbd.service.param;

import com.bbd.domain.WarnNotifier;

import java.util.List;

/**
 * @author Liuweibo
 * @version Id: WarnSettingVo.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
public class WarnSettingVo {

    private Long id;

    private Integer type;

    private Integer targetType;

    private Long eventId;

    private String name;

    private Integer level;

    private Integer popup;

    private Integer min;

    private Integer max;

    private List<WarnNotifier> notifierList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPopup() {
        return popup;
    }

    public void setPopup(Integer popup) {
        this.popup = popup;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public List<WarnNotifier> getNotifierList() {
        return notifierList;
    }

    public void setNotifierList(List<WarnNotifier> notifierList) {
        this.notifierList = notifierList;
    }

    @Override
    public String toString() {
        return "WarnSettingVo{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", popup=" + popup +
                ", min=" + min +
                ", max=" + max +
                ", notifierList=" + notifierList +
                '}';
    }
}
    
    