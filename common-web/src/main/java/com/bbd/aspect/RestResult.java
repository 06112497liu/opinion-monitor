package com.bbd.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 接口服务结果
 *
 * @author jintao
 */
@JsonInclude(Include.NON_NULL)
public class RestResult {
    /**
     * 成功代码
     */
    public static final int CODE_SUCCESS = 200;

    /**
     * 处理完成后的消息
     */
    public String           message      = "";
    /**
     * 处理结果代码
     */
    private int             status;

    private Object          data;

    public RestResult() {
        this.status = CODE_SUCCESS;
        this.message = "ok";
    }

    /**
     * 成功的情况返回
     *
     * @param data
     */
    public RestResult(Object data) {
        this.status = CODE_SUCCESS;
        this.message = "ok";
        this.data = data;
    }

    public RestResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 失败的情况返回
     */
    public RestResult(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static RestResult ok() {
        return new RestResult();
    }

    public static RestResult ok(Object data) {
        return new RestResult(data);
    }

    public static RestResult fail(int status, String message) {
        return new RestResult(status, message);
    }

    public static RestResult fail(int status, String message, Object data) {
        return new RestResult(status, message, data);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
