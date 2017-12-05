package com.bbd.service.param;

import com.bbd.exception.CommonErrorCode;
import com.bbd.util.ValidateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * @author Liuweibo
 * @version Id: WarnNotifierParam.java, v0.1 2017/11/16 Liuweibo Exp $$
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarnNotifierParam {

    /**
     * 所属配置id
     */
    private Long settingId;

    /**
     * 通知人
     */
    private String notifier;

    /**
     * 是否邮件通知（0-否，1-是）
     */
    private Integer emailNotify;

    /**
     * 邮件
     */
    private String email;

    /**
     * 是否短信通知（0-否，1-是）
     */
    private Integer smsNotify;

    /**
     * 短信
     */
    private String phone;

    // 校验参数
    public void validate() {
        ValidateUtil.checkNull(settingId, CommonErrorCode.BIZ_ERROR, "所属配置id不能为空");
        ValidateUtil.checkNull(notifier, CommonErrorCode.BIZ_ERROR, "通知人不能为空");
        if(emailNotify != null && emailNotify == 1)
            ValidateUtil.checkNull(email, CommonErrorCode.BIZ_ERROR, "邮箱不能为空");
        if(smsNotify != null && smsNotify == 1)
            ValidateUtil.checkNull(phone, CommonErrorCode.BIZ_ERROR, "电话不能为空");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarnNotifierParam that = (WarnNotifierParam) o;

        if(Objects.equals(notifier, that.notifier)) return true;
        if(Objects.equals(email, that.notifier)) return true;
        if(Objects.equals(phone, that.phone)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = settingId != null ? settingId.hashCode() : 0;
        result = 31 * result + (notifier != null ? notifier.hashCode() : 0);
        result = 31 * result + (emailNotify != null ? emailNotify.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (smsNotify != null ? smsNotify.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
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
        return emailNotify;
    }

    public void setEmailNotify(Integer emailNotify) {
        this.emailNotify = emailNotify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSmsNotify() {
        return smsNotify;
    }

    public void setSmsNotify(Integer smsNotify) {
        this.smsNotify = smsNotify;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
    
    