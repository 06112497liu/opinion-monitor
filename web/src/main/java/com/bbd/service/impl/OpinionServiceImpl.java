package com.bbd.service.impl;

import com.bbd.enums.WebsiteEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.BizErrorCode;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.RedisCacheUtil;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Resource
    public RedisTemplate redisTemplate;

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVO> result = Lists.newLinkedList();
        List<OpinionEsVO> esList = esQueryService.getWarnOpinionTopTen();
        esList.forEach(o -> {
            WarnOpinionTopTenVO v = new WarnOpinionTopTenVO();
            v.setTime(o.getPublishTime());
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
        List<KeyValueVO> mediaTypeSta = esResult.getMediaTypeStats(); transMediaTypeToChina(mediaTypeSta);
        map.put("mediaType", mediaTypeSta);
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
        for(KeyValueVO v : list) {
            v.setName( WebsiteEnum.getDescByCode( v.getKey().toString() ) );
        }
    }

    @Override
    public Map<String, Object> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer mediaType, PageBounds pb) {
        DateTime start = new DateTime(startTime);
        DateTime endTemp = new DateTime(endTime);
        int year = endTemp.getYear(); int month = endTemp.getMonthOfYear(); int day = endTemp.getDayOfMonth();
        DateTime end = new DateTime(year, month, day, 23, 59, 59);

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryHistoryOpinions(start, end, emotion, mediaType, pb);
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

    /**
     * 舆情详情
     * @param uuid
     * @return
     */
    @Override
    public OpinionExtVO getOpinionDetail(String uuid) {
        OpinionEsVO o = esQueryService.getOpinionByUUID(uuid);
        OpinionExtVO result = BeanMapperUtil.map(o, OpinionExtVO.class);
        // 判断预警级别
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot());
        result.setLevel(level);
        return result;
    }

    /**
     * 历史关键词搜索查询
     * @param keyword
     * @return
     */
    @Override
    public List<String> getHistoryWordSearch(String keyword) {
        ListOperations listOperation = redisTemplate.opsForList();
        UserInfo user = UserContext.getUser();
        if(Objects.isNull(user)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        List list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        if(!list.contains(keyword))
            listOperation.leftPush("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), keyword);
        list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        return list;
    }

    @Override
    public PageList<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pageBounds) {
        return null;
    }
}












    
    