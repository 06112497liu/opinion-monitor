/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsQueryService;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.util.StringUtils;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
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
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public class EsQueryServiceImpl implements EsQueryService {

    /**
     * 获取舆情数量 - 首页
     * @param startTime
     * @return
     */
    public OpinionCountStatVO getOpinionCountStatistic(DateTime startTime) {
        OpinionCountStatVO vo = new OpinionCountStatVO();

        String aggName = "level_count";
        String field = "hot";
        final String levelThree = "levelThree";
        final String levelTwo = "levelTwo";
        final String levelOne = "levelOne";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(QueryBuilders.rangeQuery(EsConstant.CALC_TIME_PROP).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)))
            .addAggregation(AggregationBuilders.range(aggName).field(field).keyed(true).addRange(levelThree, 60, 70).addRange(levelTwo, 70, 80).addRange(levelOne, 80, Integer.MAX_VALUE)).setSize(0)
            .execute().actionGet();

        InternalRange agg = resp.getAggregations().get(aggName);
        List<InternalRange.Bucket> bs = agg.getBuckets();
        int total = 0;
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
        List<KeyValueVO> result = Lists.newArrayList();

        String aggName = "top_kws";
        String termField = "keys";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).addAggregation(AggregationBuilders.terms(aggName).field(termField).size(10)).setSize(0).execute().actionGet();
        List<StringTerms.Bucket> bs = ((StringTerms) resp.getAggregations().get(aggName)).getBuckets();
        for (StringTerms.Bucket b : bs) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(b.getKey());
            vo.setValue(b.getDocCount());
            result.add(vo);
        }
        return result;
    }

    /**
     * 舆情传播渠道分布 - 首页
     * @return
     */
    public List<KeyValueVO> getOpinionMediaSpread() {
        String aggName = "media_aggs";
        String termField = "mediaType";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).addAggregation(AggregationBuilders.terms(aggName).field(termField).size(10)).setSize(0).execute().actionGet();

        return buildLongTermLists(resp, aggName);
    }

    /**
     * 查询预警舆情
     * @param startTime: 开始时间
     * @param emotion: 情感
     * @param pb: 分页
     * @return
     */
    public OpinionEsSearchVO queryWarningOpinion(DateTime startTime, Integer emotion, PageBounds pb) {
        OpinionEsSearchVO result = new OpinionEsSearchVO();

        List<OpinionEsVO> opList = Lists.newArrayList();

        String hotLevelAggName = "hot_level_agg";
        String hotField = "hot";
        String mediaAggName = "media_agg";
        String mediaField = "mediaType";

        String calcTimeField = "calcTime";
        String emotionField = "emotion";

        TransportClient client = EsUtil.getClient();

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery(calcTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).gte(60));
        if (emotion != null) {
            query.must(QueryBuilders.termQuery(emotionField, emotion));
        }

        RangeAggregationBuilder hotLevelAgg = AggregationBuilders.range(hotLevelAggName).field(hotField).keyed(true).addRange("levelOne", 80, 101).addRange("levelTwo", 70, 80)
            .addRange("levelThree", 60, 70);
        TermsAggregationBuilder mediaAgg = AggregationBuilders.terms(mediaAggName).field(mediaField);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setFrom(pb.getOffset()).setSize(pb.getLimit()).setQuery(query).addAggregation(hotLevelAgg).addAggregation(mediaAgg)
            .execute().actionGet();

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

    /**
     * 查询热点舆情（非预警）TOP100
     * @param param: 查询字符创
     * @param startTime
     * @param emotion
     * @return
     */
    public OpinionEsSearchVO queryTop100HotOpinion(String param, DateTime startTime, Integer emotion) {
        OpinionEsSearchVO result = new OpinionEsSearchVO();

        List<OpinionEsVO> opList = Lists.newArrayList();

        String hotField = "hot";
        String calcTimeField = "calcTime";
        String emotionField = "emotion";
        String titleField = "title";
        String contentField = "content";

        TransportClient client = EsUtil.getClient();

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param)) {
            query.must(QueryBuilders.multiMatchQuery(param, titleField, contentField));
        }
        query.must(QueryBuilders.rangeQuery(calcTimeField).gte(startTime.toString(EsConstant.LONG_TIME_FORMAT)));
        query.must(QueryBuilders.rangeQuery(hotField).lt(60));
        if (emotion != null) {
            query.must(QueryBuilders.termQuery(emotionField, emotion));
        }

        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(100).setQuery(query).addSort(SortBuilders.fieldSort(hotField).order(SortOrder.DESC)).execute().actionGet();

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

    @Override
    public List<KeyValueVO> getEventOpinionCounts() {
        String eventsAgg = "events_agg";
        String eventsField = "events";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setSize(0).addAggregation(AggregationBuilders.terms(eventsAgg).field(eventsField).size(100)).execute().actionGet();

        return buildLongTermLists(resp, eventsAgg);
    }

    @Override
    public List<KeyValueVO> getEventOpinionMediaSpread(Long eventId) {
        String eventsField = "events";
        String aggName = "media_aggs";
        String termField = "mediaType";

        TransportClient client = EsUtil.getClient();
        TermQueryBuilder query = QueryBuilders.termQuery(eventsField, eventId);
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setQuery(query).addAggregation(AggregationBuilders.terms(aggName).field(termField).size(10)).setSize(0).execute()
            .actionGet();

        return buildLongTermLists(resp, aggName);
    }
}
