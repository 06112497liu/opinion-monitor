package com.bbd.param;

/**
 * @author Liuweibo
 * @version Id: NameValueInfo.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
public class NameValueInfo {

    private Integer name;

    private String nameDesc;

    private Object value;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NameValueInfo{" +
                "name=" + name +
                ", nameDesc='" + nameDesc + '\'' +
                ", value=" + value +
                '}';
    }
}
    
    