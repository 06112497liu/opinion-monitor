/**  
 * BBD Service Inc
 * All Rights Reserved @2016
 *
 */
package com.bbd.util.bean;

/**
 * 区间查询
 * @author xc
 * @version $Id: RangeQueryBean.java, v 0.1 2016年12月27日 下午4:09:53 xc Exp $
 */
public class RangeQueryBean {

    /**
     * 参数名称
     */
    private String name;
    /**
     * 开始
     */
    private Object from;
    /**
     * 结束
     */
    private Object to;

    public RangeQueryBean() {
    }

    public RangeQueryBean(String name, Object from, Object to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "RangeQueryBean [name=" + name + ", from=" + from + ", to=" + to + "]";
    }
}
