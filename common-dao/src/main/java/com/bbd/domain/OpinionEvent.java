package com.bbd.domain;

import java.util.Date;

public class OpinionEvent {
    private Long id;

    private String eventName;

    private String eventGroup;

    private String monitor;

    private String region;

    private String eventLevel;

    private String merchant;

    private String brand;

    private String address;

    private String merchantTel;

    private String consumer;

    private String consumerTel;

    private Integer opinionCount;

    private Integer warnCount;

    private String fileReason;

    private Byte isDelete;

    private Long createBy;

    private Date gmtCreate;

    private Long modifiedBy;

    private Date gmtModified;

    private String description;

    private String includeWords;

    private String keywords;

    private String excludeWords;

    private String words;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(String eventGroup) {
        this.eventGroup = eventGroup;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMerchantTel() {
        return merchantTel;
    }

    public void setMerchantTel(String merchantTel) {
        this.merchantTel = merchantTel;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getConsumerTel() {
        return consumerTel;
    }

    public void setConsumerTel(String consumerTel) {
        this.consumerTel = consumerTel;
    }

    public Integer getOpinionCount() {
        return opinionCount;
    }

    public void setOpinionCount(Integer opinionCount) {
        this.opinionCount = opinionCount;
    }

    public Integer getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public String getFileReason() {
        return fileReason;
    }

    public void setFileReason(String fileReason) {
        this.fileReason = fileReason;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncludeWords() {
        return includeWords;
    }

    public void setIncludeWords(String includeWords) {
        this.includeWords = includeWords;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getExcludeWords() {
        return excludeWords;
    }

    public void setExcludeWords(String excludeWords) {
        this.excludeWords = excludeWords;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}