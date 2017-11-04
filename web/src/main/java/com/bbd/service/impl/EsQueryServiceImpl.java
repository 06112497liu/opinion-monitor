/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsQueryService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.util.StringUtils;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import io.swagger.models.auth.In;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final String publicTimeField = "publicTime";
    private final String emotionField = "emotion";
    private final String keysField = "keys";
    private final String eventsField = "events";
    private final String calcTimeField = "calcTime";
    private final String titleField = "title";
    private final String contentField = "content";
    private final String websiteField = "website";

    private Integer oneClass;
    private Integer twoClass;
    private Integer threeClass;

    /**
     * 获取预警舆情top10（排除在舆情任务中的预警舆情，以及热点舆情）
     * @return
     */
    @Override
    public List<OpinionEsVO> getWarnOpinionTopTen() {
        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // 构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(hotField).gte(threeClass));
        query.must(QueryBuilders.rangeQuery(publicTimeField).gte(levelThree));
        return null;
    }

    /**
     * 获取舆情数量 - 首页
     * @param startTime
     * @return
     */
    public OpinionCountStatVO getOpinionCountStatistic(DateTime startTime) {
        String aggName = "level_count";

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3); Integer twoClss = map.get(2); Integer oneClass = map.get(1);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(QueryBuilders.rangeQuery(EsConstant.CALC_TIME_PROP).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)))
                .addAggregation(AggregationBuilders.range(aggName).field(hotField).keyed(true)
                        .addRange(levelThree, threeClass, twoClss-1).addRange(levelTwo, twoClss, oneClass-1).addRange(levelOne, oneClass, Integer.MAX_VALUE))
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
     * 关键词排行TOP10 - 首页
     * @return
     */
    public List<KeyValueVO> getKeywordsTopTen() {
        String aggName = "top_kws";
        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .addAggregation(AggregationBuilders.terms(aggName).field(keysField).size(10))
                .setSize(0).execute().actionGet();
        return buildStringTermLists(resp, aggName);
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

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(publicTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).gte(threeClass));
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));
        if (mediaType != null) query.must(QueryBuilders.termQuery(mediaTypeField, mediaType));

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(hotField).keyed(true)
                .addRange("levelOne", oneClass, Integer.MAX_VALUE).addRange("levelTwo", twoClss, oneClass-1).addRange("levelThree", threeClass, twoClss-1);
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(mediaTypeField);

        SearchRequestBuilder builder = client.prepareSearch(EsConstant.IDX_OPINION)
                .setFrom(pb.getOffset()).setSize(pb.getLimit())
                .setQuery(query)
                .addAggregation(hotLevelAgg)
                .addAggregation(mediaAgg);
        if (mediaType != null) builder.setPostFilter(QueryBuilders.termQuery(mediaTypeField, mediaType));

        //step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        List<OpinionEsVO> opList = Lists.newArrayList();
        SearchResponse resp = builder.execute().actionGet();
        SearchHits hits = resp.getHits();
        result.setTotal(hits.getTotalHits());
        SearchHit[] items = hits.getHits();
        for (SearchHit item : items) {
            String source = item.getSourceAsString();
            OpinionEsVO vo = JsonUtil.parseObject(source, OpinionEsVO.class);
            opList.add(vo);
        }
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
        query.must(QueryBuilders.rangeQuery(calcTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
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
                .addAggregation(hotLevelAgg)
                .addAggregation(mediaAgg);
        if (mediaType != null) builder.setPostFilter(QueryBuilders.termQuery(mediaTypeField, mediaType));

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        List<OpinionEsVO> opList = Lists.newArrayList();
        SearchResponse resp = builder.execute().actionGet();
        SearchHits hits = resp.getHits();
        result.setTotal(hits.getTotalHits());
        SearchHit[] items = hits.getHits();
        for (SearchHit item : items) {
            String source = item.getSourceAsString();
            OpinionEsVO vo = JsonUtil.parseObject(source, OpinionEsVO.class);
            opList.add(vo);
        }
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
    public OpinionEsSearchVO queryTop100HotOpinion(String param, DateTime startTime, Integer emotion) {

        // step-1：获取预警热度分界
        Map<Integer, Integer> map = settingService.getWarnClass();
        Integer threeClass = map.get(3);

        // step-2：构建es查询条件
        TransportClient client = EsUtil.getClient();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param)) query.must(QueryBuilders.multiMatchQuery(param, titleField, contentField));
        query.must(QueryBuilders.rangeQuery(publicTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).lt(threeClass));
        if (emotion != null) query.must(QueryBuilders.termQuery(emotionField, emotion));

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(100)
                .setQuery(query).addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC))
                .execute().actionGet();

        // step-3：查询并返回结果
        OpinionEsSearchVO result = new OpinionEsSearchVO();
        List<OpinionEsVO> opList = Lists.newArrayList();
        SearchHits hits = resp.getHits();
        result.setTotal(hits.getTotalHits());
        SearchHit[] items = hits.getHits();
        for (SearchHit item : items) {
            String source = item.getSourceAsString();
            OpinionEsVO vo = JsonUtil.parseObject(source, OpinionEsVO.class);
            opList.add(vo);
        }
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
    public List<KeyValueVO> getEventOpinionMediaSpread(Long eventId) {
        String aggName = "media_aggs";

        TransportClient client = EsUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(eventsField, eventId);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query)
                .addAggregation(AggregationBuilders.terms(aggName).field(mediaTypeField).size(10)).setSize(0)
                .execute().actionGet();
        return buildLongTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventWebsiteSpread(Long eventId) {
        String aggName = "website_aggs";

        TransportClient client = EsUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(eventsField, eventId);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query)
                .addAggregation(AggregationBuilders.terms(aggName).field(websiteField).size(8)).setSize(0)
                .execute().actionGet();
        return buildStringTermLists(resp, aggName);
    }

    @Override
    public List<KeyValueVO> getEventEmotionSpread(Long eventId) {
        String aggName = "emotion_aggs";

        TransportClient client = EsUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(eventsField, eventId);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query)
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
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION)
                .setQuery(query).setFrom(0).setSize(1)
                .execute().actionGet();

        SearchHits hits = resp.getHits();
        SearchHit[] items = hits.getHits();
        OpinionEsVO vo = null;
        for (SearchHit item : items) {
            String source = item.getSourceAsString();
            vo = JsonUtil.parseObject(source, OpinionEsVO.class);
        }
        return vo;
    }
}
