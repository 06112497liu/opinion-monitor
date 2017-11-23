/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.param;

/**
 * 账户更新参数
 * @author tjwang
 * @version $Id: AccountUpdateVo.java, v 0.1 2017/9/27 0027 16:22 tjwang Exp $
 */
public class AccountUpdateVo {

    /**
     * 账户ID
     */
    private Long    id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 账户名
     */
    private String  name;

    /**
     * 电话
     */
    private String  phone;

    /**
     * 邮件
     */
    private String  email;

    /**
     * 区域
     */
    private String  region;

    /**
     * 部门
     */
    private String  depNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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
