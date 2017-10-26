/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.util.EsUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch相关服务
 * @author tjwang
 * @version $Id: EsService.java, v 0.1 2017/10/25 0025 17:43 tjwang Exp $
 */
@Service
public class EsService {

    /**
     * 创建服务
     */
    public void createIndex() {
        if (checkIndexExists(EsUtil.INDEX)) {
            deleteIndex(EsUtil.INDEX);
        }
        EsUtil.getClient().admin().indices().prepareCreate("bbd_opinion_a").get();
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

}
