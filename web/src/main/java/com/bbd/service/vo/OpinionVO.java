

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.service.vo; 
/** 
 * @author daijinlong 
 * @version $Id: OpinionVO.java, v 0.1 2017年10月31日 下午3:07:22 daijinlong Exp $ 
 */
public class OpinionVO {
    
    private String uuid;
    
    private String title;
    
    private String summary;
    
    private int hot;
    
    private int similiar;
    
    private int emotion;
    
    private String website;
    
    private String startTime;
    
    private int level;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getSimiliar() {
        return similiar;
    }

    public void setSimiliar(int similiar) {
        this.similiar = similiar;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
}

