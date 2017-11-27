

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.job.vo; 

import java.io.Serializable;

/** 
 * @author daijinlong 
 * @version $Id: MsgVO.java, v 0.1 2017年11月24日 上午11:27:08 daijinlong Exp $ 
 */
public class MsgVO implements Serializable{
    
    Content content;
    
    String type;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

