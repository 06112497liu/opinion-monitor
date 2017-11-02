package com.bbd.service.impl;

import com.bbd.service.OpinionService;
import com.bbd.service.vo.*;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceImpl.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
@Service("opinionEsServiceImpl")
public class OpinionEsServiceImpl implements OpinionService {

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        return null;
    }

    @Override
    public PageList<OpinionVO> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {
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












    
    