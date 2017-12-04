package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsInitService;
import com.bbd.util.EsUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: EsInitServiceImpl.java, v0.1 2017/12/1 Liuweibo Exp $$
 */
@Service
public class EsInitServiceImpl implements EsInitService {

    @Autowired
    private EsUtil esUtil;

    private Logger logger = LoggerFactory.getLogger(EsInitServiceImpl.class);
    
    @Override
    public void delOpOwnerField() {
        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List<String> list = getUUIDList(EsConstant.opOwnerField);
        buildBulkRequest(bulkRequest, list, EsConstant.opOwnerField);
        bulkRequest.execute().actionGet();
    }

    @Override
    public void delOperatorsField() {
        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List<String> list = getUUIDList(EsConstant.operatorsField);
        buildBulkRequest(bulkRequest, list, EsConstant.operatorsField);
        bulkRequest.execute().actionGet();
    }

    @Override
    public void delTransferTypeField() {
        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List<String> list = getUUIDList(EsConstant.transferTypeField);
        buildBulkRequest(bulkRequest, list, EsConstant.transferTypeField);
        bulkRequest.execute().actionGet();
    }

    @Override
    public void delOpStatusField() {
        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List<String> list = getUUIDList(EsConstant.opStatusField);
        buildBulkRequest(bulkRequest, list, EsConstant.opStatusField);
        bulkRequest.execute().actionGet();
    }

    @Override
    public void delOpRecord(String index) {
        TransportClient client = esUtil.getClient();
        DeleteIndexResponse resp = client.admin().indices().prepareDelete(EsConstant.IDX_OPINION_OP_RECORD).execute().actionGet();
        if (resp.isAcknowledged()) {
            System.out.println("delete index " + index + "  successfully!");
        } else {
            System.out.println("Fail to delete index " + index);
        }
        logger.info("resp: " + resp.toString());
    }

    /**
     * 构建bulkRequest
     * @param bulkRequest
     * @param uuids
     * @param removeField
     */
    private void buildBulkRequest(BulkRequestBuilder bulkRequest, List<String> uuids, String removeField) {
        for (String id : uuids) {
            UpdateRequest updateRequest = new UpdateRequest(EsConstant.IDX_OPINION, EsConstant.OPINION_TYPE, id);
            updateRequest.script(new Script("ctx._source.remove('"+ removeField +"')"));
            bulkRequest.add(updateRequest);
        }
    }

    /**
     * 查询某个操作字段存在的舆情
     * @param field
     * @return
     */
    private List<String> getUUIDList(String field) {
        TransportClient client = esUtil.getClient();
        SearchResponse resp = client.prepareSearch(EsConstant.IDX_OPINION).setTypes(EsConstant.OPINION_TYPE).setSearchType(SearchType.DEFAULT)
                .setQuery(QueryBuilders.existsQuery(field))
                .setFetchSource("uuid", null)
                .setSize(9999).execute().actionGet();
        SearchHit[] hits = resp.getHits().getHits();
        List<String> idList = Arrays.stream(hits).map(SearchHit::getId).collect(Collectors.toList());
        return idList;
    }
}
    
    