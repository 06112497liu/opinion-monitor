/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.job.vo;

import com.bbd.util.JsonUtil;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: MsgVO.java, v 0.1 2017/11/16 0016 16:26 tjwang Exp $
 */
public class SMSContent extends Content implements Serializable{

    private String templateCode;

    private String tel;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
