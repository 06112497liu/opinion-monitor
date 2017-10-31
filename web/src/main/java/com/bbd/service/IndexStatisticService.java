/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.KeyValueVo;
import com.bbd.service.vo.OpinionCountStatVO;

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
    Map<String, List<OpinionCountStatVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param);

    /**
     * 舆情数据库统计坐标轴
     * @return
     */
    List<KeyValueVo> getOpinionDBCoordinate();

    /**
     * 系统运行情况统计
     * @return
     */
    Map<String, Object> getSystemSta();

    /**
     * 舆情数据库统计
     * @return
     */
    Map<String, Object> getDBsta();

    /**
     * 本月舆情关键词top10
     * @return
     */
    List<KeyValueVo> getKeywordsTopTen();

    /**
     * 舆情传播渠道分布
     * @return
     */
    List<KeyValueVo> getEventChannelTrend();

    /**
     * 舆情事件类别分布
     * @return
     */
    List<KeyValueVo> getEventClassTrend();

    /**
     * 舆情事件地域分布
     * @return
     */
    List<KeyValueVo> getEventAreaTrend();

}
