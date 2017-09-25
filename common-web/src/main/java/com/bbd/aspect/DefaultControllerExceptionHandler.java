package com.bbd.aspect;

import com.bbd.RestResult;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller统一异常处理
 *
 * @author tjwang
 * @version $Id: DefaultControllerExceptionHandler.java, v 0.1 2017/3/21 0021 15:52 tjwang Exp $
 */
@ControllerAdvice
public class DefaultControllerExceptionHandler {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({ ApplicationException.class })
    @ResponseBody
    public RestResult businessException(ApplicationException e) {
        int errorCode = e.getErrorCode().getStatus();
        logger.info("BusinessException，errorCode=" + errorCode, e);
        String msg = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage().trim() : e.getErrorCode().getMessage();
        return RestResult.fail(errorCode, msg);
    }

    /**
     * Valid 抛出的异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ BindException.class })
    public RestResult bindException(BindException e) {
        logger.warn(e.getMessage(), e);
        List<ObjectError> errors = e.getAllErrors();
        String msg = getValidationErrorMessage(errors);
        return RestResult.fail(CommonErrorCode.PARAM_ERROR.getStatus(), msg);
    }

    /**
     * Valid RequestBody绑定抛出的异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public RestResult validateException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        BindingResult br = e.getBindingResult();
        List<ObjectError> errors = br.getAllErrors();
        String msg = getValidationErrorMessage(errors);

        return RestResult.fail(CommonErrorCode.PARAM_ERROR.getStatus(), msg);
    }

    private String getValidationErrorMessage(List<ObjectError> errors) {
        StringBuffer sb = new StringBuffer();
        for (ObjectError error : errors) {
            sb.append(error.getDefaultMessage()).append("|");
        }
        int len = sb.length();
        String msg = sb.substring(0, len - 1);

        return msg;
    }

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public RestResult allException(Exception e) {
        logger.error("系统异常", e);
        return RestResult.fail(CommonErrorCode.SYS_ERROR.getStatus(), "内部错误，请联系管理员。");
    }

}
