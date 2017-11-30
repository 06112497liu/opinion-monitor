package com.bbd.util;


import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.elasticsearch.common.inject.internal.Join;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tjwang
 */
public class StringUtils {

    // Empty checks
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Checks if a CharSequence is empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p>
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * CharSequence. That functionality is available in isBlank().
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to
     * isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty ("") and not null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to
     * isNotEmpty(CharSequence)
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    /**
     * <p>
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 3.0 Changed signature from isBlank(String) to
     * isBlank(CharSequence)
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty (""), not null and not whitespace
     * only.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and
     * not whitespace
     * @since 3.0 Changed signature from isNotBlank(String) to
     * isNotBlank(CharSequence)
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }

    public static String trim(String value) {
        return trim(3, value, new char[] { ' ' });
    }

    public static String trim(String value, char[] chars) {
        return trim(3, value, chars);
    }

    public static String trimStart(String value, char[] chars) {
        return trim(1, value, chars);
    }

    public static String trimEnd(String value, char[] chars) {
        return trim(2, value, chars);
    }

    private static String trim(int mode, String value, char[] chars) {
        if ((value == null) || (value.length() <= 0)) {
            return value;
        }
        int startIndex = 0;
        int endIndex = value.length();
        int index = 0;
        if (mode != 1) {
            if (mode != 3)
                ;
        } else {
            while (index < endIndex) {
                if (!contains(chars, value.charAt(index++)))
                    break;
                startIndex++;
            }

        }

        if (startIndex >= endIndex) {
            return "";
        }
        if ((mode == 2) || (mode == 3)) {
            index = endIndex - 1;
            while (index >= 0) {
                if (!contains(chars, value.charAt(index--)))
                    break;
                endIndex--;
            }

        }

        if (startIndex >= endIndex)
            return "";
        if ((startIndex == 0) && (endIndex == value.length() - 1)) {
            return value;
        }
        return value.substring(startIndex, endIndex);
    }

    private static boolean contains(char[] chars, char chr) {
        if ((chars == null) || (chars.length <= 0))
            return false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == chr)
                return true;
        }
        return false;
    }

    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            if (email == null) {
                return flag;
            }

            email = email.replaceAll(" ", "");
            if ("".equals(email)) {
                return flag;
            }
            if (email.length() > 64) {
                return flag;
            }

            String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            //String reg = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            flag = match(reg, email);
        } catch (Exception e) {
            return false;
        }
        return flag;
    }

    public static boolean hasEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        String reg = "([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(email);
        return m.find(0);
    }

    public static boolean isMobile(String phone) {
        boolean flag = false;
        try {
            if (phone == null) {
                return flag;
            }

            phone = phone.replaceAll(" ", "");
            if ("".equals(phone)) {
                return flag;
            }

            //String reg = "^1[3|4|5|8]\\d{9}$";
            String reg = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
            flag = match(reg, phone);
        } catch (Exception e) {
            return false;
        }
        return flag;
    }

    public static boolean hasMobile(String mobile) {
        if (isBlank(mobile)) {
            return false;
        }
        String reg = "((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(mobile);
        return m.find(0);
    }

    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isPostalCode(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        String reg = "^[1-9]\\d{5}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(code);
        return m.matches();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据匹配的字符串match 分割原字符串，然后返回前半部分 Examples: <blockquote>
     * <p>
     * <pre>
     * substringBefore("123456789", "456") returns "123"
     * <blockquote>
     *
     * <pre>
     *
     * @return
     */
    public static String substringBefore(String value, String match) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        int index = value.indexOf(match);
        if (index > -1) {
            value = value.substring(0, index);
        }
        return value;
    }

    /**
     * 根据匹配的字符串match 分割原字符串，然后返回后半部分 <br>
     * Examples: <br>
     * substringAfter("123456789", "456") returns "789"
     */
    public static String substringAfter(String value, String match) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        int index = value.indexOf(match);
        if (index > -1) {
            value = value.substring(index + match.length());
        }
        return value;
    }

    public static int parseStringToInt(String stringValue) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(stringValue);
        } catch (NumberFormatException nfe) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR, nfe);
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

    /**
     * 生成指定长度随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将int转化为String，并指定位数，不足以0代替。
     *
     * @param num
     * @param len
     * @return
     */
    public static String getIntToStringWithLen(int num, int len) {
        String str = String.valueOf(num);
        int strLen = str.length();
        if (strLen > len) {
            return str;
        }
        String z = "0";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (len - strLen); i++) {
            sb.append(z);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 分割字符串
     *
     * @param str
     * @param sep
     * @return
     */
    public static List<String> split(String str, String sep) {
        List<String> rs = Lists.newArrayList();
        if (isBlank(str)) {
            return rs;
        }
        String[] ds = str.split(sep);
        rs = Arrays.asList(ds);
        return rs;
    }

    public static List<String> getValidEmails(String es) {
        List<String> result = Lists.newArrayList();
        if (StringUtils.isBlank(es)) {
            return result;
        }
        es = es.trim();
        List<String> esArr = StringUtils.split(es, ";");
        for (String e : esArr) {
            e = e.trim();
            if (isEmail(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public static List<String> getValidPhones(String ps) {
        List<String> result = Lists.newArrayList();
        if (StringUtils.isBlank(ps)) {
            return result;
        }
        ps = ps.trim();
        List<String> psArr = StringUtils.split(ps, ";");
        for (String p : psArr) {
            p = p.trim();
            if (isMobile(p)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * 拼凑字符串
     * @param list string集合
     * @param separator 分隔符
     * @return
     */
    public static String generateStr(String separator, String... list) {
        Joiner joiner = Joiner.on(separator).skipNulls();
        String rs = joiner.join(list);
        return rs;
    }

    /**
     * 将字符串分割成集合
     * @param text
     * @param separator
     * @return
     */
    public static List<String> generateList(String text, String separator) {
        Splitter splitter = Splitter.on(separator).omitEmptyStrings().trimResults();
        Iterable<String> it = splitter.split(text);
        List<String> rs = Lists.newArrayList(it);
        return rs;
    }
}
