package com.bbd.service.utils;

import com.bbd.domain.Account;
import com.bbd.domain.User;
import com.bbd.util.StringUtils;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
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

    public static DateTime getDateTimeWithStartTime(Integer timeSpan) {
        DateTime now = DateTime.now();
        DateTime result;
        switch (timeSpan) {
            case 1:
                result = now.withTimeAtStartOfDay();
                break;
            case 2:
                result = now.withDayOfWeek(1).withTimeAtStartOfDay();
                break;
            case 3:
                result = now.withDayOfMonth(1).withTimeAtStartOfDay();
                break;
            case 4:
                result = now.withDayOfYear(1).withTimeAtStartOfDay();
                break;
            default:
                result = now.plusYears(-8);
                break;
        }
        return result;
    }

    public static DateHistogramInterval getDateHistogramInterval(Integer timeSpan) {
        DateHistogramInterval d = null;
        switch (timeSpan) {
            case 1:
                d = DateHistogramInterval.HOUR;
                break;
            case 2:
            case 3:
                d = DateHistogramInterval.DAY;
                break;
            case 4:
                d = DateHistogramInterval.MONTH;
                break;
            default:
                d = DateHistogramInterval.YEAR;
                break;
        }
        return d;
    }

    /**
     * 获取 姓名-部门名称-账号
     * @param account
     * @param username
     * @return
     */
    public static String getNameDepAccount(Account account, String username) {
        String str = StringUtils.generateStr("-", account.getName(), account.getDepNote(), username);
        return str;
    }


}
    
    