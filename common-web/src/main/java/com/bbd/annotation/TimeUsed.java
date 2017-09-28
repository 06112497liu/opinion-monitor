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
 * 监控时间消耗
 * @author tjwang
 * @version $Id: TimeUsed.java, v 0.1 2017/9/28 0028 14:04 tjwang Exp $
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TimeUsed {

    long threshold() default 0L;

}
