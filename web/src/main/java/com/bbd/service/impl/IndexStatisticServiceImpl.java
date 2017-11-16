/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.enums.WebsiteEnum;
import com.bbd.service.EsQueryService;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.utils.PercentUtil;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.util.PageListHelper;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tjwang
 * @version $Id: IndexStatisticEsServiceImpl.java, v 0.1 2017/10/31 0031 17:02 tjwang Exp $
 */
@Service
public class IndexStatisticServiceImpl implements IndexStatisticService {

    @Autowired
    private EsQueryService esQueryService;

    @Value("${systemRunDate}")
    private String systemRunDate;

    @Autowired
    private OpinionEventDao eventDao;

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(Integer timeSpan) throws NoSuchFieldException, IllegalAccessException {

        DateTime now = DateTime.now();
        DateTime startTime = BusinessUtils.getDateTimeWithStartTime(timeSpan);

        OpinionCountStatVO result = esQueryService.getOpinionCountStatistic(startTime, now);
        return result;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(Integer timeSpan) {
        DateTime start;
        start = BusinessUtils.getDateTimeWithStartTime(timeSpan);
        DateHistogramInterval interval = BusinessUtils.getDateHistogramInterval(timeSpan);
        Map<String, List<KeyValueVO>> map = esQueryService.getOpinionCountStatisticGroupTime(start, DateTime.now(), interval);
        return map;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionDBCoordinate() {
        Map<String, List<KeyValueVO>> map = esQueryService.getOpinionStaLine();
        return map;
    }

    @Override
    public SystemStaVO getSystemSta() throws NoSuchFieldException, IllegalAccessException {
        SystemStaVO v = new SystemStaVO();
        // step-1：系统运行时间
        DateTime runDate = new DateTime(systemRunDate);
        DateTime now = DateTime.now();
        v.setRunDays(Days.daysBetween(runDate, now).getDays());

        // step-2：舆情数据
        List<KeyValueVO> opinionNum = esQueryService.getOpinionSta();
        for (KeyValueVO vo : opinionNum) {
            Field f = v.getClass().getDeclaredField(vo.getKey().toString());
            f.setAccessible(true);
            f.set(v, vo.getValue());
        }
        
        // step-3：事件数量
        OpinionEventExample example = new OpinionEventExample();
        PageList<OpinionEvent> list = (PageList<OpinionEvent>) eventDao.selectByExampleWithPageBounds(example, new PageBounds(0, 0));
        int total = list.getPaginator().getTotalCount();
        v.setEventCount(total);
        return v;
    }

    /**
     * 舆情数据库统计
     * @return
     */
    @Override
    public DBStaVO getDBsta() throws NoSuchFieldException, IllegalAccessException {
        DBStaVO v = esQueryService.getOpinionDBSta();
        return v;
    }

    /**
     * 本月舆情关键词top10
     * @return
     */
    @Override
    public List<KeyValueVO> getKeywordsTopTen() {
        List<KeyValueVO> list = esQueryService.getKeywordsTopTen();
        list.sort((k1, k2) -> {
            long a = (long) k1.getValue(); long b = (long) k2.getValue();
            return Long.compare(b, a);
        });
        return list;
    }

    /**
     * 舆情传播渠道分布
     * @return
     */
    @Override
    public List<KeyValuePercentVO> getEventChannelTrend() {
        List<KeyValueVO> list = esQueryService.getOpinionMediaSpread();
        List<KeyValuePercentVO> rs = BeanMapperUtil.mapList(list, KeyValuePercentVO.class);
        rs.forEach(k -> {
            k.setName(WebsiteEnum.getDescByCode(k.getKey().toString()));
        });
        PercentUtil.calcLongPercents(rs);
        return rs;
    }

    @Override
    public List<KeyValueVO> getEventClassTrend() {
        return null;
    }

    @Override
    public List<KeyValueVO> getEventAreaTrend() {
        return null;
    }
}
