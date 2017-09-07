package com.bbd.util;

import com.bbd.common.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Web请求相关的工具类
 *
 * @author jintao
 */
public class RequestUtil {

    /**
     * 检查验证码,使用传入的generatedCode和request中的验证码（参数名captcha）进行比较；
     * 不传入generatedCode时从session中取
     *
     * @return
     */
    public static boolean checkVerificationCode(String generatedCode, HttpSession session, HttpServletRequest request) {

        if (StringUtils.isBlank(generatedCode)) {
            generatedCode = (String) session.getAttribute(Constants.DEFAULT_CAPTCHA_PARAM);
        }

        // 没有生成验证码
        if (StringUtils.isBlank(generatedCode)) {
            return false;
        }

        String submitCode = request.getParameter(Constants.DEFAULT_CAPTCHA_PARAM);
        return StringUtils.isNotBlank(submitCode) && submitCode.equalsIgnoreCase(generatedCode);
    }

    public static int getParameterForInt(HttpServletRequest request, String name) {
        String stringValue = request.getParameter(name);
        return JBStringUtils.parseStringToInt(stringValue);
    }

    /**
     * 如果参数不存在或者不合法，返回默认值
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static int getParameterForInt(HttpServletRequest request, String name, int defaultValue) {
        String stringValue = request.getParameter(name);
        if (StringUtils.isBlank(stringValue)) {
            return defaultValue;
        }
        return JBStringUtils.parseStringToInt(stringValue, defaultValue);
    }

}
