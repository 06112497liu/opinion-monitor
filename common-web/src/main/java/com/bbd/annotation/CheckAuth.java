/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author tjwang
 * @version $Id: CheckAuth.java, v 0.1 2017/9/27 0027 13:44 tjwang Exp $
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuth {

    String permission() default "";

}
