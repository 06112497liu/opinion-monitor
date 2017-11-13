/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.service.vo.*;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;

import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public interface EsQueryService {

    /**
     * 获取预警舆情top10（排除在舆情任务中的预警舆情，以及热点舆情）
     * @return
     */
    List<OpinionEsVO> getWarnOpinionTopTen();

    /**
     * 获取舆情数量 - 首页
     * @param state
     * @param startTime
     * @return
     */
    OpinionCountStatVO getOpinionCountStatistic(Integer state, DateTime startTime);

    /**
     * 获取舆情数量折线统计图 - 首页
     * @param state
     * @param timeSpan
     * @return
     */
    Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(Integer state, Integer timeSpan);
    /**
     * 获取舆情数量折线统计图 - 首页
     * @param state
     * @param timeSpan
     * @return
     */
    List<KeyValueVO> getEventStatisticInfoBySourceAndCycle(Long eventId, String sourceType, String isInfo, Integer cycle);

    /**
     * 关键词排行TOP10 - 首页
     * @return
     */
    List<KeyValueVO> getKeywordsTopTen();

    /**
     * 舆情数据库近12个月累计增量
     * @return
     */
    List<KeyValueVO> getOpinionHisotryCountSta();

    /**
     * 获取舆情统计数据（24小时新增，7天新增，30天新增，历史总量）
     * @return
     */
    DBStaVO getOpinionDBSta() throws NoSuchFieldException, IllegalAccessException;

    /**
     * 舆情传播渠道分布 - 首页
     * @return
     */
    List<KeyValueVO> getOpinionMediaSpread();

    /**
     * 查询预警舆情
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param mediaType: 媒体类型
     * @param pb: 分页
     * @return
     */
    OpinionEsSearchVO queryWarningOpinion(DateTime startTime, Integer emotion, Integer mediaType, PageBounds pb);

    /**
     * 查询舆情事件
     * @param eventId: 事件ID
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param mediaType: 媒体类型
     * @param pb: 分页
     * @return
     */
    OpinionEsSearchVO queryEventOpinions(Long eventId, DateTime startTime, Integer emotion, Integer mediaType, PageBounds pb);
    /**
     * 查询舆情事件走势
     * @param eventId: 事件ID
     * @param startTime: 开始时间
     * @param pb: 分页
     * @return
     */
    OpinionEsSearchVO queryEventTrendOpinions(Long eventId, DateTime startTime, DateTime endTime, PageBounds pb);

    /**
     * 查询历史预警舆情
     * @param startTime: 开始时间
     * @param endTime：结束时间
     * @param emotion: 情感
     * @param mediaType: 媒体类型
     * @param pb: 分页
     * @return
     */
    OpinionEsSearchVO queryHistoryOpinions(DateTime startTime, DateTime endTime, Integer emotion, Integer mediaType, PageBounds pb);

    /**
     * 查询热点舆情（非预警）TOP100
     * @param startTime
     * @param emotion
     * @return
     */
    OpinionEsSearchVO queryTop100HotOpinion(DateTime startTime, Integer emotion);

    /**
     * 热点舆情模糊查询
     * @param keyword
     * @param startTime
     * @param emotion
     * @param pb
     * @return
     */
    OpinionEsSearchVO getHotOpinionList(String keyword, DateTime startTime, Integer emotion, PageBounds pb);

    /**
     * 查询事件对应舆情数量
     * @return
     */
    List<KeyValueVO> getEventOpinionCounts();

    /**
     * 舆情传播渠道分布 - 事件详情
     * @return
     */
    List<KeyValueVO> getEventOpinionMediaSpread(Long eventId, DateTime startTime, DateTime endTime);

    /**
     * 获取事件所有舆情网站来源占比 - 事件详情（媒体活跃度，媒体来源）
     * @param eventId
     * @return
     */
    List<KeyValueVO> getEventWebsiteSpread(Long eventId, DateTime startTime, DateTime endTime);

    /**
     * 获取事件所有舆情情感占比 - 事件详情
     * @param eventId
     * @return
     */
    List<KeyValueVO> getEventEmotionSpread(Long eventId, DateTime startTime, DateTime endTime);

    /**
     * 根据舆情uuid查询舆情详情
     * @param uuid
     * @return
     */
    OpinionEsVO getOpinionByUUID(String uuid);

    /**
     * 根据舆情uuid查询该条舆情的操作人
     * @param uuid
     * @return
     */
    Long[] getOperatorsByUUID(String uuid);

    /**
     * 当前用户待处理舆情列表
     * @param userId
     * @param transferType
     * @param pb
     * @return
     */
    PageList<OpinionTaskListVO> getUnProcessedList(Long userId, Integer transferType, PageBounds pb);

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb);

    /**
     * 获取某条舆情的转发记录
     * @param keyMap
     * @param size
     * @return
     */
    List<OpinionOpRecordVO> getOpinionOpRecordByUUID(Map<String, Object> keyMap, Integer size);

    /**
     * 获取舆情热度走势
     * @param uuid
     * @param dateTime
     * @return
     */
    List<OpinionHotEsVO> getOpinionHotTrend(String uuid, DateTime dateTime);

    /**
     * 获取舆情总量和预警总量
     * @return
     */
    List<KeyValueVO> getOpinionSta();
}
