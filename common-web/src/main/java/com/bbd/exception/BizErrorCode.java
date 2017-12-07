package com.bbd.exception;

/**
 * @author Liuweibo
 * @version Id: BizErrorCode.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
public enum  BizErrorCode implements ErrorCode {

    NOTIFIER_OUT_TWENTY(1000, "通知人已达到20人"),
    NOTIFIER_SETTINGID_NOT_EXIST(1001, "通知人所属setting_id不存在"),
    OBJECT_NOT_EXIST(1002, "操作对象不存在"),
    KEY_WORD_EXIST(1003, "舆情预警关键词重复"),
    EVENT_UPTO_50(1004, "最多可同时添加跟踪监测50个事件，目前系统已添加50个事件，请删除或归档部分事件后再添加新的事件监测"),
    EVENT_NAME_EXIST(1005, "事件名称重复");
    

    private int    status;
    private String message;

    BizErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
    
    