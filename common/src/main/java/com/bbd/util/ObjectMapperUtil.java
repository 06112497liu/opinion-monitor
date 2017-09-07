package com.bbd.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 对象和字符串相互转化
 * Created by tjwang on 2017/4/18.
 */
public class ObjectMapperUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtil.class);
    private static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    public static String write(Object o) {
        Assert.notNull(o);
        String value = "{}";
        try {
            value = mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            logger.error("序化失败", ex);
        }
        return value;
    }

    public static String writePretty(Object o) {
        Assert.notNull(o);
        String value = "{}";
        try {
            value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            logger.error("序化失败", ex);
        }
        return value;
    }

    public static <T> T read(String value, Class<T> clazz) {
        try {
            return mapper.readValue(value, clazz);
        } catch (IOException ex) {
            logger.error("反序化失败", ex);

        }
        return null;
    }

}
