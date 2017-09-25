/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller.param;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tjwang
 * @version $Id: LoginUser.java, v 0.1 2017/9/25 0025 15:18 tjwang Exp $
 */
public class LoginUser {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

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
}
