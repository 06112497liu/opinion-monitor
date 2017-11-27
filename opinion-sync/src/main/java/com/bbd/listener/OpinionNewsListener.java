/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.vo.OpinionNewsVO;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    public void Listen(List<ConsumerRecord<String, String>> records) {
        processRecords(records);
    }

    @KafkaListener(topics = "bbd_opinion_news", containerFactory = "kafkaListenerContainerFactory")
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

        long end = System.currentTimeMillis();
        logger.info("Sync opinion {} news, time used: {}", records.size(), (end - start));
    }
}
