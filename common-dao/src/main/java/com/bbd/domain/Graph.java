

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.domain; 

import com.fasterxml.jackson.annotation.JsonFormat;

/** 
 * @author daijinlong 
 * @version $Id: GraphVO.java, v 0.1 2017年11月1日 上午10:24:00 daijinlong Exp $ 
 */
public class Graph {
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Object time;
    
    private Object category;
    
    private Object value;
    
    /** * @param time
    /** * @param category
    /** * @param value */
    public Graph(Object time, Object category, Object value) {
        super();
        this.time = time;
        this.category = category;
        this.value = value;
    }
    
    public Graph() {
        super();
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
}

