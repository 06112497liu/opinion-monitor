/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 分级舆情预警内容POJO
 * @author tjwang
 * @version $Id: OpinionMsgModel.java, v 0.1 2017/11/16 0016 16:33 tjwang Exp $
 */
public class OpinionMsgModel extends MsgModel {

    @JSONField(name = "levelone")
    private Integer levelOne = 0;

    @JSONField(name = "leveltwo")
    private Integer levelTwo = 0;

    @JSONField(name = "levelthree")
    private Integer levelThree = 0;

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

    public Integer getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(Integer levelThree) {
        this.levelThree = levelThree;
    }
}
