package com.bbd.domain;

import java.util.Date;

public class WarnNotifier {
    private Long id;

    private Long settingId;

    private String notifier;

    private Integer EmailNotify;

    private String email;

    private Integer SmsNotify;

    private String phone;

    private Long createBy;

    private Date gmtCreate;

    private Long modifiedBy;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }

    public Integer getEmailNotify() {
        return EmailNotify;
    }

    public void setEmailNotify(Integer EmailNotify) {
        this.EmailNotify = EmailNotify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSmsNotify() {
        return SmsNotify;
    }

    public void setSmsNotify(Integer SmsNotify) {
        this.SmsNotify = SmsNotify;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}