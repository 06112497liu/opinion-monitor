package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.constant.EsConstant;
import com.bbd.enums.WebsiteEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.*;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.StringUtils;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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

    /**
     * 预警舆情列表
     * @param timeSpan
     * @param emotion
     * @param sourceType
     * @return
     */
    @Override
    public Map<String, Object> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryWarningOpinion(BusinessUtils.getDateByTimeSpan(timeSpan), emotion, sourceType, pb);
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
        });

        // step-2：分页并返回结果
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        PageList p = PageListHelper.create(opinions, paginator);
        Map<String, Object> map = Maps.newHashMap();
        map.put("opinionsList", p);
        List<KeyValueVO> mediaTypeSta = esResult.getMediaTypeStats(); transMediaTypeToChina(mediaTypeSta);
        map.put("mediaTypeCount", mediaTypeSta);
        map.put("levelCount", esResult.getHotLevelStats());

        return map;
    }

    /**
     * 热点舆情top100
     * @param timeSpan
     * @param emotion
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionVO> getHotOpinionListTop100(Integer timeSpan, Integer emotion, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryTop100HotOpinion(BusinessUtils.getDateByTimeSpan(timeSpan), emotion);
        List<OpinionEsVO> esOpinons = esResult.getOpinions();

        // step-2：代码分页
        List<OpinionVO> allOpinions = BeanMapperUtil.mapList(esOpinons, OpinionVO.class);
        allOpinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot()));
        });

        int firstIndex = pb.getOffset(); int toIndex = pb.getLimit() * pb.getPage();
        if(toIndex > allOpinions.size()) {
            toIndex = allOpinions.size();
        }
        if(firstIndex > toIndex) {
            firstIndex = 0;
            pb.setPage(1);
        }
        List<OpinionVO> opinions = allOpinions.subList(firstIndex, toIndex);
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), allOpinions.size());
        PageList p = PageListHelper.create(opinions, paginator);

        return p;
    }

    /**
     * 热点舆情模糊查询
     * @param keyword
     * @return
     */
    @Override
    public PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, PageBounds pb) {
        // 保存关键词
        saveKeyword(keyword);

        DateTime startTime = BusinessUtils.getDateByTimeSpan(timeSpan);

        OpinionEsSearchVO esResult = esQueryService.getHotOpinionList(keyword, startTime, emotion, pb);
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        List<OpinionVO> list = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        PageList<OpinionVO> result = PageListHelper.create(list, paginator);
        return result;
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
        map.put("opinionsList", p);
            // 媒体类型中文描述转化
        List<KeyValueVO> mediaTypeList = esResult.getMediaTypeStats();
        transMediaTypeToChina(mediaTypeList);
        map.put("mediaTypeCount", mediaTypeList);
        map.put("levelCount", esResult.getHotLevelStats());

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
     * @return
     */
    @Override
    public List<String> getHistoryWordSearch() {
        ListOperations listOperation = redisTemplate.opsForList();
        UserInfo user = UserContext.getUser();
        if(Objects.isNull(user)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        List list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        return list;
    }

    // 保存历史搜索关键词
    private void saveKeyword(String keyword) {
        ListOperations listOperation = redisTemplate.opsForList();
        UserInfo user = UserContext.getUser();
        if(Objects.isNull(user)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        List list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        if(!StringUtils.isEmpty(keyword) && !list.contains(keyword))
            listOperation.leftPush("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), keyword);
    }

    @Override
    public List<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pb) {
        List<SimiliarNewsVO> result = esQueryService.querySimiliarNews(uuid, pb);
        return result;
    }

    /**
     * 获取舆情热度走势
     * @param uuid
     * @param timeSpan
     * @return
     */
    @Override
    public List<OpinionHotEsVO> getOpinionHotTrend(String uuid, Integer timeSpan) {
        DateTime startTime = BusinessUtils.getDateByTimeSpan(timeSpan);
        List<OpinionHotEsVO> result = esQueryService.getOpinionHotTrend(uuid, startTime);

        // 根据横坐标去重
        Set<OpinionHotEsVO> set;
        if(timeSpan == 1)
            set = new TreeSet<>(Comparator.comparing(o -> new DateTime(o.getHotTime()).toString("yyyy-MM-dd HH:00:00")));
        else
            set = new TreeSet<>(Comparator.comparing(o -> new DateTime(o.getHotTime()).toString("yyyy-MM-dd")));

        set.addAll(result);
        List<OpinionHotEsVO> list = new ArrayList<>();
        list.addAll(set);
        list.sort((o1, o2) -> -(o1.getHotTime().compareTo(o2.getHotTime())));
        return list;
    }
}












    
    