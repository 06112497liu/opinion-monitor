package com.bbd.service.param;

/**
 * @author Liuweibo
 * @version Id: TransferParam.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
public class TransferParam {

    private String uuid;

    private String district;

    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    