/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.annotation.TimeUsed;
import com.bbd.bean.EventEsVO;
import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.constant.EsConstant;
import com.bbd.domain.KeyValueVO;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.WarnSetting;
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.param.OpinionBaseInfoReport;
import com.bbd.service.param.WarnSettingVo;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.*;
import com.bbd.util.*;
import com.bbd.vo.UserInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public class EsQueryServiceImpl implements EsQueryService {

    private final String         total      = "total";
    private final String         levelThree = "levelThree";
    private final String         levelTwo   = "levelTwo";
    private final String         levelOne   = "levelOne";
    @Autowired
    private SystemSettingService settingService;
    @Autowired
    private EventService         eventService;

    @Autowired
    private EsUtil               esUtil;

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
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(dateTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField));

        // step-3：执行查询并返回结果
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(10).setQuery(query).addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC)).execute().actionGet();
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
    @Override
    public OpinionCountStatVO getOpinionCountStatistic(DateTime startTime, DateTime endTime) throws NoSuchFieldException, IllegalAccessException {
        Preconditions.checkNotNull(startTime, "开始时间不能为空");
        Preconditions.checkNotNull(endTime, "结束时间不能为空");
        Preconditions.checkArgument(startTime.isBefore(endTime), "结束时间必须大于开始时间");

        String start = startTime.toString(EsConstant.LONG_TIME_FORMAT);
        String end = endTime.toString(EsConstant.LONG_TIME_FORMAT);

        SearchRequestBuilder builder = esUtil.getClient().prepareSearch(EsConstant.IDX_OPINION);
        final String countAggs = "count_aggs";

        FilterAggregationBuilder oneWarnAggsBuilder = AggregationBuilders.filter(levelOne, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_ONE).gte(start).lte(end));
        oneWarnAggsBuilder.subAggregation(AggregationBuilders.cardinality(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_ONE));

        FilterAggregationBuilder twoWarnAggsBuilder = AggregationBuilders.filter(levelTwo, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_TWO).gte(start).lte(end));
        twoWarnAggsBuilder.subAggregation(AggregationBuilders.cardinality(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_TWO));

        FilterAggregationBuilder threeWarnAggsBuilder = AggregationBuilders.filter(levelThree, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_THREE).gte(start).lte(end));
        threeWarnAggsBuilder.subAggregation(AggregationBuilders.cardinality(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_THREE));

        builder.setSize(0).addAggregation(oneWarnAggsBuilder).addAggregation(twoWarnAggsBuilder).addAggregation(threeWarnAggsBuilder);

        SearchResponse resp = builder.get();

        OpinionCountStatVO result = new OpinionCountStatVO();
        Map<String, Aggregation> map = resp.getAggregations().getAsMap();
        for (Map.Entry<String, Aggregation> m : map.entrySet()) {
            String k = m.getKey();
            InternalFilter v = (InternalFilter) m.getValue();
            Field f = result.getClass().getDeclaredField(k);
            f.setAccessible(true);
            long count = v.getDocCount();
            f.set(result, count);
        }
        return result;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(DateTime startTime, DateTime endTime, DateHistogramInterval interval) {
        Preconditions.checkNotNull(startTime, "开始时间不能为空");
        Preconditions.checkNotNull(endTime, "结束时间不能为空");
        Preconditions.checkNotNull(interval, "间隔类型不能为空");
        Preconditions.checkArgument(startTime.isBefore(endTime), "结束时间必须大于开始时间");

        Map<String, List<KeyValueVO>> map = Maps.newHashMap();

        String start = startTime.toString(EsConstant.LONG_TIME_FORMAT);
        String end = endTime.toString(EsConstant.LONG_TIME_FORMAT);

        SearchRequestBuilder builder = esUtil.getClient().prepareSearch(EsConstant.IDX_OPINION);

        final String countAggs = "count_aggs";

        FilterAggregationBuilder oneWarnAggsBuilder = AggregationBuilders.filter(levelOne, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_ONE).gte(start).lte(end));
        oneWarnAggsBuilder.subAggregation(AggregationBuilders.dateHistogram(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_ONE).dateHistogramInterval(interval));

        FilterAggregationBuilder twoWarnAggsBuilder = AggregationBuilders.filter(levelTwo, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_TWO).gte(start).lte(end));
        twoWarnAggsBuilder.subAggregation(AggregationBuilders.dateHistogram(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_TWO).dateHistogramInterval(interval));

        FilterAggregationBuilder threeWarnAggsBuilder = AggregationBuilders.filter(levelThree, QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_THREE).gte(start).lte(end));
        threeWarnAggsBuilder.subAggregation(AggregationBuilders.dateHistogram(countAggs).field(EsConstant.OPINION_FIRST_WARN_TIME_THREE).dateHistogramInterval(interval));

        builder.setSize(0).addAggregation(oneWarnAggsBuilder).addAggregation(twoWarnAggsBuilder).addAggregation(threeWarnAggsBuilder);

        SearchResponse resp = builder.get();

        map.put(levelOne, buildWarnAggs(levelOne, resp.getAggregations().getAsMap()));
        map.put(levelTwo, buildWarnAggs(levelTwo, resp.getAggregations().getAsMap()));
        map.put(levelThree, buildWarnAggs(levelThree, resp.getAggregations().getAsMap()));

        return map;
    }

    private List<KeyValueVO> buildWarnAggs(String aggName, Map<String, Aggregation> map) {
        List<KeyValueVO> result = Lists.newArrayList();
        List<InternalDateHistogram.Bucket> bs = ((InternalDateHistogram) ((InternalFilter) map.get(aggName)).getAggregations().getAsMap().get("count_aggs")).getBuckets();

        for (InternalDateHistogram.Bucket b : bs) {
            String key = b.getKeyAsString();
            long value = b.getDocCount();
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(key);
            vo.setName(key);
            vo.setValue(value);
            result.add(vo);
        }

        return result;
    }

    @Override
    public OpinionEsVO queryEventMaxOpinion(Long eventId) {
        // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(0).setSize(1).setQuery(query).addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC))
                .addSort(SortBuilders.fieldSort(EsConstant.publishTimeField).order(SortOrder.DESC)).addSort(SortBuilders.fieldSort(EsConstant.uuidField).order(SortOrder.DESC));

        // step-2：查询并返回结果
        SearchResponse resp = builder.execute().actionGet();
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        if (opList != null && opList.size() > 0) {
            return opList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取事件数量折线统计图 - 图表跟踪分析
     * @param eventId
     * @return
     */
    @Override
    public List<KeyValueVO> getEventMediaStatisticBySource(Long eventId, DateTime endTime) {
        String aggName = "event_media_calc_aggs";
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(query)
                .addAggregation(AggregationBuilders.terms(aggName).field(EsConstant.mediaTypeField)).setSize(0)
                .execute().actionGet();
        return buildTermLists(resp, aggName);
    }
    /**  
     * 查询事件新增达到新增设置值的舆情
     * @param opinionEvent
     * @param startTime
     * @param endTime
     * @return 
     */
    @Override
    public List<EventEsVO> queryEventNewInfoTotal(OpinionEvent opinionEvent, DateTime startTime, DateTime endTime){
        // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.eventIdField, opinionEvent.getId()));
        if (startTime != null) {
            query.must(QueryBuilders.rangeQuery(EsConstant.matchTimeField).gt(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        if (endTime != null) {
            query.must(QueryBuilders.rangeQuery(EsConstant.matchTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION_EVENT_RECORD)
                .setTypes(EsConstant.OPINION_EVENT_RECORD_HOT_TYPE)
                .setFrom(0).setSize(1).setQuery(query);
        // step-2：查询并返回结果
        SearchResponse resp = builder.execute().actionGet();
        return EsUtil.buildResult(resp, EventEsVO.class);
    }
    /**  
     * 查询事件信息总量、预警总量
     * @param opinionEvent
     * @param isWarn
     * @param cycle
     * @return 
     */
    @Override
    public List<KeyValueVO> queryEventInfoTotal(OpinionEvent opinionEvent, boolean isWarn, Integer cycle) {

        // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.eventIdField, opinionEvent.getId()));
        SearchResponse resp;
        if (isWarn == false) {
            resp = client.prepareSearch(EsConstant.IDX_OPINION_EVENT_RECORD).setTypes(EsConstant.OPINION_EVENT_RECORD_TYPE)
                    .setQuery(query).addAggregation(buildEventDayeRange(cycle, opinionEvent))
                    .setSearchType(SearchType.DEFAULT).setSize(0).execute().actionGet();
        } else {
            resp = client.prepareSearch(EsConstant.IDX_OPINION_EVENT_RECORD).setTypes(EsConstant.OPINION_EVENT_RECORD_WARN_TYPE)
                    .setQuery(query).addAggregation(buildEventDayeRange(cycle, opinionEvent))
                    .setSearchType(SearchType.DEFAULT).setSize(0).execute().actionGet();
        }
        // step-2：构建返回结果
        List<InternalDateRange.Bucket> dateList = ((InternalDateRange) (resp.getAggregations().get("event_calc_aggs"))).getBuckets();
        List<KeyValueVO> ls = Lists.newLinkedList();
        for (InternalDateRange.Bucket d : dateList) {
            KeyValueVO v = new KeyValueVO();
            v.setName(d.getKey().toString().substring(0, 16));
            v.setValue(d.getDocCount());
            ls.add(v);
        }
        return ls;
    }

    public DateRangeAggregationBuilder buildEventDayeRange(int cycle, OpinionEvent opinionEvent) {
        String aggsName = "event_calc_aggs";

        DateRangeAggregationBuilder dateRange = AggregationBuilders.dateRange(aggsName).field(EsConstant.matchTimeTrimField).keyed(true);
        List<Date> dates = eventService.getDates(cycle, opinionEvent);
        for (Date date : dates) {
            DateTime dateTime = new DateTime(date);
            dateRange.addUnboundedTo(dateTime.toString("yyyy-MM-dd HH:mm:ss"), dateTime);
        }
        return dateRange;
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
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client
                .prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(
                        AggregationBuilders.dateRange(aggName).field(EsConstant.publishTimeField).keyed(true).addUnboundedFrom("dayAdd", now.plusHours(-24))
                                .addUnboundedFrom("weekAdd", now.plusDays(-7).withTimeAtStartOfDay()).addUnboundedFrom("monthAdd", now.plusDays(-30).withTimeAtStartOfDay())
                                .addUnboundedFrom("historyTotal", now.plusYears(-6))).setSize(0).execute().actionGet();
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
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(
                        AggregationBuilders.terms(aggName)
                        .field(EsConstant.mediaTypeField).size(10).order(Terms.Order.count(true))
                ).setSize(0).execute()
                .actionGet();
        return buildTermLists(resp, aggName);
    }

    /**
     * 根据发布时间来判断舆情传播渠道分布
     * @param startTime
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionMediaSpread(DateTime startTime) {

        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        String aggName = "media_aggs";
        TransportClient client = esUtil.getClient();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        boolQuery.must(QueryBuilders.existsQuery(EsConstant.warnTimeField)); // 必须是已经被预警的
        boolQuery.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未进入舆情任务中的舆情

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(boolQuery)
                .addAggregation(
                        AggregationBuilders.terms(aggName)
                                .field(EsConstant.mediaTypeField).size(10).order(Terms.Order.count(false))
                ).setSize(0).execute()
                .actionGet();
        return buildTermLists(resp, aggName);
    }

    /**
     * 查询预警舆情
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param pb: 分页
     * @return
     */
    @Override
    @TimeUsed
    public OpinionEsSearchVO queryWarningOpinion(DateTime startTime, Integer emotion, Integer mediaType, PageBounds pb) {
        String hotLevelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        Integer twoClss = map.get(2);
        Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        query.must(QueryBuilders.existsQuery(EsConstant.warnTimeField)); // 必须是已经被预警的
        query.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未进入舆情任务中的舆情

        if (emotion != null)
            query.must(QueryBuilders.termQuery(EsConstant.emotionField, emotion));

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(EsConstant.hotField).keyed(true).addRange("levelOne", oneClass, Integer.MAX_VALUE)
                .addRange("levelTwo", twoClss, oneClass).addRange("levelThree", threeClass, twoClss);
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(EsConstant.mediaTypeField);

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .setFetchSource(null, new String[]{EsConstant.contentField, EsConstant.keysField, EsConstant.keywordField})
                .addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC))
                .addAggregation(hotLevelAgg).addAggregation(mediaAgg);
        if (mediaType != null)
            builder.setPostFilter(QueryBuilders.termQuery(EsConstant.mediaTypeField, mediaType));

        //step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        SearchHits hits = resp.getHits();
        result.setTotal(hits.getTotalHits());
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        result.setOpinions(opList);

        List<KeyValueVO> hotLevelList = buildHotLevelLists(resp, hotLevelAggName);
        List<KeyValueVO> mediaList = buildTermLists(resp, mediaAggName);
        result.setHotLevelStats(hotLevelList);
        result.setMediaTypeStats(mediaList);

        return result;
    }

    /**
     * 查询预警舆情
     * @param startTime
     * @return
     */
    @Override
    public List<OpinionBaseInfoReport> queryWarningOpinion(DateTime startTime) {

        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        TransportClient client = esUtil.getClient();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        boolQuery.must(QueryBuilders.existsQuery(EsConstant.warnTimeField)); // 必须是已经被预警的
        boolQuery.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未进入舆情任务中的舆情

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setSize(100)
                .setQuery(boolQuery)
                .setFetchSource(null, new String[]{EsConstant.contentField, EsConstant.keysField, EsConstant.keywordField})
                .addSort(EsConstant.hotField, SortOrder.DESC)
                .execute().actionGet();

        List<OpinionBaseInfoReport> rs = EsUtil.buildResult(resp, OpinionBaseInfoReport.class);
        List<WarnSetting> setting = settingService.queryWarnSetting(3);
        for (OpinionBaseInfoReport r : rs) {
            if (r.getLevel() == null) {
                Integer level = settingService.judgeOpinionSettingClass(r.getHot(), setting);
                r.setLevel(level);
            }
        }
        return rs;
    }

    @Override
    public OpinionEsSearchVO queryEventOpinions(Long eventId, DateTime startTime, Integer emotion, Integer mediaType, Integer hot, PageBounds pb) {
        String hotLevelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";
        String emotionAggName = "emotion_agg";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        Integer twoClss = map.get(2);
        Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (startTime != null) {
            query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        query.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        if (hot != -1) {
            query.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(hot));
        } else {
            query.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        }
        
        if (emotion != null)
            query.must(QueryBuilders.termQuery(EsConstant.emotionField, emotion));

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(EsConstant.hotField).keyed(true).addRange("levelOne", oneClass, Integer.MAX_VALUE)
                .addRange("levelTwo", twoClss, oneClass ).addRange("levelThree", threeClass, twoClss);
        if (hot != -1 && threeClass > 10) {
            hotLevelAgg.addRange("overThanTen", hot, threeClass);
        }
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(EsConstant.mediaTypeField);
        TermsAggregationBuilder emotionAgg = AggregationBuilders.terms(emotionAggName).field(EsConstant.emotionField);

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(pb.getOffset()).setSize(pb.getLimit()).setQuery(query)
                .addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC)).addAggregation(hotLevelAgg).addAggregation(mediaAgg).addAggregation(emotionAgg);
        if (mediaType != null)
            builder.setPostFilter(QueryBuilders.termQuery(EsConstant.mediaTypeField, mediaType));

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        result.setOpinions(opList);

        List<KeyValueVO> hotLevelList = buildHotLevelLists(resp, hotLevelAggName);
        result.setHotLevelStats(hotLevelList);
        List<KeyValueVO> mediaList = buildTermLists(resp, mediaAggName);
        result.setMediaTypeStats(mediaList);
        List<KeyValueVO> emotionList = buildTermLists(resp, emotionAggName);
        result.setEmotionStats(emotionList);
        return result;
    }

   
    /**
     * 获取事件相关信息总量
     * @param eventId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Long queryEventInfoTotal(Long eventId, DateTime startTime, DateTime endTime, boolean isWarn) {
        // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (startTime != null) {
            query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        if (endTime != null) {
            query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        } 
        if (isWarn == true) {
            // step-1：获取预警热度分界
            Map<Integer, Integer> map = settingService.getWarnClass();
            Integer threeClass = map.get(3);
            query.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        }
        query.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(0).setSize(1).setQuery(query);
        // step-2：查询并返回结果
        SearchResponse resp = builder.execute().actionGet();
        return resp.getHits().getTotalHits();
    }

    /**
     * 查询历史预警舆情
     * @param startTime: 开始时间
     * @param endTime：结束时间
     * @param emotion: 情感
     * @param mediaType: 媒体类型
     * @param pb: 分页
     * @return
     */
    @Override
    public OpinionEsSearchVO queryHistoryOpinions(DateTime startTime, DateTime endTime, Integer emotion, Integer mediaType, PageBounds pb) {
        String levelAggName = "hot_level_agg";
        String mediaAggName = "media_agg";
        // step-1：获取预警等级分界线

        // step-2：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.termQuery(EsConstant.opStatusField, 2)); // 必须是已经解除的
        if (emotion != null)
            query.must(QueryBuilders.termQuery(EsConstant.emotionField, emotion));

        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(EsConstant.mediaTypeField);
        TermsAggregationBuilder hotLevelAgg = AggregationBuilders.terms(levelAggName).field(EsConstant.levelField);

        // step-3：查询es
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(pb.getOffset()).setSize(pb.getLimit()).setQuery(query)
                .addSort(EsConstant.recordTimeField, SortOrder.DESC).addAggregation(hotLevelAgg).addAggregation(mediaAgg);
        if (mediaType != null)
            builder.setPostFilter(QueryBuilders.termQuery(EsConstant.mediaTypeField, mediaType));
        SearchResponse resp = builder.execute().actionGet();

        // step-4：构建返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        result.setTotal(resp.getHits().getTotalHits());
        List<OpinionEsVO> opList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        result.setOpinions(opList);

        // 舆情来源类型统计
        List<KeyValueVO> mediaList = buildTermLists(resp, mediaAggName);
        result.setMediaTypeStats(mediaList);
        // 预警等级水平统计
        List<KeyValueVO> hotLevleList = buildTermLists(resp, levelAggName);
        result.setHotLevelStats(hotLevleList);

        return result;
    }

    /**
     * 查询热点舆情（非预警）TOP100
     * @param startTime
     * @param emotion
     * @return
     */
    @TimeUsed
    public OpinionEsSearchVO queryTop100HotOpinion(DateTime startTime, Integer emotion) {
        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // step-2：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(EsConstant.hotField).lt(threeClass));
        query.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未处于舆情任务中的热点舆情
        if (emotion != null)
            query.must(QueryBuilders.termQuery(EsConstant.emotionField, emotion));

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .setFetchSource(null, EsConstant.contentField)
                .setQuery(query)
                .addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC)).setSize(100);

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        result.setTotal(resp.getHits().getTotalHits());
        // 列表结果
        List<OpinionEsVO> opList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        result.setOpinions(opList);

        return result;
    }

    /**
     * 热点舆情模糊查询
     * @param keyword
     * @param startTime
     * @param emotion
     * @param pb
     * @return
     */
    @Override
    public OpinionEsSearchVO getHotOpinionList(String keyword, DateTime startTime, Integer emotion, PageBounds pb) {
        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // step-2：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(keyword)) {
            query.must(QueryBuilders.multiMatchQuery(keyword, EsConstant.titleField, EsConstant.contentField));
        }
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(EsConstant.hotField).lt(threeClass));
        query.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未处于舆情任务中的热点舆情
        if (emotion != null) {
            query.must(QueryBuilders.termQuery(EsConstant.emotionField, emotion));
        }

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .setFetchSource(null, EsConstant.contentField);

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        SearchResponse resp = builder.execute().actionGet();
        result.setTotal(resp.getHits().getTotalHits());
        // 列表结果
        List<OpinionEsVO> opList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
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
            vo.setName(key);
            vo.setValue(count);
            result.add(vo);
        }
        return result;
    }

    private List<KeyValueVO> buildTermLists(SearchResponse resp, String aggName) {
        List<KeyValueVO> result = Lists.newArrayList();
        List<InternalTerms.Bucket> bs = ((InternalTerms) (resp.getAggregations().get(aggName))).getBuckets();
        for (InternalTerms.Bucket b : bs) {
            result.add(buildKeyValue(b));
        }
        return result;
    }

    private KeyValueVO buildKeyValue(InternalTerms.Bucket b) {
        KeyValueVO vo = new KeyValueVO();
        vo.setKey(b.getKey());
        vo.setName(b.getKey().toString());
        vo.setValue(b.getDocCount());
        return vo;
    }


    @Override
    public List<KeyValueVO> getEventOpinionCounts() {
        String eventsAgg = "events_agg";

        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(0).addAggregation(AggregationBuilders.terms(eventsAgg).field(EsConstant.eventsField).size(100)).execute()
                .actionGet();

        return buildTermLists(resp, eventsAgg);
    }

    @Override
    public List<KeyValueVO> getEventOpinionMediaSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "media_aggs";

        TransportClient client = esUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        if (startTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(booleanQuery).addAggregation(AggregationBuilders.terms(aggName).field(EsConstant.mediaTypeField).size(10))
                .setSize(0).execute().actionGet();
        return buildTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventWebsiteSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "website_aggs";

        TransportClient client = esUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        if (startTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        booleanQuery.mustNot(QueryBuilders.termQuery(EsConstant.websiteField, ""));
        
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(booleanQuery).addAggregation(AggregationBuilders.terms(aggName).field(EsConstant.websiteField).size(8)).setSize(0)
                .execute().actionGet();
        return buildTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventEmotionSpread(Long eventId, DateTime startTime, DateTime endTime) {
        String aggName = "emotion_aggs";

        TransportClient client = esUtil.getClient();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        if (startTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        if (endTime != null) {
            booleanQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).lte(endTime.toString(EsConstant.LONG_TIME_FORMAT)));
        }
        booleanQuery.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(booleanQuery).addAggregation(AggregationBuilders.terms(aggName).field(EsConstant.emotionField).size(10)).setSize(0)
                .execute().actionGet();
        return buildTermLists(resp, aggName);
    }

    /**
     * 根据舆情uuid查询舆情详情
     * @param uuid
     * @return
     */
    @Override
    public OpinionEsVO getOpinionByUUID(String uuid) {
        String uuidField = "uuid";
        TransportClient client = esUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(uuidField, uuid);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .setQuery(query)
                .setFetchSource(null, EsConstant.keysField)
                .setSize(1)
                .execute().actionGet();

        List<OpinionEsVO> list = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(list, calSimilarCount(getUuids(list)));
        if (list.isEmpty())
            return null;
        OpinionEsVO rs = list.get(0);
        rs.setSimiliarCount(calSimilarCount(uuid));
        return rs;
    }
    /**
     * 根据舆情uuids查询热度最高的舆情
     * @param uuids
     * @return
     */
    @Override
    public OpinionEsVO getMaxOpinionByUUIDs(List<String> uuids){
     // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termsQuery(EsConstant.OPINION_UUID, uuids));
        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(0).setSize(1).setQuery(query).addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC))
                .addSort(SortBuilders.fieldSort(EsConstant.publishTimeField).order(SortOrder.DESC)).addSort(SortBuilders.fieldSort(EsConstant.uuidField).order(SortOrder.DESC));

        // step-2：查询并返回结果
        SearchResponse resp = builder.execute().actionGet();
        List<OpinionEsVO> opList = EsUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(opList, calSimilarCount(getUuids(opList)));
        if (opList != null && opList.size() > 0) {
            return opList.get(0);
        } else {
            return new OpinionEsVO();
        }
    }

    /**
     * 当前用户待处理舆情列表
     * @param userId
     * @param transferType
     * @param pb
     * @return
     */
    @Override
    public PageList<OpinionTaskListVO> getUnProcessedList(Long userId, Integer transferType, PageBounds pb) {

        // step-1：构建es查询条件
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.opStatusField, 1));
        query.must(QueryBuilders.termQuery(EsConstant.opOwnerField, userId));
        if (transferType != null) {
            if (transferType <= 3)
                query.must(QueryBuilders.termsQuery(EsConstant.transferTypeField, new Integer[] { 1, 2}));
            else
                query.must(QueryBuilders.termsQuery(EsConstant.transferTypeField, new Integer[] { 3, 4, 5, 6 }));
        }

        // step-2：查询
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .setFetchSource(null, new String[]{EsConstant.keysField, EsConstant.keywordField, EsConstant.contentField})
                .addSort(EsConstant.recordTimeField, SortOrder.DESC)
                .execute().actionGet();

        // step-3：返回查询结果
        Long total = resp.getHits().getTotalHits();
        List<OpinionEsVO> esList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(esList, calSimilarCount(getUuids(esList)));
        List<OpinionTaskListVO> list = BeanMapperUtil.mapList(esList, OpinionTaskListVO.class);
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
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        // 判断当前用户是否是超级管理员(如果是管理员的话，就能看到所有的数据)
        UserInfo user = UserContext.getUser();
        if (!UserContext.isAdmin()) {
            query.must(QueryBuilders.matchQuery(EsConstant.operatorsField, user.getId())); // 操作者字段必须包含当前用户
            if(opStatus == 1) query.mustNot(QueryBuilders.termQuery(EsConstant.opOwnerField, user.getId())); // 舆情下一步需要操作的人不是当前用户
        }
        if (opStatus != null) {
            query.must(QueryBuilders.termQuery(EsConstant.opStatusField, opStatus));
        }

        // step-2：查询
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .setFetchSource(null, new String[]{EsConstant.contentField, EsConstant.keywordField, EsConstant.keysField})
                .addSort(EsConstant.recordTimeField, SortOrder.DESC)
                .execute().actionGet();

        // step-3：返回查询结果
        Long total = resp.getHits().getTotalHits();
        List<OpinionEsVO> esList = esUtil.buildResult(resp, OpinionEsVO.class);
        setSimilar(esList, calSimilarCount(getUuids(esList)));
        List<OpinionTaskListVO> list = BeanMapperUtil.mapList(esList, OpinionTaskListVO.class);
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
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION_OP_RECORD).setTypes(EsConstant.OPINION_OP_RECORD_TYPE).setSearchType(SearchType.DEFAULT)
                .setQuery(buildSearchRequest(null, keyMap))
                .addSort(EsConstant.opTimeField, SortOrder.ASC)
                .setSize(szie).execute().actionGet();

        List<OpinionOpRecordVO> list = esUtil.buildResult(resp, OpinionOpRecordVO.class);
        return list;
    }

    /**
     * 获取舆情热度走势
     * @param uuid
     * @param startTime
     * @return
     */
    @Override
    public List<OpinionHotEsVO> getOpinionHotTrend(String uuid, DateTime startTime) {
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.hotTimeField).gte(startTime.toString("yyyy-MM-dd HH:mm:ss")));
        boolQuery.must(QueryBuilders.termQuery(EsConstant.uuidField, uuid));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION_HOT)
                .setQuery(boolQuery)
                .setSize(9999).execute().actionGet();
        List<OpinionHotEsVO> result = esUtil.buildResult(resp, OpinionHotEsVO.class);
        return result;
    }

    /**
     * 获取舆情总量和预警总量
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionSta() {
        String aggsname = "opinon_group_sta";
        // step-1：预警热度分解
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // 查询
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .addAggregation(AggregationBuilders.range("opinon_group_sta").field(EsConstant.hotField).keyed(true).addRange("opinionCount", 0, 101).addRange("warnCount", threeClass, Integer.MAX_VALUE)).setSize(0)
                .execute().actionGet();
        List<KeyValueVO> list = buildHotLevelLists(resp, aggsname);
        return list;
    }

    /**
     * 舆情数据库折线图
     * @return
     */
    @Override
    public Map<String, List<KeyValueVO>> getOpinionStaLine() {
        String monthAgg = "month_agg";
        String countAgg = "count_agg";
        TransportClient client = esUtil.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT).setSize(0);

        DateRangeAggregationBuilder monthRange = AggregationBuilders.dateRange(monthAgg).field(EsConstant.publishTimeField).keyed(true);
        addRange(monthRange, 2);
        DateRangeAggregationBuilder countRange = AggregationBuilders.dateRange(countAgg).field(EsConstant.publishTimeField).keyed(true);
        addRange(countRange, 1);
        SearchResponse resp = requestBuilder.addAggregation(monthRange).addAggregation(countRange).execute().actionGet();
        List<KeyValueVO> month = buildHotLevelLists(resp, monthAgg);
        List<KeyValueVO> count = buildHotLevelLists(resp, countAgg);

        Map<String, List<KeyValueVO>> map = Maps.newHashMap();
        map.put("month", month);
        map.put("count", count);
        return map;
    }

    /**
     * 实时统计预警舆情数量
     * @return
     */
    @Override
    public List<KeyValueVO> opinionInstant() {
        String hotAgg = "hotAgg";
        TransportClient client = esUtil.getClient();
        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        Integer twoClss = map.get(2);
        Integer oneClass = map.get(1);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField));
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(BusinessUtils.getDateByTimeSpan(3).toString("yyyy-MM-dd HH:mm:ss")));
        SearchResponse resp = client
                .prepareSearch(EsConstant.IDX_OPINION)
                .setTypes(EsConstant.OPINION_TYPE)
                .setSearchType(SearchType.DEFAULT)
                .setSize(0)
                .setQuery(query)
                .addAggregation(
                        AggregationBuilders.range(hotAgg).field(EsConstant.hotField).keyed(true).addRange(levelThree, threeClass, twoClss).addRange(levelTwo, twoClss, oneClass)
                                .addRange(levelOne, oneClass, Integer.MAX_VALUE)).execute().actionGet();
        List<KeyValueVO> list = buildHotLevelLists(resp, hotAgg);
        return list;
    }

    /**
     * 实时统计预警舆情数量
     * @return
     */
    @Override
    public Integer opinionInstantByEvent(Long eventId) {
        String eventHotAgg = "eventHotAgg";
        TransportClient client = esUtil.getClient();

        List<WarnSettingVo> eventNewWarnList = settingService.getWarnSettingList(1, eventId);
        int eventNewMin = eventNewWarnList.get(0).getMin();
        // step-1：获取预警热度分界
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        DateTime dayOneMonthBefore = new DateTime().minusDays(30);
        query.must(QueryBuilders.termQuery(EsConstant.eventsField, eventId));
        query.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(dayOneMonthBefore.toString(EsConstant.LONG_TIME_FORMAT)));
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT).setSize(0).setQuery(query)
                .addAggregation(AggregationBuilders.range(eventHotAgg).field(EsConstant.hotField).keyed(true).addUnboundedFrom("eventNewOpinion", eventNewMin)).execute().actionGet();
        List<KeyValueVO> list = buildHotLevelLists(resp, eventHotAgg);
        if (list != null && list.size() > 0) {
            return (int) list.get(0).getValue();
        } else {
            return 0;
        }

    }

    /**
     * 查询管理员用户已介入、已解除、已监测数量
     * @return
     */
    @Override
    public List<KeyValueVO> queryCoutGroupOpStatus() {
        String aggsName = "task_sta";
        TransportClient client = esUtil.getClient();
        TermsAggregationBuilder agg = AggregationBuilders.terms(aggsName).field(EsConstant.opStatusField);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(agg)
                .setSize(0).execute().actionGet();
        List<KeyValueVO> result = buildTermLists(resp, aggsName);
        return result;
    }

    /**
     * 查询普通用户待处理、已转发、已解除、已监测数量
     * @param userId
     * @return
     */
    @Override
    public List<KeyValueVO> queryCoutGroupOpStatus(Long userId) {
        PageBounds pb = new PageBounds(0,0);
        int unOp = getUnProcessedList(userId, null, pb).getPaginator().getTotalCount(); //待处理数量
        int transfer = getProcessedList(1, pb).getPaginator().getTotalCount(); // 已转发数量
        int remove = getProcessedList(2, pb).getPaginator().getTotalCount(); // 已解除数量
        int monitor = getProcessedList(3, pb).getPaginator().getTotalCount(); // 已监测数量
        List<Integer> list = Arrays.asList(unOp, transfer, remove, monitor);
        List<KeyValueVO> result = Lists.newLinkedList();
        for(int i=0; i<list.size(); i++) {
            KeyValueVO v = new KeyValueVO();
            Integer l = list.get(i);
            v.setKey(i);
            v.setValue(l);
            result.add(v);
        }
        return result;
    }

    /**
     * 查询舆情相同文章信息
     * @param uuid
     * @return
     */
    @Override
    public List<SimiliarNewsVO> querySimiliarNews(String uuid, PageBounds pb) {
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION_SIMILAR_NEWS)
                .setQuery(QueryBuilders.termQuery(EsConstant.uuidField, uuid))
                .setSize(6666)
                .execute().actionGet();
        List<SimiliarNewsVO> list = EsUtil.buildResult(resp, SimiliarNewsVO.class);

        OpinionEsVO opinion = getOpinionByUUID(uuid);
        String md5 = opinion.getMd5();
        String link = opinion.getLink();

        List<SimiliarNewsVO> temp = list;
        if (Objects.nonNull(md5)) {
            temp = list.stream().filter(vo -> !vo.getMd5().equals(md5)).collect(Collectors.toList());
        } else {
            if (Objects.nonNull(link))
                temp = list.stream().filter(vo -> !vo.getLink().equals(link)).collect(Collectors.toList());
        }

        Long total = resp.getHits().getTotalHits() - 1;
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), total.intValue());
        PageList<SimiliarNewsVO> result = PageListHelper.create(temp, paginator);
        return result;
    }

    /**
     * 新增一级、二级、三级预警舆情数量
     * @param lastSendTime
     * @return
     */
    @Override
    public Map<Integer, Integer> queryAddWarnCount(DateTime lastSendTime) {
        TransportClient client = esUtil.getClient();
        DateRangeAggregationBuilder aggOne = AggregationBuilders.dateRange(levelOne).field(EsConstant.OPINION_FIRST_WARN_TIME_ONE).addUnboundedFrom("1", lastSendTime);
        DateRangeAggregationBuilder aggTwo = AggregationBuilders.dateRange(levelTwo).field(EsConstant.OPINION_FIRST_WARN_TIME_TWO).addUnboundedFrom("2", lastSendTime);
        DateRangeAggregationBuilder aggThree = AggregationBuilders.dateRange(levelThree).field(EsConstant.OPINION_FIRST_WARN_TIME_THREE).addUnboundedFrom("3", lastSendTime);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(aggOne)
                .addAggregation(aggTwo)
                .addAggregation(aggThree)
                .setSize(0).execute().actionGet();
        List<KeyValueVO> one = buildHotLevelLists(resp, levelOne);
        List<KeyValueVO> two = buildHotLevelLists(resp, levelTwo);
        List<KeyValueVO> three = buildHotLevelLists(resp, levelThree);
        List<KeyValueVO> result = Lists.newLinkedList();
        result.addAll(one);
        result.addAll(two);
        result.addAll(three);
        Map<Integer, Integer> map = result.stream().collect(
                Collectors.toMap(p -> Integer.parseInt(p.getKey().toString()), p -> Integer.parseInt(p.getValue().toString()))
        );
        return map;
    }

    /**
     * 查询新增预警舆情热度最高的
     * @param lastSendTime
     * @param type
     * @return
     */
    @Override
    public Integer queryMaxHot(DateTime lastSendTime, Integer type) {
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(type == 1)
            query.must(QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_ONE).gt(lastSendTime.toString("yyyy-MM-dd HH:mm:ss")));
        else if(type == 2)
            query.must(QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_TWO).gt(lastSendTime.toString("yyyy-MM-dd HH:mm:ss")));
        else
            query.must(QueryBuilders.rangeQuery(EsConstant.OPINION_FIRST_WARN_TIME_THREE).gt(lastSendTime.toString("yyyy-MM-dd HH:mm:ss")));
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFetchSource(EsConstant.hotField, null)
                .setQuery(query)
                .addSort(EsConstant.hotField, SortOrder.DESC)
                .setSize(1).execute().actionGet();
        List<OpinionVO> opinion = EsUtil.buildResult(resp, OpinionVO.class);
        if(opinion.isEmpty()) return null;
        Integer hot = opinion.get(0).getHot();
        return hot;
    }

    /**
     * 历史舆情详情
     * @param uuid
     */
    @Override
    public OpinionEsVO queryHistoryWarnDetail(String uuid) {
        TransportClient client = esUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(EsConstant.uuidField, uuid));
        query.must(QueryBuilders.termQuery(EsConstant.opStatusField, 2));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(query).execute().actionGet();
        List<OpinionEsVO> list = EsUtil.buildResult(resp, OpinionEsVO.class);
        if(!list.isEmpty()) {
            OpinionEsVO rs = list.get(0);
            rs.setSimiliarCount(calSimilarCount(uuid));
            return rs;
        }
        return null;
    }

    /**
     * 舆情情感数量统计（报告专用）
     * @param startTime
     * @return
     */
    @Override
    public List<KeyValueVO> queryAffectionSta(DateTime startTime) {

        String aggs = "affection_aggs";
        TransportClient client = esUtil.getClient();

        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        boolQuery.must(QueryBuilders.existsQuery(EsConstant.warnTimeField)); // 必须是已经被预警的
        boolQuery.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未进入舆情任务中的舆情

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(boolQuery)
                .setSize(0)
                .addAggregation(AggregationBuilders.terms(aggs).field(EsConstant.emotionField))
                .execute().actionGet();
        List<KeyValueVO> rs = buildTermLists(resp, aggs);
        return rs;
    }

    /**
     * 舆情等级统计（报告专用）
     * @param startTime
     * @return
     */
    @Override
    public List<KeyValueVO> queryHotLevelSta(DateTime startTime) {

        String aggs = "affection_aggs";
        TransportClient client = esUtil.getClient();

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);
        Integer twoClss = map.get(2);
        Integer oneClass = map.get(1);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.publishTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        boolQuery.must(QueryBuilders.rangeQuery(EsConstant.hotField).gte(threeClass));
        boolQuery.must(QueryBuilders.existsQuery(EsConstant.warnTimeField)); // 必须是已经被预警的
        boolQuery.mustNot(QueryBuilders.existsQuery(EsConstant.opStatusField)); // 必须是未进入舆情任务中的舆情

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(aggs).field(EsConstant.hotField).keyed(true).addRange("levelOne", oneClass, Integer.MAX_VALUE)
                .addRange("levelTwo", twoClss, oneClass).addRange("levelThree", threeClass, twoClss);

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setSize(0)
                .setQuery(boolQuery)
                .addSort(SortBuilders.fieldSort(EsConstant.hotField).order(SortOrder.DESC))
                .addAggregation(hotLevelAgg)
                .execute().actionGet();
        List<KeyValueVO> hotLevelList = buildHotLevelLists(resp, aggs);
        return hotLevelList;
    }

    /**
     * 添加范围查询
     * @param dateRange
     * @param state 1-累积总量；2-当月数量
     */
    private void addRange(DateRangeAggregationBuilder dateRange, Integer state) {
        DateTime now = DateTime.now();
        DateTime startTime = now.plusMonths(-11).withTimeAtStartOfDay();
        dateRange.format("yyyy-MM");
        int between = Months.monthsBetween(startTime, now).getMonths();
        for (int i = 0; i <= between; i++) {
            if (state == 1)
                dateRange.addRange(startTime.plusMonths(i).toString("yyyy年MM月"), startTime.plusYears(-10), startTime.plusMonths(i + 1));
            else if (state == 2)
                dateRange.addRange(startTime.plusMonths(i).toString("yyyy年MM月"), startTime.plusMonths(i), startTime.plusMonths(i + 1));
        }
    }

    // 创建BoolQueryBuilder
    private BoolQueryBuilder buildSearchRequest(Map<String, List<String>> matchMap, Map<String, Object> keyMap) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (keyMap != null) {
            for (String key : keyMap.keySet()) {
                Object value = keyMap.get(key);
                query.must(QueryBuilders.termsQuery(key, value));
            }
        }
        if (matchMap != null) {
            for (String key : matchMap.keySet()) {
                List<String> value = matchMap.get(key);
                String[] arr = new String[value.size()];
                query.must(new MultiMatchQueryBuilder(key, value.toArray(arr)));
            }
        }
        return query;
    }

    /**
     * 计算相同文章数
     * @param uuid
     * @return
     */
    @Override
    public Integer calSimilarCount(String uuid) {
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION_SIMILAR_NEWS)
                .setQuery(QueryBuilders.termQuery(EsConstant.uuidField, uuid))
                .setSize(0).execute().actionGet();
        Long count = resp.getHits().getTotalHits();
        return count.intValue() - 1;
    }

    /**
     * 计算一批舆情的相似文章数
     * @param uuids
     * @return
     */
    @Override
    public Map<String, Object> calSimilarCount(List<String> uuids) {
        String aggs = "uuid_sta";
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION_SIMILAR_NEWS)
                .setQuery(QueryBuilders.termsQuery(EsConstant.uuidField, uuids))
                .setSize(0)
                .addAggregation(AggregationBuilders.terms(aggs).field(EsConstant.uuidField).size(uuids.size() + 1))
                .execute().actionGet();
        List<KeyValueVO> rs = buildTermLists(resp, aggs);
        Map<String, Object> map = rs.stream().collect(Collectors.toMap(KeyValueVO::getName, vo -> Integer.parseInt(vo.getValue().toString()) - 1));
        return map;
    }
    
    public void setSimilar(List<OpinionEsVO> opinionEsVOList, Map<String, Object> map) {
        if (opinionEsVOList == null) {
            return;
        }
        for (OpinionEsVO e : opinionEsVOList) {
            Object value = map.get(e.getUuid());
            if (Objects.nonNull(value))
                e.setSimiliarCount(Integer.valueOf(value.toString()));
            else
                e.setSimiliarCount(0);
        }
    }


    public List<String> getUuids(List<OpinionEsVO> opinionEsVOList) {
        List<String> uuids = new ArrayList<String>();
        if (opinionEsVOList == null) {
            return uuids;
        }
        for (OpinionEsVO e : opinionEsVOList) {
            uuids.add(e.getUuid());
        }
        return uuids;
    }
    

    /**
     * 检查舆情是否处于任务当中
     * @param uuid
     * @return
     */
    @Override
    public Boolean checkOpinionTasking(String uuid) {
        OpinionEsVO opinion = getOpinionByUUID(uuid);
        Integer statu = opinion.getOpStatus();
        statu = (statu == null ? 0 : statu);
        return statu != 0;
    }
}
