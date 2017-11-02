package com.bbd.service.impl;

import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceImpl.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
@Service("opinionEsServiceImpl")
public class OpinionEsServiceImpl implements OpinionService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        return null;
    }

    @Override
    public Map<String, Object> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {
        // step-1：组装条件
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(2 == timeSpan) startTime = now.plusDays(-7);
        else if(3 == timeSpan) startTime = now.plusMonths(-1);
        else startTime = now.plusHours(-24);

        // step-2：查询es，并构建结果
        OpinionEsSearchVO esResult = esQueryService.queryWarningOpinion(startTime, emotion, pb);
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
        });

        return null;
    }

    @Override
    public List<KeyValueVO> getWarnOpinionMediaTrend(Integer timeSpan, Integer emotion, Integer rank) {
        return null;
    }

    @Override
    public PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {
        return null;
    }

    @Override
    public PageList<OpinionVO> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer sourceType, PageBounds pb) {
        return null;
    }

    @Override
    public OpinionExtVO getOpinionDetail(String uuid) {
        return null;
    }

    @Override
    public PageList<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pageBounds) {
        return null;
    }
}












    
    