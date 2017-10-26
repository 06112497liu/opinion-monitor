/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.mybatis.domain.PageBounds;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch相关服务
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

    /**
     * 同步数到新的索引。创建新索引，将新索引与别名关联，删除就索引。
     * @param newIndex
     * @param oldIndex
     */
    public void syncOpinionToNewIndex(String newIndex, String oldIndex) {
        createIndex(newIndex);
        syncOpinions(newIndex);
        changeOpinionAlias(newIndex, oldIndex);
        deleteIndex(oldIndex);
    }

    public void changeOpinionAlias(String newIndex, String oldIndex) {
        IndicesAliasesResponse resp = EsUtil.getClient().admin().indices().prepareAliases().addAlias(newIndex, INDEX_ALIAS).removeAlias(oldIndex, INDEX_ALIAS).execute().actionGet();
        logger.info(String.valueOf(resp.isAcknowledged()));
    }

    /**
     * 创建服务
     */
    public void createIndex(String index) {
        if (checkIndexExists(index)) {
            deleteIndex(index);
        }
        EsUtil.getClient().admin().indices().prepareCreate(index).get();
    }

    /**
     * 检查索引是否存在
     * @param index
     * @return
     */
    private boolean checkIndexExists(String index) {
        IndicesExistsRequest req = new IndicesExistsRequest(index);
        IndicesExistsResponse resp = EsUtil.getClient().admin().indices().exists(req).actionGet();
        return resp.isExists();
    }

    /**
     * 删除索引
     * @param index
     */
    private void deleteIndex(String index) {
        DeleteIndexRequest req = new DeleteIndexRequest(index);
        DeleteIndexResponse resp = EsUtil.getClient().admin().indices().delete(req).actionGet();
    }

    /**
     * 同步舆情到ES
     */
    public void syncOpinions(String index) {
        List<OpinionEsVO> ds = opinionService.queryOpinion();
        if (ds.size() == 0) {
            return;
        }
        EsUtil.create(index, OPINION_TYPE, ds);
    }

    public List<OpinionEsVO> searchOpinions() {
        PageBounds pb = new PageBounds(1, 20);
        List<OpinionEsVO> rs = EsUtil.search(INDEX_ALIAS, OPINION_TYPE, QueryBuilders.matchAllQuery(), pb, OpinionEsVO.class);

        return rs;
    }
}
