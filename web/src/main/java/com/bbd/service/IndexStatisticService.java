/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.domain.KeyValueVO;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @author tjwang
 * @version $Id: IndexStatisticService.java, v 0.1 2017/10/31 0031 9:56 tjwang Exp $
 */
public interface IndexStatisticService {

    /**
     * 首页预警舆情数量统计
     * @param timeSpan
     * @return
     */
    OpinionCountStatVO getOpinionCountStatistic(Integer timeSpan) throws NoSuchFieldException, IllegalAccessException;

    /**
     * 预警舆情数量统计坐标轴
     * @param timeSpan
     * @return
     */
    Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(Integer timeSpan);

    /**
     * 舆情数据库统计坐标轴
     * @return
     */
    Map<String, List<KeyValueVO>> getOpinionDBCoordinate();

    /**
     * 系统运行情况统计
     * @return
     */
    SystemStaVO getSystemSta() throws NoSuchFieldException, IllegalAccessException;

    /**
     * 舆情数据库统计
     * @return
     */
    DBStaVO getDBsta() throws NoSuchFieldException, IllegalAccessException;

    /**
     * 本月舆情关键词top10
     * @return
     */
    List<KeyValueVO> getKeywordsTopTen();

    /**
     * 舆情传播渠道分布
     * @return
     */
    List<KeyValueVO> getEventChannelTrend();

    /**
     * 舆情事件类别分布
     * @return
     */
    List<KeyValueVO> getEventClassTrend();

    /**
     * 舆情事件地域分布
     * @return
     */
    List<KeyValueVO> getEventAreaTrend();

}
