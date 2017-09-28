/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.aspect;

import com.bbd.annotation.CheckAuth;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author tjwang
 * @version $Id: AuthAspect.java, v 0.1 2017/9/27 0027 14:05 tjwang Exp $
 */
@Aspect
@Component
@Order(1)
public class CheckAuthAspect {

    @Pointcut(value = "@annotation(checkAuth)", argNames = "checkAuth")
    public void checkAuthPointcut(CheckAuth checkAuth) {
    }

    @Before(value = "com.bbd.aspect.CheckAuthAspect.checkAuthPointcut(checkAuth)")
    public void before(CheckAuth checkAuth) {
        UserInfo user = UserContext.getUser();

        if (user == null) {
            throw new ApplicationException(CommonErrorCode.PERMISSION_ERROR);
        }

        if (user.getAdmin()) {
            return;
        }

        String permission = checkAuth.permission();
        if (StringUtils.isBlank(permission)) {
            throw new ApplicationException(CommonErrorCode.PERMISSION_ERROR);
        }

        if (!user.getPermissions().contains(permission)) {
            throw new ApplicationException(CommonErrorCode.PERMISSION_ERROR);
        }
    }
}
