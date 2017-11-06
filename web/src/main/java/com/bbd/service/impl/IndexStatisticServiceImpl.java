/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.enums.WebsiteEnum;
import com.bbd.service.EsQueryService;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.utils.PercentUtil;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
        return null;
    }

    @Override
    public SystemStaVO getSystemSta() {
        return null;
    }

    @Override
    public DBStaVO getDBsta() {
        return null;
    }

    @Override
    public List<KeyValueVO> getKeywordsTopTen() {
        List<KeyValueVO> list = esQueryService.getKeywordsTopTen();
        list.sort((k1, k2) -> {
            int a = ((Long)(k1.getValue())).intValue(); int b = ((Long)(k2.getValue())).intValue();
            return Integer.compare(b, a);
        });
        return list;
    }

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
