/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.aspect;

import com.bbd.annotation.TimeUsed;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author tjwang
 * @version $Id: TimeUsedAspect.java, v 0.1 2017/9/28 0028 14:06 tjwang Exp $
 */
@Order
@Aspect
@Component
public class TimeUsedAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "@annotation(timeUsed)", argNames = "timeUsed")
    public void timeUsedPointcut(TimeUsed timeUsed) {
    }

    @Around(value = "com.bbd.aspect.TimeUsedAspect.timeUsedPointcut(timeUsed)")
    public Object around(ProceedingJoinPoint pjp, TimeUsed timeUsed) throws Throwable {
        Object result;
        long start = System.currentTimeMillis();
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long end = System.currentTimeMillis();
            String methodName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
            long used = (end - start);
            if (used >= timeUsed.threshold()) {
                logger.info(String.format("执行方法 %s 耗时: %d millis", methodName, used));
            }
        }
        return result;
    }
}
