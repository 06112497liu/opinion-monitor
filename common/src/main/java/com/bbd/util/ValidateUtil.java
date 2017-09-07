/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.util;

import com.bbd.exception.ApplicationException;
import com.bbd.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 字符串校验工具类
 *
 * @author zhanghui
 * @version $Id: StringValidator.java, v 0.1 2016年12月7日 下午6:04:40 zhanghui Exp $
 */
public class ValidateUtil {

    /**
     * 手机号码的校验规则
     */
    public static final String MOBILE_PATTERN   = "^[1][3-8][0-9]{9}$";

    /**
     * 密码校验规则
     */
    //public static final String PASSWORD_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$";
    public static final String PASSWORD_PATTERN = "^((?=.*[0-9].*)(?=.*[A-Za-z].*))[0-9A-Za-z]{6,20}$";

    /**
     * 用户名校验规则
     */
    public static final String USERNAME_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])[0-9a-zA-Z]{6,20}$";

    /**
     * 检查对象是否为为空.
     *
     * @param source 源对象
     */
    public static void checkNull(Object source, ErrorCode errorCode) {
        if (Objects.isNull(source)) {
            throw new ApplicationException(errorCode);
        }
    }

    /**
     * 检查对象是否为为空.
     *
     * @param source 源对象
     */
    public static void checkNull(Object source, ErrorCode errorCode, String message) {
        if (Objects.isNull(source)) {
            throw new ApplicationException(errorCode, message);
        }
    }

    /**
     * 校验字符串是否为空
     *
     * @param source 源字符串
     */
    public static void checkBlank(String source, ErrorCode errorCode) {
        if (StringUtils.isBlank(source)) {
            throw new ApplicationException(errorCode);
        }
    }

    /**
     * 校验字符串是否为空
     *
     * @param source 源字符串
     */
    public static void checkBlank(String source, ErrorCode errorCode, String message) {
        if (StringUtils.isBlank(source)) {
            throw new ApplicationException(errorCode, message);
        }
    }

    /**
     * 校验手机号码
     *
     * @param source 源字符串
     */
    public static void checkMobile(String source, ErrorCode errorCode) {
        if (!match(source, MOBILE_PATTERN)) {
            throw new ApplicationException(errorCode);
        }
    }

    /**
     * 密码校验.
     *
     * @param source 密码
     */
    public static void checkPassword(String source, ErrorCode errorCode) {
        if (!match(source, PASSWORD_PATTERN)) {
            throw new ApplicationException(errorCode);
        }
    }

    public static void checkUsername(String source, ErrorCode errorCode) {
        if (!match(source, USERNAME_PATTERN)) {
            throw new ApplicationException(errorCode);
        }
    }

    /**
     * 校验条件是否正确
     *
     * @param condition
     * @param errorCode
     */
    public static void checkCondition(boolean condition, ErrorCode errorCode, String message) {
        if (!condition) {
            throw new ApplicationException(errorCode, message);
        }
    }

    private static boolean match(String source, String pattern) {
        return Pattern.compile(pattern).matcher(source).matches();
    }

    /**
     * 检查列表是否为空.
     */
    public static void checkListEmpty(List<?> list, ErrorCode errorCode) {
        if (list == null || list.size() == 0) {
            throw new ApplicationException(errorCode);
        }
    }

    /**
     * 手机号验证
     *
     * @param phone
     */
    public static boolean checkMobile(String phone) {
        if (!match(phone, MOBILE_PATTERN)) {
            return false;
        }
        return true;
    }

    /**
     * 判断两个字符串是否不相等
     * 相等 抛出异常
     */
    public static void checkNotEquals(String s1, String s2, ErrorCode errorCode) {
        if (StringUtils.equals(s1, s2)) {
            throw new ApplicationException(errorCode);
        }
    }

}
