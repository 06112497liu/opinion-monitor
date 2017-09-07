package com.bbd.exception;

/**
 * 运行时逻辑异常
 * Created by tjwang on 2017/2/22.
 */
public class ApplicationException extends RuntimeException {

    /**
     * 系统错误代码
     */
    private ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, Throwable ex) {
        super(errorCode.getMessage(), ex);
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, String message, Throwable ex) {
        super(message, ex);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
