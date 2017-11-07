package com.bbd.service.impl;

import com.bbd.constant.EsConstant;
import com.bbd.service.EsModifyService;
import com.bbd.service.EsQueryService;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.bbd.vo.UserInfo;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        Long[] operators = original;
        if(original == null) {
            operators = new Long[]{operatorId};
        } else {
            List<Long> operatorsList = Arrays.asList(original);
            if(!operatorsList.contains(operator.getId())) {
                operators = Arrays.copyOf(original, original.length+1);
                operators[operators.length-1] = operatorId;
            }
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
                        .field(operatorsField, operators)
                        .field(transferTypeField, param.getTransferType())
                .endObject()
        );
        UpdateResponse resp = client.update(request).get();
        System.out.println(resp);
    }
}
    
    