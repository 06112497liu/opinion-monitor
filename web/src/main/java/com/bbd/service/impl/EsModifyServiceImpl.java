package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.constant.EsConstant;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.exception.ErrorCode;
import com.bbd.service.EsModifyService;
import com.bbd.service.EsQueryService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.vo.UserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.VersionConflictEngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Liuweibo
 * @version Id: EsModifyServiceImpl.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
@Service
public class EsModifyServiceImpl implements EsModifyService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private EsUtil         esUtil;

    @Autowired
    private SystemSettingService settingService;

    /**
     * 转发舆情
     * @param operator
     * @param uuid
     * @param fieldMap
     */
    @Override
    public void updateOpinion(UserInfo operator, Long targetId, String uuid, Map<String, Object> fieldMap) throws IOException, ExecutionException, InterruptedException {

        OpinionEsVO opinion = esQueryService.getOpinionByUUID(uuid);
        // 保留解除时的预警等级
        Integer hot = opinion.getHot();
        Integer level = settingService.judgeOpinionSettingClass(hot, settingService.queryWarnSetting(3));
        fieldMap.put(EsConstant.levelField, level);

        // 记录参与人
            // 操作人
        Long operatorId = operator.getId();
        Long[] original = opinion.getOperators();
        Long[] tempArr = getOperators(original, operatorId);
            // 被操作人
        Long[] newArr = getOperators(tempArr, targetId);

        fieldMap.put(EsConstant.operatorsField, newArr);

        TransportClient client = esUtil.getClient();
        UpdateRequest request = new UpdateRequest();
        request.index(EsConstant.IDX_OPINION);
        request.type(EsConstant.OPINION_TYPE);
        request.id(uuid);
        request.doc(buildXContentBuilder(fieldMap));
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE); // 被搜索时可见
        request.detectNoop(false); // 即时修改和上次相同，也增加版本号

        // 设置版本号，是这个版本号，才更新字段，否则报错
        try {
            client.update(request).get();
        } catch (VersionConflictEngineException e) {
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "修改冲突");
        }
    }

    // 记录参与人
    private Long[] getOperators(Long[] original, Long userid) {
        boolean flag = ArrayUtils.contains(original, userid);
        Long[] newArr = original;
        if (!flag && userid != -1L) {
            newArr = ArrayUtils.add(original, userid);
        }
        return newArr;
    }

    private XContentBuilder buildXContentBuilder(Map<String, Object> map) throws IOException {
        XContentBuilder result = XContentFactory.jsonBuilder().startObject();
        if (map != null) {
            for (String key : map.keySet()) {
                result.field(key, map.get(key));
            }
        }
        result.endObject();
        return result;
    }

    /**
     * 添加转发记录
     * @param recordVO
     */
    @Override
    public void recordOpinionOp(OpinionOpRecordVO recordVO) {

        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareIndex(EsConstant.IDX_OPINION_OP_RECORD, EsConstant.OPINION_OP_RECORD_TYPE).setSource(JsonUtil.fromJson(recordVO), XContentType.JSON));
        // 插入数据
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL).execute().actionGet();
    }

}
