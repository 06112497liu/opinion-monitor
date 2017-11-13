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
import com.bbd.service.utils.PercentUtil;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.util.PageListHelper;
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
    public OpinionCountStatVO getOpinionCountStatistic(Integer state, Integer timeSpan) {

        // step-1：组装条件
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(timeSpan == 1) startTime = now.withTimeAtStartOfDay();
        else if(timeSpan == 2) startTime = now.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
        else if(timeSpan == 3) startTime = now.withDayOfMonth(1).withTimeAtStartOfDay();
        else if(timeSpan == 4) startTime = now.withDayOfYear(1).withTimeAtStartOfDay();
        else startTime = now.plusYears(-6);

        // step-2：执行查询
        OpinionCountStatVO result = esQueryService.getOpinionCountStatistic(state, startTime);
        return result;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(Integer state, Integer timeSpan) {
        Map<String, List<KeyValueVO>> map = esQueryService.getOpinionCountStatisticGroupTime(state, timeSpan);
        List<KeyValueVO> levleOneList = map.get("levelOne");
        List<KeyValueVO> levleTwoList = map.get("levelTwo");
        List<KeyValueVO> levleThreeList = map.get("levelThree");
        List<KeyValueVO> allList = Lists.newLinkedList();
        for (int i=0; i<levleOneList.size(); i++) {
            KeyValueVO v = new KeyValueVO();
            v.setKey(levleOneList.get(i).getKey());
            v.setValue((long)(levleOneList.get(i).getValue()) + (long)(levleTwoList.get(i).getValue()) + (long)(levleThreeList.get(i).getValue()));
            allList.add(v);
        }
        map.put("all", allList);
        return map;
    }

    @Override
    public List<KeyValueVO> getOpinionDBCoordinate() {
        esQueryService.getOpinionStaLine();
        return null;
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
