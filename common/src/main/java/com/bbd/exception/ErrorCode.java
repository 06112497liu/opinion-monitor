package com.bbd.exception;

/**
 * 错误码，为每一种错误定义一个错误码，避免为每种错误创建异常.错误码分配如下：<br>
 * Base功能模块错误(0-999)<br>
 * Core业务层错误码(1000-1999)<br>
 *
 * @author jintao
 * @date 2016年9月13日
 */
public interface ErrorCode {

    /**
     * 状态码
     * @return
     */
    int getStatus();

    /**
     * 
     * @return
     */
    String getMessage();

}
