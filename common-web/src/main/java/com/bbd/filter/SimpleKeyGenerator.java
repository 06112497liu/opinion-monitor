/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.filter;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * spring cache 默认键生成器
 * @author tjwang
 * @version $Id: SimpleKeyGenerator.java, v 0.1 2017/6/2 0002 10:58 tjwang Exp $
 */
@Component("simpleKeyGenerator")
public class SimpleKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        int n = 0;
        int size = params.length;
        for (Object obj : params) {
            if (obj == null) {
                continue;
            }
            sb.append(obj.toString());
            if (++n < size) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
