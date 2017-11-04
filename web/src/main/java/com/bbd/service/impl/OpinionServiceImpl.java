package com.bbd.service.impl;

import com.bbd.enums.WebsiteEnum;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceImpl.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
@Service
public class OpinionServiceImpl implements OpinionService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVO> result = Lists.newLinkedList();
        List<OpinionEsVO> esList = esQueryService.getWarnOpinionTopTen();
        esList.forEach(o -> {
            WarnOpinionTopTenVO v = new WarnOpinionTopTenVO();
            v.setTime(o.getPublicTime());
            v.setHot(o.getHot());
            v.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
            v.setTitle(o.getTitle());
            result.add(v);
        });
        result.sort((x1, x2) -> ComparisonChain.start().compare(x1.getLevel(), x2.getLevel()).compare(x2.getHot(), x1.getHot()).result());
        return result;
    }

    @Override
    public Map<String, Object> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryWarningOpinion(buildTimeSpan(timeSpan), emotion, sourceType, pb);
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
        });

        // step-2：分页并返回结果
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        PageList p = PageListHelper.create(opinions, paginator);
        Map<String, Object> map = Maps.newHashMap();
        map.put("opinions", p);
        map.put("mediaType", esResult.getMediaTypeStats());
        map.put("level", esResult.getHotLevelStats());

        return map;
    }

    @Override
    public Map<String, Object> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryTop100HotOpinion(keyword, buildTimeSpan(timeSpan), emotion, sourceType);
        List<OpinionEsVO> esOpinons = esResult.getOpinions();

        // step-2：代码分页
        int index = (pb.getPage()-1) * (pb.getLimit());
        List<OpinionVO> allOpinions = BeanMapperUtil.mapList(esOpinons, OpinionVO.class);
        allOpinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
        });
        List<OpinionVO> opinions = allOpinions.subList(index, index + pb.getLimit());
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        PageList p = PageListHelper.create(opinions, paginator);

        // step-3：舆情来源和热度等级统计数据
        Map<Integer, Long> mediaMap = allOpinions.stream().collect(Collectors.groupingBy(OpinionVO::getMediaType, Collectors.counting()));
        Map<Integer, Long> levelMap = allOpinions.stream().collect(Collectors.groupingBy(OpinionVO::getLevel, Collectors.counting()));
        List<KeyValueVO> mediaTypeSta = buildKeyValueVOS(mediaMap); transMediaTypeToChina(mediaTypeSta);
        List<KeyValueVO> hotLevelSta = buildKeyValueVOS(levelMap);
        Map<String, Object> map = Maps.newHashMap();
        map.put("opinions", p);
        map.put("mediaType", mediaTypeSta);
        map.put("level", hotLevelSta);
        return map;
    }

    private DateTime buildTimeSpan(Integer timeSpan) {
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(2 == timeSpan) startTime = now.plusDays(-7);
        else if(3 == timeSpan) startTime = now.plusMonths(-1);
        else startTime = now.plusHours(-24);
        return startTime;
    }

    // map -> 构建KevyValueVO对象
    private <K, V> List<KeyValueVO> buildKeyValueVOS(Map<K, V> map) {
        List<KeyValueVO> list = Lists.newLinkedList();
        for(Map.Entry<K, V> entry : map.entrySet()) {
            K k = entry.getKey(); V v = entry.getValue();
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(k); vo.setValue(v);
            list.add(vo);
        }
        return list;
    }

    private void transMediaTypeToChina(List<KeyValueVO> list) {
        list.forEach(v -> v.setName(WebsiteEnum.getDescByCode((Integer) v.getKey())));
    }

    @Override
    public PageList<OpinionVO> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer sourceType, PageBounds pb) {
        return null;
    }

    @Override
    public OpinionExtVO getOpinionDetail(String uuid) {
        OpinionEsVO o = esQueryService.getOpinionByUUID(uuid);
        OpinionExtVO result = BeanMapperUtil.map(o, OpinionExtVO.class);
        // 判断预警级别
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot());
        result.setLevel(level);
        return result;
    }

    @Override
    public PageList<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pageBounds) {
        return null;
    }
}












    
    