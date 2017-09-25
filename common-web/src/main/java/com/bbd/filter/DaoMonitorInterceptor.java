/**  
 * BBD Service Inc
 * All Rights Reserved @ 2016
 */
package com.bbd.filter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO层日志摘要拦截器
 * 
 * @author tjwang
 * @version $Id: DaoMonitorInterceptor.java, v 0.1 Jul 15, 2017 11:09:54 AM tjwang Exp $
 */
public class DaoMonitorInterceptor implements MethodInterceptor {

    /** 日志 */
    private final Logger logger = LoggerFactory.getLogger(DaoMonitorInterceptor.class);

    /** 
     * @see MethodInterceptor#invoke(MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 包名、类名、DB名、方法名
        String className = invocation.getMethod().getDeclaringClass().getSimpleName();

        // 方法名
        String method = className + "." + invocation.getMethod().getName();

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 是否有错误
        boolean hasError = false;

        try {
            return invocation.proceed();
        } catch (Throwable e) {

            // 标记异常
            hasError = true;
            throw e;

        } finally {
            if (logger.isInfoEnabled()) {
                long elapseTime = System.currentTimeMillis() - startTime;

                if (hasError) {
                    logger.info("provider," + method + ",N," + elapseTime + "ms");
                } else {
                    logger.debug("provider," + method + ",Y," + elapseTime + "ms");
                }
            }
        }
    }
}
