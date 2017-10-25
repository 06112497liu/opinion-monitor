/**
 * BBD Service Inc
 * AllRight Reserved @2017
 */
package com.bbd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * json
 * 
 * @author xc
 * @version $Id: JsonUtil.java, v 0.1 2016年12月5日 下午1:23:57 xc Exp $
 */
public class JsonUtil {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String          dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
    }

    /**
     * 转字符串
     *
     * @param t
     * @return
     */
    public static <T> String fromJson(T t) {

        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
                    return new BigDecimal(value.toString());
                }

                return value;
            }
        };

        return JSON.toJSONString(t, mapping, filter);
    }

    public static <T> T parseObject(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }
}
