/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

import java.io.Serializable;

/**
 *
 * @author tjwang
 * @version $Id: MsgVO.java, v 0.1 2017/11/16 0016 16:26 tjwang Exp $
 */
public class EmailContent extends Content implements Serializable{

    private String       subject;

    private Integer      retry;

    private String       template;

    private String to;

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
