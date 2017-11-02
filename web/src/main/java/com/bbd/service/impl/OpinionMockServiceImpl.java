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
    public PageList<OpinionVO> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {
        List<OpinionVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            OpinionVO v = new OpinionVO();
            v.setUuid("111");
            v.setTitle("AAA");
            v.setSummary("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            v.setWebsite("贵阳网");
            v.setLevel(1);
            v.setCalcTime(new Date());
            v.setPublicTime(new Date());
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
    public PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {
        List<OpinionVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            OpinionVO v = new OpinionVO();
            v.setUuid("111");
            v.setTitle("AAA");
            v.setSummary("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            v.setWebsite("贵阳网");
            v.setLevel(1);
            v.setCalcTime(new Date());
            v.setPublicTime(new Date());
            v.setEmotion(1);
            v.setHot(95);
            list.add(v);
        }
        Paginator p = new Paginator(1, 10, 200);
        PageList<OpinionVO> pageList = PageListHelper.create(list, p);
        return pageList;
    }

    @Override
    public PageList<OpinionVO> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer sourceType, PageBounds pb) {
        List<OpinionVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            OpinionVO v = new OpinionVO();
            v.setUuid("111");
            v.setTitle("AAA");
            v.setSummary("包边报表不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不不");
            v.setWebsite("贵阳网");
            v.setLevel(1);
            v.setCalcTime(new Date());
            v.setPublicTime(new Date());
            v.setEmotion(1);
            v.setHot(95);
            list.add(v);
        }
        Paginator p = new Paginator(1, 10, 200);
        PageList<OpinionVO> pageList = PageListHelper.create(list, p);
        return pageList;
    }

    @Override
    public OpinionExtVO getOpinionDetail(String uuid) {
        OpinionExtVO v = new OpinionExtVO();
        v.setUuid("654654+4fdas8");
        v.setEmotion(0);
        v.setHot(56);
        v.setLevel(3);
        v.setCalcTime(new Date());
        v.setPublicTime(new Date());
        v.setSummary("今天凌晨，欧冠小组赛进行了第4轮的比赛，B组中，巴黎和拜仁双双赢球，两队携手提前两轮出线。D组的巴萨客场与奥林匹亚科斯打成0-0，但依然在小组中领跑。");
        v.setTitle("欧冠-内马尔破门铁卫戴帽巴黎5-0 拜仁2-1 两队携手出线 巴萨战平");
        v.setContent("北京时间11月1日凌晨3点45分，2017-18赛季欧冠联赛B组小组赛第4轮打响，法甲豪门大巴黎坐镇王子公园球场迎战比利时球队安德莱赫特。\n" +
                "第29分钟，巴黎取得领先，内马尔左路得球后回敲，姆巴佩斜塞禁区左侧，维拉蒂顺势一拉晃过防守球员，右脚兜射远角破门得分，大巴黎1-0领先！\n" +
                "上半场补时阶段，内马尔左路带球内切，在禁区弧顶附近右脚大力轰门，皮球直窜网窝，2-0！");
        v.setWebsite("微博");
        v.setSimiliar(666);
        v.setKeyword("欧冠，成都，北京");
        return v;
    }

    @Override
    public PageList<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pageBounds) {
        List<SimiliarNewsVO> list = Lists.newArrayList();
        for(int i=0; i<200; i++) {
            SimiliarNewsVO v = new SimiliarNewsVO();
            v.setTitle("欧冠-内马尔破门铁卫戴帽巴黎5-0 拜仁2-1 两队携手出线 巴萨战平");
            v.setHot((int)(Math.random()*66));
            v.setSource("论坛");
            list.add(v);
        }
        Paginator p = new Paginator(1, 10, 200);
        PageList<SimiliarNewsVO> pageList = PageListHelper.create(list, p);
        return pageList;
    }
}












    
    