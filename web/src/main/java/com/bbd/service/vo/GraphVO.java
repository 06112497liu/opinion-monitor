

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.service.vo; 
/** 
 * @author daijinlong 
 * @version $Id: GraphVO.java, v 0.1 2017年11月1日 上午10:24:00 daijinlong Exp $ 
 */
public class GraphVO {
    
    private Object time;
    
    private Object category;
    
    private Object value;
    
    /** * @param time
    /** * @param category
    /** * @param value */
    public GraphVO(Object time, Object category, Object value) {
        super();
        this.time = time;
        this.category = category;
        this.value = value;
    }
    
    public GraphVO() {
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

