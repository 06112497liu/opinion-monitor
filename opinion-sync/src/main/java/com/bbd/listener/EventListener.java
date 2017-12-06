/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.alibaba.fastjson.JSON;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventWordsDao;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventWords;
import com.bbd.domain.OpinionEventWordsExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/** 
 * @author daijinlong 
 * @version $Id: EventService.java, v 0.1 2017年11月10日 下午2:06:26 daijinlong Exp $ 
 */
@Component
public class EventListener {

    private static final Logger  logger = LoggerFactory.getLogger(EventListener.class);

    @Autowired
    private OpinionEventDao      opinionEventDao;
    @Autowired
    private OpinionEventWordsDao opinionEventWordsDao;

    @KafkaListener(topics = "bbd_event_words", containerFactory = "kafkaListenerContainerFactory")
    public void ListenWords(List<String> records) {
        long start = System.currentTimeMillis();
        List<OpinionEventWords> opinionEventWordList = JSON.parseArray(records.toString(), OpinionEventWords.class);
        for (OpinionEventWords opinionEventWord : opinionEventWordList) {
            OpinionEventWordsExample example = new OpinionEventWordsExample();
            example.createCriteria().andEventIdEqualTo(opinionEventWord.getEventId()).andCycleEqualTo(opinionEventWord.getCycle());
            List<OpinionEventWords> opinionEventWordsList = opinionEventWordsDao.selectByExample(example);
            if (opinionEventWordsList != null && opinionEventWordsList.size() > 0) {
                opinionEventWord.setGmtModified(new Date());
                opinionEventWordsDao.updateByPrimaryKeySelective(opinionEventWord);
            } else {
                opinionEventWord.setGmtCreate(new Date());
                opinionEventWordsDao.insert(opinionEventWord);
            }
        }
        long end = System.currentTimeMillis();
        logger.info("Process {} success, time used: {}", records.toString(), end - start);
    }

    @KafkaListener(topics = "bbd_event_hot", containerFactory = "kafkaListenerContainerFactory")
    public void ListenHot(List<String> records) {
        long start = System.currentTimeMillis();
        List<OpinionEvent> opinionEventList = JSON.parseArray(records.toString(), OpinionEvent.class);
        for (OpinionEvent opinionEvent : opinionEventList) {
            opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
        }
        long end = System.currentTimeMillis();
        logger.info("Process {}  success, time used: {}", records.toString(), end - start);
    }
}
