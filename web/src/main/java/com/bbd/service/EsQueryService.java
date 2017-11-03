/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.mybatis.domain.PageBounds;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public interface EsQueryService {

    /**
     * 获取舆情数量 - 首页
     * @param startTime
     * @return
     */
    OpinionCountStatVO getOpinionCountStatistic(DateTime startTime);

    /**
     * 关键词排行TOP10 - 首页
     * @return
     */
    List<KeyValueVO> getKeywordsTopTen();

    /**
     * 舆情传播渠道分布 - 首页
     * @return
     */
    List<KeyValueVO> getOpinionMediaSpread();

    /**
     * 查询预警舆情
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param pb: 分页
     * @return
     */
    OpinionEsSearchVO queryWarningOpinion(DateTime startTime, Integer emotion, Integer sourceType, PageBounds pb);

    /**
     * 查询热点舆情（非预警）TOP100
     * @param param: 查询字符创
     * @param startTime
     * @param emotion
     * @return
     */
    OpinionEsSearchVO queryTop100HotOpinion(String param, DateTime startTime, Integer emotion);

    /**
     * 查询事件对应舆情数量
     * @return
     */
    List<KeyValueVO> getEventOpinionCounts();

    /**
     * 舆情传播渠道分布 - 事件详情
     * @return
     */
    List<KeyValueVO> getEventOpinionMediaSpread(Long eventId);

    /**
     * 获取事件所有舆情网站来源占比 - 事件详情（媒体活跃度，媒体来源）
     * @param eventId
     * @return
     */
    List<KeyValueVO> getEventWebsiteSpread(Long eventId);

    /**
     * 获取事件所有舆情情感占比 - 事件详情
     * @param eventId
     * @return
     */
    List<KeyValueVO> getEventEmotionSpread(Long eventId);

}
