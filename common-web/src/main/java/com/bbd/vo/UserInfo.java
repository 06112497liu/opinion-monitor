/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.vo;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: UserInfo.java, v 0.1 2017/9/25 0025 14:32 tjwang Exp $
 */
public class UserInfo {

    /**
     * 登陆用户id
     */
    private Long id;

    /**
     * 是否是管理员
     */
    private Boolean      admin;

    /**
     * 登录名
     */
    private String       username;

    /**
     * 账户名
     */
    private String       accountName;

    private List<String> roles;

    private List<PermissionView> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<PermissionView> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionView> permissions) {
        this.permissions = permissions;
    }
}
