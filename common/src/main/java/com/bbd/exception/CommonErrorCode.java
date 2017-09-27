package com.bbd.exception;

/**
 * 核心模块错误(0-999),具体细分子模块：<br>
 * 0-99为系统常见异常的错误码，如IllegalArgumentException<br>
 * 100-199为日期相关错误<br>
 * <p>
 * <br>
 * 900-999为action,state,audit相关的错误定义
 * <br>
 *
 * @author jintao
 */
public enum CommonErrorCode implements ErrorCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),

    /**
     * 参数异常
     */
    PARAM_ERROR(201, "参数异常"),
    /**
     * 业务异常
     */
    BIZ_ERROR(202, "业务异常"),
    /**
     * 系统异常
     */
    SYS_ERROR(101, "系统异常"),

    INNER_ERROR(102, "内部异常"),

    PERMISSION_ERROR(301, "权限不足"),

    ;

    private int    status;
    private String message;

    CommonErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
