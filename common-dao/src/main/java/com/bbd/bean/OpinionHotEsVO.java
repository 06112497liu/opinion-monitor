/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.bean;

/**
 * 舆情热度记录
 * 
 * @author tjwang
 * @version $Id: OpinionHotEsVO.java, v 0.1 2017/11/9 0009 13:47 tjwang Exp $
 */
public class OpinionHotEsVO implements EsBase {

    private String  uuid;

    private Integer hot;

    @Override
    public String getEsId() {
        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }
}
