/**
 * BBD Service Inc
 * AllRight Reserved @2017
 */

package com.bbd.util;

import com.bbd.context.SessionContext;
import com.bbd.vo.UserInfo;

/**
 * 用户的session
 * 
 * @author tjwang
 * @version $Id: UserContext.java, v 0.1 2017年9月15日 下午1:26:22 xc Exp $
 */
public class UserContext {

    /**
     * 用户存在session中的key
     */
    private static final String ADMIN_INFO_SESSION = "user";

    public static UserInfo getUser() {
        return (UserInfo) SessionContext.getSession(ADMIN_INFO_SESSION);
    }

    public static void setUser(UserInfo user) {
        SessionContext.setSession(ADMIN_INFO_SESSION, user);
    }

    /**
     *  <p>Date:2016年10月17日上午11:51:16;</p>
     *	<p>Description: 删除session中的用户;</p>
     */
    public static void removeUser() {
        SessionContext.removeSession(ADMIN_INFO_SESSION);
    }

    public static boolean isAdmin() {
        UserInfo user = getUser();
        return user.getAdmin();
    }
}
