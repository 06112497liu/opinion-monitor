package com.bbd.domain;

/**
 * @author Liuweibo
 * @version Id: KeyValueVo.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public class KeyValueVO {

    private Object key;
    private String name;
    private Object value;

    public KeyValueVO() {
    }

    public KeyValueVO(Object key, String name,Object value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
    
    