/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

import com.bbd.util.JsonUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: MsgVO.java, v 0.1 2017/11/16 0016 16:26 tjwang Exp $
 */
public class MsgVO {

    private MsgModel     model;

    private String       subject;

    private Integer      retry;

    private String       template;

    private String to;

    public static void main(String[] args) {
        MsgVO vo = new MsgVO();

        OpinionMsgModel model = new OpinionMsgModel();
        model.setUsername("xxx");
        model.setLevelOne(100);
        model.setLevelTwo(200);
        model.setLevelThree(300);
        model.setScore(99);
        model.setLink("http://www.baidu.com");

        vo.setModel(model);
        vo.setRetry(3);
        vo.setTemplate("classify_opinion_warnning");
        vo.setTo("12313@qq.com");

        System.out.println(JsonUtil.fromJson(vo));

    }

    public MsgModel getModel() {
        return model;
    }

    public void setModel(MsgModel model) {
        this.model = model;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
