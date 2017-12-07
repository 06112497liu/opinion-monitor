package com.bbd.service.param;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Liuweibo
 * @version Id: TransferParam.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public class TransferParam {

    @NotBlank(message = "uuid不能为空")
    private String uuid;

    @NotBlank(message = "区域不能为空")
    private String district;

    @NotNull(message = "转发用户id不能为空")
    private Long userId;

    @NotNull(message = "转发类型不能为空")
    private Integer transferType;

    private String transferNote;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public String getTransferNote() {
        return transferNote;
    }

    public void setTransferNote(String transferNote) {
        this.transferNote = transferNote;
    }
}
    
    