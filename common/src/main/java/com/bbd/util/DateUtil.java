/**  
 * BBD Service Inc
 * All Rights Reserved @2016
 *
 */
package com.bbd.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类.
 * 
 * @author zhanghui
 * @version $Id: DateUtil.java, v 0.1 2016年12月14日 下午4:06:37 zhanghui Exp $
 */
public class DateUtil {

    /** 日期格式化 */
    public static final String DATE_PATTERN_1 = "yyyy年MM月dd日";
    private static      Logger logger         = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 获取今年
     * @return
     */
    public static String getYear() {
        return Calendar.getInstance().get(Calendar.YEAR) + "";
    }

    /**
     * 获取当月
     * @return
     */
    public static String getMonth() {
        return Calendar.getInstance().get(Calendar.MONDAY) + 1 + "";
    }

    /**
     * 获取日期
     * @return
     */
    public static Integer getDate() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取季度
     * @return
     */
    public static Integer getSeason() {
        Date date = new Date();
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;

    }

    /**
     * 时间格式化
     * 
     * @param pattern 格式化
     * @return 时间格式化后的字符串
     */
    public static String formatDateByPatten(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(date);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取开始时间到结束时间之间的月份间隔,不包含开始和结束月份.
     * @param start
     * @param end
     * @return
     */
    public static int getMonthBetweenNum(Date start, Date end) {
        int between = 0;
        Calendar cs = Calendar.getInstance();
        cs.setTime(start);
        Calendar ce = Calendar.getInstance();
        ce.setTime(end);
        int index = 1;
        while (cs.get(Calendar.YEAR) < ce.get(Calendar.YEAR) || (cs.get(Calendar.YEAR) == ce.get(Calendar.YEAR) && cs.get(Calendar.MONTH) < ce.get(Calendar.MONTH) - 1)) {
            cs.setTime(start);
            cs.add(Calendar.MONTH, index);
            between++;
            index++;
        }
        return between;
    }

    /**
     * 以固定的方式格式化
     */
    public static Date parseDate(String dateString, String dateFormat) {
        Date result = null;
        try {
            result = DateUtils.parseDate(dateString, dateFormat);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取上一年的年份.
     * @param endMonth 
     * @return
     */
    public static String getLastYear(Date endMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(endMonth);
        c.add(Calendar.YEAR, -1);
        return c.get(Calendar.YEAR) + "-12-31";
    }

    public static int getLastYearNum(Date endMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(endMonth);
        c.add(Calendar.YEAR, -1);
        return c.get(Calendar.YEAR);
    }

    public static int getRealYearNum(Date endMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(endMonth);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取时间里的真实月份
     * @param date
     * @return
     */
    public static int getRealMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 判断月份是否是当月.
     * @return
     */
    public static boolean isTheMonth(Date monthDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(monthDate);
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH) == c.get(Calendar.MONTH);
    }

    /***
     * 获取上个月的最后一天
     */
    public static Date getLastMonthEndDay(Date month) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(month);
        // 设置时间为改时间月份第一天
        c1.set(Calendar.DAY_OF_MONTH, 1);
        // 天数坚毅
        c1.add(Calendar.DAY_OF_YEAR, -1);
        return c1.getTime();
    }

    /**
     * 获取所有的月份包括 start和end时间里的月份
     */
    public static List<Date> getAllMonths(Date start, Date end) {
        List<Date> months = new ArrayList<Date>();
        Calendar cs = Calendar.getInstance();
        cs.setTime(start);
        Calendar s = Calendar.getInstance();
        s.clear();
        s.set(Calendar.YEAR, cs.get(Calendar.YEAR));
        s.set(Calendar.MONTH, cs.get(Calendar.MONTH));
        Calendar ce = Calendar.getInstance();
        ce.setTime(end);
        Calendar e = Calendar.getInstance();
        e.clear();
        e.set(Calendar.YEAR, ce.get(Calendar.YEAR));
        e.set(Calendar.MONTH, ce.get(Calendar.MONTH));
        while (s.getTime().before(e.getTime())) {
            months.add(s.getTime());
            s.add(Calendar.MONTH, 1);
        }
        months.add(e.getTime());
        return months;
    }

    /**
     * 比较两个时间的月份.
     * @param month1 月份1
     * @param month2 月份2
     */
    public static boolean monthEquals(Date month1, Date month2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(month1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(month2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    /**
     * 得到本月的最后一天
     */
    public static Date getMonthEndDay(Date month) {
        Calendar c = Calendar.getInstance();
        c.setTime(month);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    /**
     * 获取本月第一天
     * @param month
     * @return
     */
    public static Date getMonthStartDay(Date month) {
        Calendar c = Calendar.getInstance();
        c.setTime(month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static String strip(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return formatDateByPatten(parseDate(date, "yyyy-MM-dd"), "yyyyMMdd");
    }

    public static String split(String date, String str) {
        if (date == null) {
            return null;
        }
        if (date.indexOf(str) > 0) {
            return date.replace(str, "");
        }
        return date;
    }

    /**
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static int monthCompare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        int y1 = c1.get(Calendar.YEAR);
        int y2 = c2.get(Calendar.YEAR);
        int m1 = c1.get(Calendar.MONTH);
        int m2 = c2.get(Calendar.MONTH);
        if (y1 > y2) {
            return 1;
        } else if (y1 < y2) {
            return -1;
        } else if (m1 > m2) {
            return 1;
        } else if (m1 < m2) {
            return -1;
        } else {
            return 0;
        }
    }

    /** 去年12月31日*/
    public static String getLastYearEndDayString() {
        return getLastYear() + "年12月31日";
    }

    /**
     * 今年的6月30日
     */
    public static String getYearSixMonthEndDayString() {
        return getYear() + "年6月30日";
    }

    /**
     * 获取上个月今天
     * @return
     */
    public static Date getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 获取某月今天
     * @return
     */
    public static Date getLastMonth(Date date, Integer months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -months);
        return c.getTime();
    }

    /** 获取去年 */
    public static String getLastYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.get(Calendar.YEAR) + "";
    }

    /** 获取去年 */
    public static int getLastYear(Date date, Integer years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -years);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取上上个月的今天
     * 
     * @param date
     * @return
     */
    public static String getTheDateBeforeLastMonth(String date) {
        String resultString;
        try {
            if (StringUtils.isEmpty(date)) {
                return "";
            }
            Calendar cal = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            cal.add(Calendar.MONTH, -2);
            int day = Integer.parseInt(StringUtils.substring(date, StringUtils.lastIndexOf(date, "-") + 1, date.length()));
            cal.set(Calendar.DATE, day);
            String lastMonthStart = format.format(cal.getTime());//上月开始
            cal.clear();
            resultString = lastMonthStart;
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
        return resultString;
    }

    public static final int daysBetween(String early, String late) {
        DateFormat df = DateFormat.getDateInstance();
        Date earlydate = new Date();
        Date latedate = new Date();
        try {
            earlydate = df.parse(early);
            latedate = df.parse(late);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(earlydate);
        caled.setTime(latedate);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数   
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        if (days < 0) {
            throw new RuntimeException("时间间隔有误，请确认！");
        }
        return days;
    }

    /**
     * 去掉秒之后的信息
     * @param date
     * @return
     */
    public static Date trimMills(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 前一天
     * @param date
     * @return
     */
    public static Date lastDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTime();
    }

    /**
     * 获取某年某月最后一天
     * @param year
     * @param month
     * @return
     */

    public static Date getMonthEndDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //设置time
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return cal.getTime();
    }

    /**
     * 获取某年某月前推6个月的时间
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthStartDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 6);
        //设置time
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        return cal.getTime();
    }

    public static Date getCurrentTimeWithoutMills() {
        Date now = new Date();
        now = trimMills(now);
        return now;
    }

    public static void main(String[] args) {

        System.out.println(getMonthStartDay(2017, 5));
        System.out.println(getMonthEndDay(2017, 5));

    }
}
