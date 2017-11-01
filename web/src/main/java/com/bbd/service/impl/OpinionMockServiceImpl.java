package com.bbd.service.impl;

import com.bbd.service.OpinionService;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionVO;
import com.bbd.service.vo.WarnOpinionTopTenVO;
import com.google.common.collect.Lists;
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
@Service("opinionMockServiceImpl")
public class OpinionMockServiceImpl implements OpinionService {

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVO> list = Lists.newArrayList();
        for(int i=0; i<10; i++) {
            WarnOpinionTopTenVO v = new WarnOpinionTopTenVO("标题" + (i+1), (int)(Math.random()*3+1), new Date());
            list.add(v);
        }
        return list;
    }

    @Override
    public PageList<OpinionVO> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType) {
        List<OpinionVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            OpinionVO v = new OpinionVO();
            v.setUuid("111");
            v.setTitle("AAA");
            v.setSummary("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            v.setWebsite("贵阳网");
            v.setLevel(1);
            v.setStartTime("2017-05-09 11:45");
            v.setEmotion(1);
            v.setHot(95);
            list.add(v);
        }
        Paginator p = new Paginator(1, 10, 200);
        PageList<OpinionVO> pageList = PageListHelper.create(list, p);
        return pageList;
    }

    @Override
    public List<KeyValueVO> getWarnOpinionMediaTrend(Integer timeSpan, Integer emotion, Integer rank) {
        List<KeyValueVO> list = Lists.newLinkedList();
        for(int i=0; i<7; i++) {
            KeyValueVO v = new KeyValueVO(i+1, "微博", 26);
            list.add(v);
        }
        return list;
    }

    @Override
    public PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, Integer sourceType) {
        List<OpinionVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            OpinionVO v = new OpinionVO();
            v.setUuid("111");
            v.setTitle("AAA");
            v.setSummary("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            v.setWebsite("贵阳网");
            v.setLevel(1);
            v.setStartTime("2017-05-09 11:45");
            v.setEmotion(1);
            v.setHot(95);
            list.add(v);
        }
        Paginator p = new Paginator(1, 10, 200);
        PageList<OpinionVO> pageList = PageListHelper.create(list, p);
        return pageList;
    }
}












    
    