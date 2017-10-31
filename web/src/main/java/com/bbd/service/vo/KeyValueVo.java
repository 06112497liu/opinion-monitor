package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: KeyValueVo.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public class KeyValueVO {

    private Object key;
    private Object value;

    public KeyValueVO() {
    }

    public KeyValueVO(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
    
    