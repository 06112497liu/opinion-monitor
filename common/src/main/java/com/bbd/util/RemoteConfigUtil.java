package com.bbd.util;

import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * @author Liuweibo
 * @version Id: RemoteConfigUtil.java, v0.1 2017/11/22 Liuweibo Exp $$
 */
public class RemoteConfigUtil {

    private static Properties properties = null;

    private static Logger logger = LoggerFactory.getLogger(RemoteConfigUtil.class);

    static {
        properties = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("remote.properties");
            properties.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            throw new ApplicationException(CommonErrorCode.SYS_ERROR, e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取配置属性
     * @param key
     * @param defaultVlaue
     * @return
     */
    public static String get(String key, String defaultVlaue) {
        String str = properties.getProperty(key, defaultVlaue);
        return str;
    }

    /**
     * 获取配置属性
     * @param key
     * @return
     */
    public static String get(String key) {
        String str = properties.getProperty(key);
        return str;
    }

}
    
    