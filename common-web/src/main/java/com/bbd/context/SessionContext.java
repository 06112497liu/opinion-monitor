/**
 * BBD Service Inc
 * AllRight Reserved @2017
 */

package com.bbd.context;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * session
 *
 * @author xc
 * @version $Id: SessionContext.java, v 0.1 2016年12月5日 下午1:25:04 xc Exp $
 */
public class SessionContext {

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 设置session
     *
     * @param key
     * @param object
     */
    public static void setSession(String key, Object object) {
        getSession().setAttribute(key, object);
    }

    /**
     * 获取session的值
     *
     * @param key
     * @return
     */
    public static Object getSession(String key) {
        return getSession().getAttribute(key);
    }

    /**
     * 删除session
     *
     * @param key
     */
    public static void removeSession(String key) {
        getSession().removeAttribute(key);
    }
}
