/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.util;

import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;

/**
 * @author tjwang
 * @version $Id: JBStringUtils.java, v 0.1 2017/8/4 0004 17:07 tjwang Exp $
 */
public class JBStringUtils {

    public static int parseStringToInt(String stringValue) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(stringValue);
        } catch (NumberFormatException nfe) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "参数不是数字");
        }
        return intValue;
    }

    public static int parseStringToInt(String stringValue, int defaultValue) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(stringValue);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
        return intValue;
    }

}
