package com.bbd.service;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/5.
 */
@Component("defaultKeyGenerator")
public class DefaultKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName());
        sb.append(method.getName());
        sb.append("(");
        for (Object obj : objects) {
            sb.append(obj.toString()).append(",");
        }
        sb.append(")");
        return sb.toString();
    }

}
