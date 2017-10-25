package com.bbd.bean;

/**
 * @author tjwang
 * @version $Id: IndustryAndArea.java, v 0.1 2017年6月26日 下午3:51:45 songyusheng Exp $
 */
public class DataDictionaryVO {

    private String code;

    private String name;

    public DataDictionaryVO() {
    }

    public DataDictionaryVO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
