/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: OpinionKeywordsListener.java, v 0.1 2017/11/29 0029 16:56 tjwang Exp $
 */
@Component
public class OpinionKeywordsListener {

    @KafkaListener(topics = "bbd_opinion_keywords", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        processRecords(records);
    }

    private void processRecords(List<ConsumerRecord<String, String>> records) {

    }

}
