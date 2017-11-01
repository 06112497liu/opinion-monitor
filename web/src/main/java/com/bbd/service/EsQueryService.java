/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.constant.EsConstant;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.util.EsUtil;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ES查询服务
 * @author tjwang
 * @version $Id: EsQueryService.java, v 0.1 2017/10/31 0031 17:04 tjwang Exp $
 */
@Service
public class EsQueryService {

    /**
     * 获取舆情
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

    List<KeyValueVO> getKeywordsTopTen() {
        List<KeyValueVO> result = Lists.newArrayList();

        String aggName = "top_kws";
        String termField = "keys";

        TransportClient client = EsUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).addAggregation(AggregationBuilders.terms(aggName).field(termField).size(10)).setSize(0).execute().actionGet();
        List<StringTerms.Bucket> bs = ((StringTerms) resp.getAggregations().get(aggName)).getBuckets();
        for (StringTerms.Bucket b : bs) {
            String key = String.valueOf(b.getKey());
            Integer count = Long.valueOf(b.getDocCount()).intValue();
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(key);
            vo.setName(key);
            vo.setValue(count);
            result.add(vo);
        }
        return result;
    }
}
