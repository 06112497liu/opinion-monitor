package com.bbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: PopOpinionMsg.java, v0.1 2018/1/16 Liuweibo Exp $$
 */
public class PopOpinionMsg {

    private String username;

    private Integer levelOne;

    private Integer levelTwo;

    private Integer levleThree;

    private Integer hot;

    private String link;

    @JsonIgnore
    private Map<Integer, String> mapping;

    public Map<Integer, String> getMapping() {
        mapping = Maps.newHashMap();
        mapping.put(1, "levelOne");
        mapping.put(2, "levelTwo");
        mapping.put(3, "levleThree");
        return mapping;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(Integer levelOne) {
        this.levelOne = levelOne;
    }

    public Integer getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(Integer levelTwo) {
        this.levelTwo = levelTwo;
    }

    public Integer getLevleThree() {
        return levleThree;
    }

    public void setLevleThree(Integer levleThree) {
        this.levleThree = levleThree;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
    
    