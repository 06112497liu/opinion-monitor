/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

/**
 *
 * @author tjwang
 * @version $Id: PermissionView.java, v 0.1 2017/9/27 0027 17:04 tjwang Exp $
 */
public class PermissionView {

    private Long   id;

    private String code;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
