/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

import com.bbd.exception.CommonErrorCode;
import com.bbd.util.ValidateUtil;

/**
 * 创建账户传参
 * @author tjwang
 * @version $Id: UserCreateVO.java, v 0.1 2017/11/15 0015 17:03 tjwang Exp $
 */
public class AccountCreateVO {

    /**
     * 用户ID
     */
    private Long   userId;

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

    /**
     * 校验
     */
    public void validate() {
        ValidateUtil.checkNull(userId, CommonErrorCode.PARAM_ERROR);
        ValidateUtil.checkBlank(region, CommonErrorCode.PARAM_ERROR);
        ValidateUtil.checkBlank(name, CommonErrorCode.PARAM_ERROR);
        ValidateUtil.checkBlank(phone, CommonErrorCode.PARAM_ERROR);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
