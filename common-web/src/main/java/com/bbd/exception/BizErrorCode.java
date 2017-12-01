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
    EVENT_UPTO_50(1004, "事件已经达到50");
    

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
    
    