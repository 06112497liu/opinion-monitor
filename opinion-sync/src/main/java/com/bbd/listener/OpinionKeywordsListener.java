/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.alibaba.fastjson.JSONObject;
import com.bbd.dao.KeywordStatisticsDao;
import com.bbd.dao.KeywordStatisticsExtDao;
import com.bbd.domain.KeywordStatistics;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author tjwang
 * @version $Id: OpinionKeywordsListener.java, v 0.1 2017/11/29 0029 16:56 tjwang Exp $
 */
@Component
public class OpinionKeywordsListener {

    @Autowired
    private KeywordStatisticsExtDao keywordStatisticsExtDao;

    @KafkaListener(topics = "bbd_opinion_keywords", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        processRecords(records);
    }

    @Transactional(rollbackFor = Exception.class)
    public void processRecords(List<ConsumerRecord<String, String>> records) {
        Splitter spli1 = Splitter.on("#").omitEmptyStrings().trimResults();
        Splitter spli2 = Splitter.on(",").omitEmptyStrings().trimResults();
        for (ConsumerRecord c : records) {
            String s = (String) c.value();
            JSONObject obj = JSONObject.parseObject(s);
            String value = obj.getString("words");
            Iterable<String> it = spli1.split(value);
            List<String> list = Lists.newArrayList(it);
            List<KeywordStatistics> result = Lists.newArrayList();
            for(String str : list) {
                List<String> keywords = Lists.newArrayList(spli2.split(str));
                KeywordStatistics vo = new KeywordStatistics();
                vo.setKeyword(keywords.get(0));
                vo.setCount(Long.parseLong(keywords.get(1)));
                result.add(vo);
            }
            keywordStatisticsExtDao.batchDel();
            keywordStatisticsExtDao.batchInsert(result);
        }
    }
}
