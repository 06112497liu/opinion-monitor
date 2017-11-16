/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

/**
 *
 * @author tjwang
 * @version $Id: MsgModel.java, v 0.1 2017/11/16 0016 16:32 tjwang Exp $
 */
public class MsgModel {

    private String  username;

    private Integer score;

    private String  link;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
