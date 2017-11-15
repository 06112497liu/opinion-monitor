/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 创建用户前端参数
 * @author tjwang
 * @version $Id: UserCreateParam.java, v 0.1 2017/11/15 0015 17:12 tjwang Exp $
 */
@ApiModel(value = "user", description = "用户创建参数")
public class UserCreateParam implements Serializable {

    /**
     * 账户名（登录账户）
     */
    @ApiModelProperty(name = "username", value = "用户名（用于登录）", required = true)
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String name;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 区域
     */
    private String region;

    /**
     * 部门备注
     */
    private String depNote;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDepNote() {
        return depNote;
    }

    public void setDepNote(String depNote) {
        this.depNote = depNote;
    }

}
