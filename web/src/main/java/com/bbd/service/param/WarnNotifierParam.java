package com.bbd.service.param;

import com.bbd.exception.CommonErrorCode;
import com.bbd.util.ValidateUtil;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @author Liuweibo
 * @version Id: WarnNotifierParam.java, v0.1 2017/11/16 Liuweibo Exp $$
 */
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
        if(emailNotify != null)
            ValidateUtil.checkNull(email, CommonErrorCode.BIZ_ERROR, "邮箱不能为空");
        if(smsNotify != null)
            ValidateUtil.checkNull(phone, CommonErrorCode.BIZ_ERROR, "电话不能为空");
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
    
    