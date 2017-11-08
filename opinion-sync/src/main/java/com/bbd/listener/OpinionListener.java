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
 * @version $Id: OpinionListener.java, v 0.1 2017/11/8 0008 11:38 tjwang Exp $
 */
@Component
public class OpinionListener {

    @KafkaListener(topics = "test", group = "test-group", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
        }
    }

}
