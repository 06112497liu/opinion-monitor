/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.bbd.constant.EsConstant;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.vo.OpinionNewsVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 舆情新闻Listener
 * 
 * @author tjwang
 * @version $Id: OpinionNewsListener.java, v 0.1 2017/11/27 0027 10:38 tjwang Exp $
 */
@Component
public class OpinionNewsListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EsUtil esUtil;

    @KafkaListener(topics = "bbd_opinion_news", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        processRecords(records);
    }

    private void processRecords(List<ConsumerRecord<String, String>> records) {
        if (records.size() == 0) {
            return;
        }
        long start = System.currentTimeMillis();

        List<OpinionNewsVO> vos = Lists.newArrayList();
        for (ConsumerRecord<String, String> record : records) {
            logger.debug("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            OpinionNewsVO vo = JsonUtil.parseObject(record.value(), OpinionNewsVO.class);
            vos.add(vo);
        }

        syncData(vos);

        long end = System.currentTimeMillis();
        logger.info("Sync opinion {} news, time used: {}", records.size(), (end - start));
    }

    /**
     * 同步数据
     * @param vos
     */
    private void syncData(List<OpinionNewsVO> vos) {
        TransportClient client = esUtil.getClient();

        String index = EsConstant.IDX_OPINION_SIMILAR_NEWS;
        String type = EsConstant.OPINION_SIMILAR_NEWS_TYPE;

        BulkRequestBuilder bulk = client.prepareBulk();
        for (OpinionNewsVO vo : vos) {
            String id = vo.getId();
            IndexRequest ir = new IndexRequest(index, type, id);
            ir.source(JsonUtil.fromJson(vo), XContentType.JSON);
            UpdateRequest ur = new UpdateRequest(index, type, id).upsert(ir);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx.op = 'none'", Maps.newHashMap());
            ur.script(script);
            bulk.add(ur);
        }

        BulkResponse resp = bulk.get();
        System.out.println("Sync opinion news data has failure: " + resp.hasFailures());
    }
}
