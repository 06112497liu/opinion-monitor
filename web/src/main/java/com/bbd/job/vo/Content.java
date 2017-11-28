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
public class Content implements Serializable{

    private MsgModel     model;

    public MsgModel getModel() {
        return model;
    }

    public void setModel(MsgModel model) {
        this.model = model;
    }


}
