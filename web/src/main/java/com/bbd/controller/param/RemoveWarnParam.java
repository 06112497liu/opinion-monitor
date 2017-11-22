package com.bbd.controller.param;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Liuweibo
 * @version Id: RemoveWarnParam.java, v0.1 2017/11/22 Liuweibo Exp $$
 */
public class RemoveWarnParam {

    @NotBlank(message = "uuid不能为空")
    private String uuid;

    @NotNull(message = "解除原因不能为空")
    private Integer removeReason;

    private String removeNote;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(Integer removeReason) {
        this.removeReason = removeReason;
    }

    public String getRemoveNote() {
        return removeNote;
    }

    public void setRemoveNote(String removeNote) {
        this.removeNote = removeNote;
    }
}
    
    