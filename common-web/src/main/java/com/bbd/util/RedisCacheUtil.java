/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author tjwang
 * @version $Id: RedisCacheUtil.java, v 0.1 2017/6/1 0001 11:08 tjwang Exp $
 */
@Service
public class RedisCacheUtil {

    private static final String charset = "utf-8";
    @Resource
    public RedisTemplate        redisTemplate;
    private Logger              logger  = LoggerFactory.getLogger(getClass());

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {

        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(String key/*,ValueOperations<String,T> operation*/) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {

                listOperation.rightPush(key, dataList.get(i));
            }
        }

        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add((T) listOperation.leftPop(key));
        }

        return dataList;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        /*T[] t = (T[]) dataSet.toArray();
             setOperation.add(t);*/

        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }

        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(String key) {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);

        Long size = operation.size();
        for (int i = 0; i < size; i++) {
            dataSet.add(operation.pop());
        }
        return dataSet;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T, V> void setCacheMap(String key, Map<T, V> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap && dataMap.size() > 0) {
            for (Map.Entry<T, V> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key/*,HashOperations<String,String,T> hashOperation*/) {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {
            for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<Integer, T> getCacheIntegerMap(String key/*,HashOperations<String,String,T> hashOperation*/) {
        Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
        /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }

    public byte[] toBytes(Object o) {
        try {
            String str = o.toString();
            if (StringUtils.isNotBlank(str)) {
                return str.getBytes(charset);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String bytesToString(byte[] b) {
        try {
            return new String(b, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
