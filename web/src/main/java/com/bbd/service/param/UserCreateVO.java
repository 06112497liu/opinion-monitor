/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

import com.bbd.exception.CommonErrorCode;
import com.bbd.util.ValidateUtil;

/**
 * 创建用户传参
 * @author tjwang
 * @version $Id: UserCreateVO.java, v 0.1 2017/11/15 0015 17:03 tjwang Exp $
 */
public class UserCreateVO {

    /**
     * 账户名（登录账户）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public void validate() {
        ValidateUtil.checkBlank(username, CommonErrorCode.PARAM_ERROR);
        ValidateUtil.checkBlank(password, CommonErrorCode.PARAM_ERROR);
    }

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
