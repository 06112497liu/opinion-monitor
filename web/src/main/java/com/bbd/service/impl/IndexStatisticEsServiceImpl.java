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
import org.joda.time.DateTime;
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
@Service("indexStatisticEsServiceImpl")
public class IndexStatisticEsServiceImpl implements IndexStatisticService {

    @Autowired
    private EsQueryService esQueryService;

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(Integer state, Integer timeSpan) {
        // step-1：组装条件
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(timeSpan == 2) startTime = now.plusDays(-7);
        else if(timeSpan == 3) startTime = now.plusMonths(-1);
        else startTime = now.plusDays(-1);
        // step-2：执行查询
        OpinionCountStatVO result = esQueryService.getOpinionCountStatistic(startTime);
        return result;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param) {
        return null;
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
            k.setName(WebsiteEnum.getDescByCode(Integer.parseInt(k.getKey().toString())));
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
