/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.dao.KeywordStatisticsDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.*;
import com.bbd.enums.WebsiteEnum;
import com.bbd.service.EsQueryService;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.DBStaVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.SystemStaVO;
import com.bbd.util.BigDecimalUtil;
import com.bbd.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private KeywordStatisticsDao keywordStatisticsDao;

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
        buildPresentDateData(map, timeSpan);
        // 构建总量
        List<KeyValueVO> levelOneList = map.get("levelOne");
        List<KeyValueVO> levelTwoList = map.get("levelTwo");
        List<KeyValueVO> levelThreeList = map.get("levelThree");
        List<KeyValueVO> totalList = Lists.newLinkedList();
        for (int i=0; i<levelOneList.size(); i++) {
            KeyValueVO one = levelOneList.get(i);
            KeyValueVO two = levelTwoList.get(i);
            KeyValueVO three = levelThreeList.get(i);
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(one.getKey()); vo.setName(one.getName());
            vo.setValue(Long.parseLong(one.getValue().toString()) +
                        Long.parseLong(two.getValue().toString()) +
                        Long.parseLong(three.getValue().toString()));
            totalList.add(vo);
        }
        map.put("total", totalList);
        return map;
    }

    // 补全预警舆情数量坐标轴统计
    private void buildPresentDateData(Map<String, List<KeyValueVO>> map, Integer timeSpan) {
        Set<String> allKeys = getAllKeys(map);
        for (Map.Entry<String, List<KeyValueVO>> entry : map.entrySet()) {
            List<KeyValueVO> value = entry.getValue();
            Set<String> keys = value.stream().map(KeyValueVO::getName).collect(Collectors.toSet());
            List<KeyValueVO> noContains = BusinessUtils.buildAllVos(keys, allKeys);
            value.addAll(noContains);
            value.sort(Comparator.comparing(KeyValueVO::getName));
            formatKeys(value, timeSpan);
        }
    }

    private void formatKeys(List<KeyValueVO> list, Integer timeSpan) {
        String pattern1 = "HH时";
        String pattern2 = "dd日";
        String pattern3 = "MM月";
        String pattern4 = "yyyy年";
        for (KeyValueVO v : list) {
            Date keyDate = DateUtil.parseDate(v.getName(), "yyyy-MM-dd HH:mm:ss");
            String key;
            switch (timeSpan) {
                case 1:
                    key = DateUtil.formatDateByPatten(keyDate, pattern1);
                    break;
                case 2:
                case 3:
                    key = DateUtil.formatDateByPatten(keyDate, pattern2);
                    break;
                case 4:
                    key = DateUtil.formatDateByPatten(keyDate, pattern3);
                    break;
                default:
                    key = DateUtil.formatDateByPatten(keyDate, pattern4);
                    break;
            }
            v.setKey(key);
            v.setName(key);
        }
    }

    // 获取所有的key
    private Set<String> getAllKeys(Map<String, List<KeyValueVO>> map) {
        List<KeyValueVO> all = Lists.newLinkedList();
        for (Map.Entry<String, List<KeyValueVO>> entry : map.entrySet()) {
            List<KeyValueVO> val = entry.getValue();
            all.addAll(val);
        }
        Set<String> keySet = all.stream().map(KeyValueVO::getName).collect(Collectors.toSet());
        return keySet;
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
        KeywordStatisticsExample example = new KeywordStatisticsExample();
        example.setOrderByClause("count asc");
        List<KeywordStatistics> list = keywordStatisticsDao.selectByExample(example);
        List<KeyValueVO> rs = Lists.newArrayList();
        for(KeywordStatistics k : list) {
            KeyValueVO vo = new KeyValueVO();
            vo.setName(k.getKeyword());
            vo.setKey(k.getKeyword());
            vo.setValue(k.getCount());
            rs.add(vo);
        }
        return rs;
    }

    /**
     * 舆情传播渠道分布
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionChannelTrend() {
        List<KeyValueVO> list = esQueryService.getOpinionMediaSpread();
        if (list.isEmpty()) return Lists.newArrayList();
        calPercent(list);
        return list;
    }

    /**
     * 舆情传播渠道分布(根据预警时间)
     * @param firstWarnTime
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionChannelTrend(DateTime firstWarnTime) {
        List<KeyValueVO> list = esQueryService.getOpinionMediaSpread(firstWarnTime);
        if (list.isEmpty()) return Lists.newArrayList();
        calPercent(list);
        return list;
    }

    private void calPercent(List<KeyValueVO> list) {
        long count = list.stream().map(v -> {
            Object num = v.getValue();
            return Long.parseLong(num.toString());
        }).collect(Collectors.toList()).stream().reduce((sum, item) -> sum += item).get();

        list.forEach(k -> {
            Long num = Long.parseLong(k.getValue().toString());
            k.setName(WebsiteEnum.getDescByCode(k.getKey().toString()));
            double per = BigDecimalUtil.div(num, count, 4);
            k.setValue(BigDecimalUtil.mul(per, 100));
        });
    }
}
