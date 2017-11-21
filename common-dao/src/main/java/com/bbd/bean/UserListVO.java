package com.bbd.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: UserListVO.java, v0.1 2017/11/21 Liuweibo Exp $$
 */
public class UserListVO {

    private Long userId;

    private String region;

    private String reginDesc;

    private Boolean admin;

    private String username;

    private String name;

    private String phone;

    private String email;

    private String depNote;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtCreate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getReginDesc() {
        return reginDesc;
    }

    public void setReginDesc(String reginDesc) {
        this.reginDesc = reginDesc;
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

    public String getDepNote() {
        return depNote;
    }

    public void setDepNote(String depNote) {
        this.depNote = depNote;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
    
    