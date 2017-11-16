/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch相关服务
 *
 * @author tjwang
 * @version $Id: EsService.java, v 0.1 2017/10/25 0025 17:43 tjwang Exp $
 */
@Service
public class EsService {

    private static final String INDEX_ALIAS  = "bbd_opinion";
    private static final String OPINION_TYPE = "opinion";
    private Logger              logger       = LoggerFactory.getLogger(getClass());
    @Autowired
    private OpinionService      opinionService;

    @Autowired
    private EsUtil              esUtil;

    /**
     * 同步数到新的索引。创建新索引，将新索引与别名关联，删除就索引。
     *
     * @param newIndex
     * @param oldIndex
     */
    public void syncOpinionToNewIndex(String newIndex, String oldIndex) {
        createIndex(newIndex);
        syncOpinions(newIndex);
        changeOpinionAlias(newIndex, oldIndex, INDEX_ALIAS);
        deleteIndex(oldIndex);
    }

    public void changeOpinionAlias(String newIndex, String oldIndex, String indexAlias) {
        IndicesAliasesRequestBuilder builder = esUtil.getClient().admin().indices().prepareAliases().addAlias(newIndex, indexAlias);
        if (checkIndexExists(oldIndex)) {
            builder.removeAlias(oldIndex, indexAlias);
        }
        builder.execute().actionGet();
    }

    /**
     * 创建服务
     */
    public void createIndex(String index) {
        if (checkIndexExists(index)) {
            deleteIndex(index);
        }
        esUtil.getClient().admin().indices().prepareCreate(index).setSettings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 1)).get();
    }

    /**
     * 创建舆情索引
     * @param index
     */
    public void createOpinionIndex(String index, String type) {
        if (checkIndexExists(index)) {
            deleteIndex(index);
        }
        String mapping = "{\"opinion\": {  \"properties\": {\"esId\": {  \"type\": \"keyword\"},\"source\": {  \"type\": \"keyword\"},\"uuid\": {  \"type\": \"keyword\"},\"startTime\": {  \"type\": \"date\",  \"format\": \"yyyy-MM-dd HH:mm:ss\"},\"gmtCreate\": {  \"type\": \"date\",  \"format\": \"yyyy-MM-dd HH:mm:ss\"},\"gmtModified\": {  \"type\": \"date\",  \"format\": \"yyyy-MM-dd HH:mm:ss\"}  }}  }";
        esUtil.getClient().admin().indices().prepareCreate(index).setSettings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 1)).addMapping(type, mapping, XContentType.JSON)
            .get();
    }

    /**
     * 检查索引是否存在
     *
     * @param index
     * @return
     */
    private boolean checkIndexExists(String index) {
        IndicesExistsRequest req = new IndicesExistsRequest(index);
        IndicesExistsResponse resp = esUtil.getClient().admin().indices().exists(req).actionGet();
        return resp.isExists();
    }

    /**
     * 删除索引
     *
     * @param index
     */
    public void deleteIndex(String index) {
        if (checkIndexExists(index)) {
            DeleteIndexRequest req = new DeleteIndexRequest(index);
            esUtil.getClient().admin().indices().delete(req).actionGet();
        }
    }

    /**
     * 同步舆情到ES
     */
    public void syncOpinions(String index) {
        List<OpinionEsVO> ds = opinionService.queryOpinion();
        if (ds.size() == 0) {
            return;
        }
        esUtil.create(index, OPINION_TYPE, ds);
    }

    public List<OpinionEsVO> searchOpinions() {
        PageBounds pb = new PageBounds(1, 20);
        List<OpinionEsVO> rs = esUtil.search(INDEX_ALIAS, OPINION_TYPE, QueryBuilders.matchAllQuery(), pb, OpinionEsVO.class);

        return rs;
    }

    public void searchOpinionAggs() {
        SearchResponse resp = esUtil.getClient().prepareSearch(INDEX_ALIAS).addAggregation(AggregationBuilders.terms("agg").field("emotion")).setSize(0).execute().actionGet();
        Aggregations aggs = resp.getAggregations();
        Terms agg = aggs.get("agg");
        List<? extends Terms.Bucket> buckets = agg.getBuckets();
        for (Terms.Bucket b : buckets) {
            logger.info("key: {}, value: {}", b.getKey(), b.getDocCount());
        }

    }

    /**
     * 获取当前索引
     * @return
     */
    public String getCurrentAlias() {
        GetAliasesResponse resp = esUtil.getClient().admin().indices().prepareGetAliases("bbd_opinion").execute().actionGet();
        if (resp.getAliases().isEmpty()) {
            return null;
        }
        ArrayList<String> list = Lists.newArrayList(resp.getAliases().keysIt());
        return list.get(0);
    }
}
