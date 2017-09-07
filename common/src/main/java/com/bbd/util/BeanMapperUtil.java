/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.util;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 工具类，用于pojo转换
 *
 * @author tjwang
 * @version $Id: BeanMapperUtil.java, v 0.1 2017/2/16 0016 15:26 tjwang Exp $
 */
public class BeanMapperUtil {

    private static Logger logger = LoggerFactory.getLogger(BeanMapperUtil.class);

    public static <T> T map(Object sourceObject, Class<T> destObjectclazz) {
        if (sourceObject == null)
            return null;
        T obj = null;
        try {
            obj = destObjectclazz.newInstance();
        } catch (InstantiationException e) {
            logger.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            BeanUtils.copyProperties(obj, sourceObject);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

    public static <T, S> List<T> mapList(Collection<S> sourceList, Class<T> destObjectclazz) {
        List<T> destinationList = new ArrayList<T>();
        if (sourceList == null || sourceList.size() == 0) {
            return destinationList;
        }
        for (Iterator<S> it = sourceList.iterator(); it.hasNext();) {
            destinationList.add(map(it.next(), destObjectclazz));
        }
        return destinationList;
    }

}
