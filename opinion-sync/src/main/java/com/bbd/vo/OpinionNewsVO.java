/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * ES舆情新闻
 * @author tjwang
 * @version $Id: OpinionNewsVO.java, v 0.1 2017/11/27 0027 10:41 tjwang Exp $
 */
public class OpinionNewsVO {

    @JSONField(name = "md5")
    private String  id;

    @JSONField(name = "uuid")
    private String  opinionId;

    private String  title;

    private Integer hot;

    private String website;

    private String  source;

    private String  link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(String opinionId) {
        this.opinionId = opinionId;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
