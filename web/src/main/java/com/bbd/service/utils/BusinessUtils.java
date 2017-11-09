package com.bbd.service.utils;

import org.joda.time.DateTime;

/**
 * 业务工具
 * @author Liuweibo
 * @version Id: BusinessUtils.java, v0.1 2017/11/9 Liuweibo Exp $$
 */
public class BusinessUtils {

    /**
     * 根据时间跨度获取时间
     * @param timeSpan 1.近24小时 2.近7天 3.近30天
     * @return
     */
    public static DateTime getDateByTimeSpan(Integer timeSpan) {
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(2 == timeSpan) startTime = now.plusDays(-7).withTimeAtStartOfDay();
        else if(3 == timeSpan) startTime = now.plusDays(-30).withTimeAtStartOfDay();
        else if(1 == timeSpan)startTime = now.plusHours(-24);
        return startTime;
    }

}
    
    