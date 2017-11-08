/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsQueryService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.*;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.util.StringUtils;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public class EsQueryServiceImpl implements EsQueryService {

    @Autowired
    private SystemSettingService settingService;

    private final String hotField = "hot";
    private final String levelThree = "levelThree";
    private final String levelTwo = "levelTwo";
    private final String levelOne = "levelOne";
    private final String mediaTypeField = "mediaType";
    private final String publishTimeField = "publishTime";
    private final String emotionField = "emotion";
    private final String keysField = "keys";
    private final String eventsField = "events";
    private final String calcTimeField = "calcTime";
    private final String titleField = "title";
    private final String contentField = "content";
    private final String websiteField = "website";
    private final String warnTimeField = "warnTime";
    private final String hotLevelField = "hotLevel";
    private final String opStatusField = "opStatus";
    private final String opOwnerField = "opOwner";
    private final String transferTypeField = "transferType";
    private final String operatorsField = "operators";
    private final String opTimeField = "opTime";

    /**
     * 获取预警舆情top10（排除在舆情任务中的预警舆情，以及热点舆情）
     * @return
     */
    @Override
    public List<OpinionEsVO> getWarnOpinionTopTen() {
        // step-1：获取预警热度分界和时间区间
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        DateTime dateTime = DateTime.now().plusDays(-30);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(hotField).gte(threeClass));
        query.must(QueryBuilders.rangeQuery(publishTimeField).gte(dateTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.termQuery(opStatusField, 0));

        // step-3：执行查询并返回结果
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setSize(10)
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .execute().actionGet();
        SearchHits hits = resp.getHits();
        SearchHit[] items = hits.getHits();
        List<OpinionEsVO> list = Lists.newLinkedList();
        for (SearchHit s : items) {
            String source = s.getSourceAsString();
            OpinionEsVO v = JsonUtil.parseObject(source, OpinionEsVO.class);
            list.add(v);
        }
        return list;
    }

    /**
     * 获取舆情数量 - 首页
     * @param startTime
     * @return
     */
    public OpinionCountStatVO getOpinionCountStatistic(Integer state, DateTime startTime) {
        String aggName = "level_count";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3); Integer twoClss = map.get(2); Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(EsConstant.CALC_TIME_PROP).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        if(state != null) {
            if(state == 0) query.must(QueryBuilders.termQuery(opStatusField, 0));
            else query.mustNot(QueryBuilders.termQuery(opStatusField, 0));
        }

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(aggName).field(hotField).keyed(true)
                .addRange(levelThree, threeClass, twoClss - 1)
                .addRange(levelTwo, twoClss, oneClass - 1)
                .addRange(levelOne, oneClass, Integer.MAX_VALUE);

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query)
                .addAggregation(hotLevelAgg)
                .setSize(0).execute().actionGet();

        // step-3：查询并构建结果
        InternalRange agg = resp.getAggregations().get(aggName);
        List<InternalRange.Bucket> bs = agg.getBuckets();
        int total = 0;
        OpinionCountStatVO vo = new OpinionCountStatVO();
        for (InternalRange.Bucket b : bs) {
            int count = Long.valueOf(b.getDocCount()).intValue();
            switch (b.getKey()) {
                case levelOne:
                    vo.setLevelOne(count);
                    total += count;
                    break;
                case levelTwo:
                    vo.setLevelTwo(count);
                    total += count;
                    break;
                case levelThree:
                    vo.setLevelThree(count);
                    total += count;
                    break;
            }
        }
        vo.setTotal(total);
        return vo;
    }

    /**
     * 获取舆情数量折线统计图 - 首页
     * @param state
     * @param timeSpan
     * @return
     */
    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(Integer state, Integer timeSpan) {
        String aggsName = "level_aggs";
        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3); Integer twoClss = map.get(2); Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(state != null) {
            if(state == 0) query.must(QueryBuilders.termQuery(opStatusField, 0));
            else query.mustNot(QueryBuilders.termQuery(opStatusField, 0));
        }
        RangeAggregationBuilder aggregation = AggregationBuilders.range(aggsName).field(hotField).keyed(true)
                .addRange(levelThree, threeClass, twoClss - 1)
                .addRange(levelTwo, twoClss, oneClass - 1)
                .addRange(levelOne, oneClass, Integer.MAX_VALUE)
                .subAggregation(buildDateRange(timeSpan));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query)
                .addAggregation(aggregation)
                .setSize(0).execute().actionGet();

        // step-3：构建返回结果
        Map<String, List<KeyValueVO>> result = Maps.newHashMap();
        List<InternalRange.Bucket> list = ((InternalRange) (resp.getAggregations().get(aggsName))).getBuckets();
        for(InternalRange.Bucket b : list) {
            List<InternalDateRange.Bucket> dateList = ((InternalDateRange) (b.getAggregations().get("calc_aggs"))).getBuckets();
            List<KeyValueVO> ls = Lists.newLinkedList();
            for(InternalDateRange.Bucket d : dateList) {
                KeyValueVO v = new KeyValueVO();
                v.setKey(d.getKey()); v.setValue(d.getDocCount());
                ls.add(v);
            }
            result.put(b.getKey(), ls);
        }
        return result;
    }

    // 创建dateRange
    private DateRangeAggregationBuilder buildDateRange(Integer timeSpan) {
        String aggsName = "calc_aggs";

        // step-1：组装条件
        DateTime now = DateTime.now();
        DateTime startTime = null;
        DateRangeAggregationBuilder dateRange = AggregationBuilders.dateRange(aggsName).field(calcTimeField).keyed(true);
        if(timeSpan == 1) {
            startTime = now.withTimeAtStartOfDay();
            dateRange.format("yyyy-MM-dd HH");
            int currentHour = now.getHourOfDay();
            int startHour = startTime.getHourOfDay();
            int between = currentHour - startHour;
            for (int i=between; i>=0; i--) {
                String from = "now-" + i + "H/H";
                String to = "now-" + (i-1) + "H/H";
                if(i==0) to = "now+" + 1 + "H/H";
                dateRange.addRange(between-i+1 + ":00:00", from, to);
            }
        } else if(timeSpan == 2) {
            startTime = now.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
            dateRange.format("yyyy-MM-dd");
            int currentDay = now.getDayOfWeek();
            int startDay = startTime.getDayOfWeek();
            int between = currentDay - startDay;
            for (int i=between; i>=0; i--) {
                String from = "now-" + i + "d/d";
                String to = "now-" + (i-1) + "d/d";
                if(i==0) to = "now+" + 1 + "d/d";
                dateRange.addRange("周" + (between-i+1), from, to);
            }
        } else if(timeSpan == 3) {
            startTime = now.withDayOfMonth(1).withTimeAtStartOfDay();
            dateRange.format("yyyy-MM-dd");
            int currentMonth = now.getMonthOfYear();
            int currentDay = now.getDayOfMonth();
            int startDay = startTime.getDayOfMonth();
            int between = currentDay - startDay;
            for (int i=between; i>=0; i--) {
                String from = "now-" + i + "d/d";
                String to = "now-" + (i-1) + "d/d";
                if(i==0) to = "now+" + 1 + "d/d";
                dateRange.addRange(currentMonth + "月" + (between-i+1) + "日", from, to);
            }
        } else if(timeSpan == 4) {
            startTime = now.withDayOfYear(1).withTimeAtStartOfDay();
            dateRange.format("yyyy-MM");
            int currentMonth = now.getMonthOfYear();
            int startMonth = startTime.getMonthOfYear();
            int between = currentMonth - startMonth;
            for (int i=between; i>=0; i--) {
                String from = "now-" + i + "M/M";
                String to = "now-" + (i-1) + "M/M";
                if(i==0) to = "now+" + 1 + "M/M";
                dateRange.addRange((between-i+1) + "月", from, to);
            }
        } else {
            startTime = now.plusYears(-4);
            dateRange.format("yyyy");
            int currentYear = now.getYear();
            int startYear = startTime.getYear();
            int between = currentYear - startYear;
            for (int i=between; i>=0; i--) {
                String from = "now-" + i + "y/y";
                String to = "now-" + (i-1) + "y/y";
                if(i==0) to = "now+" + 1 + "y/y";
                dateRange.addRange(now.plusYears(-i).getYear() + "年", from, to);
            }

        }
        return dateRange;
    }

    /**
     * 关键词排行TOP10 - 首页
     * @return
     */
    public List<KeyValueVO> getKeywordsTopTen() {
        String aggName = "top_kws";
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(QueryBuilders.rangeQuery(calcTimeField).gte(DateTime.now().plusMonths(-1).toString("yyyy-MM-dd HH:mm:ss")))
                .addAggregation(AggregationBuilders.terms(aggName).field(keysField).size(10))
                .setSize(0).execute().actionGet();
        return buildStringTermLists(resp, aggName);
    }

    /**
     * 舆情数据库近12个月累计增量
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionHisotryCountSta() {
        return null;
    }

    /**
     * 获取舆情统计数据（24小时新增，7天新增，30天新增，历史总量）
     * @return
     */
    @Override
    public DBStaVO getOpinionDBSta() throws NoSuchFieldException, IllegalAccessException {
        String aggName = "calc_aggs";
        DateTime now = DateTime.now();
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(
                        AggregationBuilders.dateRange(aggName).field(calcTimeField).keyed(true)
                                .addUnboundedFrom("dayAdd", now.plusHours(-24))
                                .addUnboundedFrom("weekAdd", now.plusDays(-7))
                                .addUnboundedFrom("monthAdd", now.plusDays(-30))
                                .addUnboundedFrom("historyTotal", now.plusYears(-10))
                )
                .setSize(0).execute().actionGet();
        List<InternalRange.Bucket> agg = ((InternalRange) resp.getAggregations().get(aggName)).getBuckets();
        DBStaVO v = new DBStaVO();
        for (InternalRange.Bucket b : agg) {
            String key = b.getKey();
            long value = b.getDocCount();
            Field s = v.getClass().getDeclaredField(key);
            s.setAccessible(true);
            s.set(v, value);
        }
        return v;
    }

    /**
     * 舆情传播渠道分布 - 首页
     * @return
     */
    public List<KeyValueVO> getOpinionMediaSpread() {
        String aggName = "media_aggs";
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(AggregationBuilders.terms(aggName).field(mediaTypeField).size(10))
                .setSize(0).execute().actionGet();
        return buildLongTermLists(resp, aggName);
    }

    /**
     * 查询预警舆情
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param pb: 分页
     * @return
     */
    public OpinionEsSearchVO queryWarningOpinion(DateTime startTime, Integer emotion, Integer mediaType, PageBounds pb) {
        String hotLevelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3); Integer twoClss = map.get(2); Integer oneClass = map.get(1);

        // step-2：构建es查询条件（这里差一个条件：舆情未存在舆情任务流程中）
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).gte(threeClass));
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));
        if (mediaType != null) query.must(QueryBuilders.termQuery(mediaTypeField, mediaType));

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(hotField).keyed(true)
                .addRange("levelOne", oneClass, Integer.MAX_VALUE).addRange("levelTwo", twoClss, oneClass-1).addRange("levelThree", threeClass, twoClss-1);
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(mediaTypeField);

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .addAggregation(hotLevelAgg)
                .addAggregation(mediaAgg);
        if (mediaType != null) builder.setPostFilter(QueryBuilders.termQuery(mediaTypeField, mediaType));

        //step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        SearchHits hits = resp.getHits();
        result.setTotal(hits.getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        result.setOpinions(opList);

        List<KeyValueVO> hotLevelList = buildHotLevelLists(resp, hotLevelAggName);
        List<KeyValueVO> mediaList = buildLongTermLists(resp, mediaAggName);
        result.setHotLevelStats(hotLevelList);
        result.setMediaTypeStats(mediaList);

        return result;
    }

    @Override
    public OpinionEsSearchVO queryEventOpinions(Long eventId, DateTime startTime, Integer emotion, Integer mediaType, PageBounds pb) {
        String hotLevelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3); Integer twoClss = map.get(2); Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).gte(threeClass));
        query.must(QueryBuilders.termQuery(eventsField, eventId));
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));
        if (mediaType != null) query.must(QueryBuilders.termQuery(mediaTypeField, mediaType));

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(hotField).keyed(true)
                .addRange("levelOne", oneClass, Integer.MAX_VALUE).addRange("levelTwo", twoClss, oneClass-1).addRange("levelThree", threeClass, twoClss-1);
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(mediaTypeField);

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .addAggregation(hotLevelAgg)
                .addAggregation(mediaAgg);
        if (mediaType != null) builder.setPostFilter(QueryBuilders.termQuery(mediaTypeField, mediaType));

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        result.setOpinions(opList);


        List<KeyValueVO> mediaList = buildLongTermLists(resp, mediaAggName);
        result.setMediaTypeStats(mediaList);

        return result;
    }

    /**
     * 查询舆情事件走势
     * @param eventId: 事件ID
     * @param startTime: 开始时间
     * @param pb: 分页
     * @return
     */
    @Override
    public OpinionEsSearchVO queryEventTrendOpinions(Long eventId, DateTime startTime, DateTime endTime, PageBounds pb) {
        // step-1：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        if (endTime != null) {
            query.must(QueryBuilders.rangeQuery(publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        query.must(QueryBuilders.termQuery(eventsField, eventId));
     
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(publishTimeField).order(SortOrder.ASC));

        // step-2：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        result.setOpinions(opList);

        return result;
    }

    @Override
    public OpinionEsSearchVO queryHistoryOpinions(DateTime startTime, DateTime endTime, Integer emotion, Integer mediaType, PageBounds pb) {
        String hotLevelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";

        // step-1：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(warnTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotLevelField).gte(3)); // 预警等级必须达到3
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));
        if (mediaType != null) query.must(QueryBuilders.termQuery(mediaTypeField, mediaType));

        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(mediaTypeField);
        TermsAggregationBuilder hotLevelAgg = AggregationBuilders.terms(hotLevelAggName).field(hotLevelField);

        // step-2：查询es
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .addAggregation(hotLevelAgg)
                .addAggregation(mediaAgg);
        SearchResponse resp = builder.execute().actionGet();

        // step-3：构建返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        result.setOpinions(opList);

        List<KeyValueVO> mediaList = buildLongTermLists(resp, mediaAggName);
        result.setMediaTypeStats(mediaList);
        return result;
    }

    /**
     * 查询热点舆情（非预警）TOP100
     * @param param: 查询字符创
     * @param startTime
     * @param emotion
     * @return
     */
    public OpinionEsSearchVO queryTop100HotOpinion(String param, DateTime startTime, Integer emotion, Integer mediaType) {

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // step-2：构建es查询条件（这里还差一个条件：添加了监测的热点舆情不显示）
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param)) query.must(QueryBuilders.multiMatchQuery(param, titleField, contentField));
        query.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).lt(threeClass));
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));
        if (mediaType != null) query.must(QueryBuilders.termQuery(mediaTypeField, mediaType));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(100)
                .setQuery(query).addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .execute().actionGet();

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        result.setOpinions(opList);

        return result;
    }


    private List<KeyValueVO> buildHotLevelLists(SearchResponse resp, String aggName) {
        List<KeyValueVO> result = Lists.newArrayList();
        InternalRange agg = resp.getAggregations().get(aggName);
        List<InternalRange.Bucket> bs = agg.getBuckets();
        for (InternalRange.Bucket b : bs) {
            int count = Long.valueOf(b.getDocCount()).intValue();
            String key = b.getKey();
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(key);
            vo.setValue(count);
            result.add(vo);
        }
        return result;
    }

    private List<KeyValueVO> buildLongTermLists(SearchResponse resp, String aggName) {
        List<KeyValueVO> result = Lists.newArrayList();

        List<LongTerms.Bucket> bs = ((LongTerms) resp.getAggregations().get(aggName)).getBuckets();
        for (LongTerms.Bucket b : bs) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(b.getKey());
            vo.setValue(b.getDocCount());
            result.add(vo);
        }

        return result;
    }

    private List<KeyValueVO> buildStringTermLists(SearchResponse resp, String aggName) {
        List<KeyValueVO> result = Lists.newArrayList();

        List<StringTerms.Bucket> bs = ((StringTerms) resp.getAggregations().get(aggName)).getBuckets();
        for (StringTerms.Bucket b : bs) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(b.getKey());
            vo.setValue(b.getDocCount());
            result.add(vo);
        }

        return result;
    }

    @Override
    public List<KeyValueVO> getEventOpinionCounts() {
        String eventsAgg = "events_agg";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(0)
                .addAggregation(AggregationBuilders.terms(eventsAgg).field(eventsField).size(100))
                .execute().actionGet();

        return buildLongTermLists(resp, eventsAgg);
    }

    @Override
    public List<KeyValueVO> getEventOpinionMediaSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "media_aggs";

        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(eventsField, eventId));
       
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(booleanQuery)
                .addAggregation(AggregationBuilders.terms(aggName).field(mediaTypeField).size(10)).setSize(0)
                .execute().actionGet();
        return buildLongTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventWebsiteSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "website_aggs";

        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(eventsField, eventId));
        
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(booleanQuery)
                .addAggregation(AggregationBuilders.terms(aggName).field(websiteField).size(8)).setSize(0)
                .execute().actionGet();
        return buildStringTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventEmotionSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "emotion_aggs";

        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(eventsField, eventId));
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(booleanQuery)
                .addAggregation(AggregationBuilders.terms(aggName).field(emotionField).size(10)).setSize(0)
                .execute().actionGet();
        return buildLongTermLists(resp, aggName);
    }

    /**
     * 根据舆情uuid查询舆情详情
     * @param uuid
     * @return
     */
    @Override
    public OpinionEsVO getOpinionByUUID(String uuid) {
        String uuidField = "uuid";
        TransportClient client = EsUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(uuidField, uuid);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .setQuery(query).setFrom(0).setSize(1)
                .execute().actionGet();

        List<OpinionEsVO> list = EsUtil.buildResult(resp, OpinionEsVO.class);
        if(list.isEmpty()) return null;
        return list.get(0);
    }

    /**
     * 当前用户待处理舆情列表
     * @param userId
     * @param transferType
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getUnProcessedList(Long userId, Integer transferType, PageBounds pb) {

        // step-1：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(opStatusField, 1));
        query.must(QueryBuilders.termQuery(opOwnerField, userId));
        if(transferType != null) {
            if(transferType <= 3) query.must(QueryBuilders.termsQuery(transferTypeField, new Integer[]{1,2,3}));
            else query.must(QueryBuilders.termsQuery(transferTypeField, new Integer[]{4,5,5}));
        }

        // step-2：查询
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(hotField, SortOrder.DESC)
                .execute().actionGet();

        // step-3：返回查询结果
        Long total = resp.getHits().getTotalHits();
        List<OpinionTaskListVO> list = EsUtil.buildResult(resp, OpinionTaskListVO.class);
            // 查询转发记录
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), total.intValue());
        PageList<OpinionTaskListVO> result = PageListHelper.create(list, paginator);
        return result;
    }

    /**
     * 当前用户转发、解除、监测列表
     * @param opStatus 1. 转发（介入）；2. 已解除； 3. 已监控
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getProcessedList(Integer opStatus, PageBounds pb) {

        // step-1：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
            // 判断当前用户是否是超级管理员(如果是管理员的话，就能看到所有的数据)
        UserInfo user = UserContext.getUser();
        if(!UserContext.isAdmin()) query.must(QueryBuilders.matchQuery(operatorsField, user.getId())); // 操作者字段必须包含当前用户
        if(opStatus != null) {
            query.must(QueryBuilders.termQuery(opStatusField, opStatus));
        }

        // step-2：查询
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addSort(hotField, SortOrder.DESC)
                .execute().actionGet();

        // step-3：返回查询结果
        Long total = resp.getHits().getTotalHits();
        List<OpinionTaskListVO> list = EsUtil.buildResult(resp, OpinionTaskListVO.class);
            // 查询转发记录
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), total.intValue());
        PageList<OpinionTaskListVO> result = PageListHelper.create(list, paginator);
        return result;
    }

    /**
     * 获取某条舆情的转发记录
     * @param keyMap
     * @param szie
     * @return
     */
    @Override
    public List<OpinionOpRecordVO> getOpinionOpRecordByUUID(Map<String, Object> keyMap, Integer szie) {
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setTypes(EsConstant.OPINION_OP_RECORD_TYPE)
                .setSearchType(SearchType.DEFAULT).setSize(szie)
                .setQuery(buildSearchRequest(keyMap))
                .addSort(opTimeField, SortOrder.DESC)
                .execute().actionGet();
        List<OpinionOpRecordVO> list = EsUtil.buildResult(resp, OpinionOpRecordVO.class);
        return list;
    }

    private BoolQueryBuilder buildSearchRequest(Map<String, Object> keyMap) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(keyMap != null) {
            for(String key : keyMap.keySet()) {
                Object value = keyMap.get(key);
                query.must(QueryBuilders.termsQuery(key, value));
            }
        }
        return query;
    }
}























