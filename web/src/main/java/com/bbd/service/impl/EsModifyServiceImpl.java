package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsModifyService;
import com.bbd.service.EsQueryService;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.vo.UserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Liuweibo
 * @version Id: EsModifyServiceImpl.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
@Service
public class EsModifyServiceImpl implements EsModifyService {

    @Autowired
    private EsQueryService esQueryService;

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

    /**
     * 转发舆情
     * @param param
     */
    @Override
    public void transferOpinion(UserInfo operator, Long opOwnerId, TransferParam param) throws IOException, ExecutionException, InterruptedException {
        OpinionEsVO opinion = esQueryService.getOpinionByUUID(param.getUuid());
        Long operatorId = operator.getId();

        Long[] original = opinion.getOperators();
        boolean flag = ArrayUtils.contains(original, operatorId);
        Long[] newArr = original;
        if(!flag) {
            newArr = ArrayUtils.add(original, operatorId);
        }

        TransportClient client = EsUtil.getClient();
        UpdateRequest request = new UpdateRequest();
        request.index(EsConstant.IDX_OPINION);
        request.type(EsConstant.OPINION_TYPE);
        request.id(param.getUuid());

        request.doc(
                XContentFactory.jsonBuilder().startObject()
                        .field(opStatusField, 1)
                        .field(opOwnerField, opOwnerId)
                        .field(operatorsField, newArr)
                        .field(transferTypeField, param.getTransferType())
                .endObject()
        );
        client.update(request).get();
    }

    /**
     * 添加转发记录
     * @param recordVO
     */
    @Override
    public void recordTransfer(OpinionOpRecordVO recordVO) {

        TransportClient client = EsUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(
                client.prepareIndex(EsConstant.IDX_OPINION, EsConstant.OPINION_OP_RECORD_TYPE)
                        .setSource(JsonUtil.fromJson(recordVO), XContentType.JSON)
        );
        // 插入数据
        bulkRequest.get();
    }
}
    
    