/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.DBStaVO;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.SystemStaVO;

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
     * @param param
     * @return
     */
    OpinionCountStatVO getOpinionCountStatistic(OpinionCountStatQueryParam param);

    /**
     * 预警舆情数量统计坐标轴
     * @param param
     * @return
     */
    Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param);

    /**
     * 舆情数据库统计坐标轴
     * @return
     */
    List<KeyValueVO> getOpinionDBCoordinate();

    /**
     * 系统运行情况统计
     * @return
     */
    SystemStaVO getSystemSta();

    /**
     * 舆情数据库统计
     * @return
     */
    DBStaVO getDBsta();

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
