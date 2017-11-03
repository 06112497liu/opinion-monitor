/**  
 * BBD Service Inc
 * All Rights Reserved @2016
 *
 */
package com.bbd.service.utils;


import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.vo.KeyValuePercentVO;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

/**
 * 百分比工具类
 * 
 * @author zhanghui
 * @version $Id: PercentUtil.java, v 0.1 2016年12月5日 下午4:08:49 zhanghui Exp $
 */
public class PercentUtil {

    private static NumberFormat nf = null;

    static {
        nf = NumberFormat.getPercentInstance();

        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    /**
     * 计算参数在总数中的百分比.
     * 
     * @param total 总数
     * @param num 计算参数
     * @return 计算参数在总数中的百分比
     */
    public static String calcPercent(double total, double num) {
        double d = num / total;
        if (d > 1) {
            return "100%";
        } else if (d * 100 > 0.004) {
            return nf.format(d);
        } else {
            // 为零时 直接百分之零 奇怪的需求
            return "0%";
        }
    }

    /**
     * 计算各个数在总数中的百分比.
     * 
     * @param args 参数数组
     * @return 各个数组在总数中的百分比字符串数组
     */
    public static String[] calcPercents(double... args) {
        double total = 0.00;
        for (double i : args) {
            if (i < 0.0) {
                throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
            }
            total += i;
        }

        if (total < 0.00) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
        }
        String[] percents = new String[args.length];

        if (Math.abs(total) < 0.00001) {
            for (int i = 0; i < percents.length; i++) {
                percents[i] = "0%";
            }
            return percents;
        }

        int i = 0;
        for (double d : args) {
            percents[i] = calcPercent(total, d);
            i++;
        }
        return percents;
    }

    /**
     * 计算各个数在总数中的百分比.
     * 
     * @param args 参数数组
     * @return 各个数组在总数中的百分比字符串数组
     */
    public static String[] calcLongPercents(Long... args) {
        double total = 0.00;
        for (double i : args) {
            if (i < 0.0) {
                throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
            }
            total += i;
        }

        if (total < 0.00) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
        }
        String[] percents = new String[args.length];

        if (Math.abs(total) < 0.00d) {
            for (int i = 0; i < percents.length; i++) {
                percents[i] = "0%";
            }
            return percents;
        }

        int i = 0;
        for (double d : args) {
            percents[i] = calcPercent(total, d);
            i++;
        }
        return percents;
    }

    /**
     * 计算long型数据的百分比.
     * 
     * @return 各个数组在总数中的百分比字符串数组
     */
    public static String calcLongPercents(long total, long value) {
        if (total <= 0.0) {
            return "0%";
        }
        double d = value / Double.valueOf(total);
        if (d * 100 > 0.004) {
            return nf.format(d);
        } else {
            // 为零时 直接百分之零 奇怪的需求
            return "0%";
        }
    }

    /**
     * 计算各个数在总数中的百分比.
     * 
     * @param args 参数数组
     * @return 各个数组在总数中的百分比字符串
     */
    public static String[] calcPercents(int... args) {
        double[] d = new double[args.length];
        int j = 0;
        for (int i : args) {
            if (i < 0) {
                throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
            }
            d[j] = i * 1.0;
            j++;
        }
        return calcPercents(d);
    }

    /***
     * 计算节点的占比
     * @param infos 节点的列表
     */
    public static void calcPercents(List<KeyValuePercentVO> infos) {
        if (infos.size() > 0) {
            int[] values = new int[infos.size()];
            int i = 0;
            for (KeyValuePercentVO info : infos) {
                values[i] = (int) info.getValue();
                i++;
            }
            String[] percents = PercentUtil.calcPercents(values);
            i = 0;
            for (KeyValuePercentVO info : infos) {
                info.setPercent(percents[i]);
                i++;
            }
        }
    }

    public static void calcLongPercents(List<KeyValuePercentVO> infos) {
        if (infos.size() > 0) {
            Long[] values = new Long[infos.size()];
            int i = 0;
            for (KeyValuePercentVO info : infos) {
                values[i] = (Long) info.getValue();
                i++;
            }
            String[] percents = PercentUtil.calcLongPercents(values);
            i = 0;
            for (KeyValuePercentVO info : infos) {
                info.setPercent(percents[i]);
                i++;
            }
        }
    }

    /***
     * 计算节点的占比
     * @param infos 节点的列表
     */
    public static void calcPercents(KeyValuePercentVO[] infos) {
        if (infos.length > 0) {
            int[] values = new int[infos.length];
            int i = 0;
            for (KeyValuePercentVO info : infos) {
                values[i] = (int) info.getValue();
                i++;
            }
            String[] percents = PercentUtil.calcPercents(values);
            i = 0;
            for (KeyValuePercentVO info : infos) {
                info.setPercent(percents[i]);
                i++;
            }
        }
    }

    /**
     * 计算参数在总数中的百分比.
     *
     * @param total 总数
     * @param num 计算参数
     * @return 计算参数在总数中的百分比
     */
    public static String calcIntPercent(int total, int num) {
        if (total <= 0.0) {
            return "0%";
        }
        double d = num / Double.valueOf(total);
        if (d * 100 > 0.004) {
            return nf.format(d);
        } else {
            // 为零时 直接百分之零 奇怪的需求
            return "0%";
        }
    }
}
