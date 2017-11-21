

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.job.service; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bbd.dao.MsgSendRecordDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventLevelChangeDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.MsgSendRecord;
import com.bbd.domain.MsgSendRecordExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventLevelChange;
import com.bbd.domain.OpinionEventLevelChangeExample;
import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;

/** 
 * @author daijinlong 
 * @version $Id: EventStatisticService.java, v 0.1 2017年11月14日 上午10:23:47 daijinlong Exp $ 
 */
@Service
public class MsgService {
    @Autowired
    OpinionEventDao opinionEventDao;
    @Autowired
    WarnSettingDao warnSettingDao;
    @Autowired
    OpinionEventLevelChangeDao opinionEventLevelChangeDao;
    @Autowired
    MsgSendRecordDao msgSendRecordDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private EventService eventService;
   
    @Autowired  
    private KafkaTemplate<Integer, String> kafkaTemplate;  
    
    public int getLevel(OpinionEvent e){
        WarnSettingExample warnSettingExample = new WarnSettingExample();
        warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(2).andTargetTypeEqualTo(2)
        .andMinGreaterThanOrEqualTo(e.getHot()).andMaxGreaterThanOrEqualTo(e.getHot());
        List<WarnSetting> eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        warnSettingExample.clear();
        if (eventWarnList == null || eventWarnList.size() == 0){
            return 0;
        } else {
            return  eventWarnList.get(0).getLevel();
        }
    }
    
    public void changeLevel(OpinionEvent e, int level, Date now, 
                            List<OpinionEvent> needProEvtList, List<OpinionEventLevelChange> levelChangeList) {
        OpinionEventLevelChangeExample  levelChangeExample = new OpinionEventLevelChangeExample();
        levelChangeExample.createCriteria().andEventIdEqualTo(e.getId());
        List<OpinionEventLevelChange> opinionEventLevelChangeList = opinionEventLevelChangeDao.selectByExample(levelChangeExample);
        levelChangeExample.clear();
        
        OpinionEventLevelChange opinionEventLevelChange;
        if (opinionEventLevelChangeList == null || opinionEventLevelChangeList.size() == 0) {
            opinionEventLevelChange = new OpinionEventLevelChange();
            opinionEventLevelChange.setEventId(e.getId());
            opinionEventLevelChange.setLevel((byte)level);
            opinionEventLevelChange.setGmtLevelChange(now);
            opinionEventLevelChange.setGmtCreate(now);
            opinionEventLevelChangeDao.insert(opinionEventLevelChange);
            needProEvtList.add(e);
            levelChangeList.add(opinionEventLevelChange);
            return;
        } 
        opinionEventLevelChange = opinionEventLevelChangeList.get(0);
        if (level != opinionEventLevelChange.getLevel()) {
            opinionEventLevelChange.setLevel((byte)level);
            opinionEventLevelChange.setGmtLevelChange(now);
            opinionEventLevelChange.setGmtModified(now);
            opinionEventLevelChangeDao.updateByPrimaryKeySelective(opinionEventLevelChange);
            needProEvtList.add(e);
            levelChangeList.add(opinionEventLevelChange);
        }
    }
    
    @Scheduled(cron="0 0 * * * ?")
    public void eventKafka() {
        
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        List<OpinionEvent> opinionEventList = opinionEventDao.selectByExample(example);
        
        int level;
        Date now = new Date();
        List<OpinionEvent> needProEvtList = new ArrayList<OpinionEvent>();
        List<OpinionEventLevelChange> levelChangeList = new ArrayList<OpinionEventLevelChange>();
        
        for (OpinionEvent e : opinionEventList) {
            level = getLevel(e);
            if (level == 0) {
                continue;
            }
            changeLevel(e, level, now, needProEvtList, levelChangeList);
        }
        
        MsgSendRecordExample msgExample = new MsgSendRecordExample();
        msgExample.createCriteria().andSendTypeEqualTo(3).andMsgTypeEqualTo(2);
        List<MsgSendRecord> mgsList = msgSendRecordDao.selectByExample(msgExample);
        
        
        
        
        
        
        if (mgsList == null || mgsList.size() == 0) {
            MsgSendRecord msgSendRecord = new MsgSendRecord();
            msgSendRecord.setSendType(3);
            msgSendRecord.setMsgType(2);
            msgSendRecord.setSendTime(new Date());
            msgSendRecordDao.insert(msgSendRecord);
            kafkaTemplate.sendDefault("haha111");//发送所有事件消息
        } else {//发送级别变化的消息
            OpinionEventLevelChangeExample  levelChangeExample = new OpinionEventLevelChangeExample();
            //List<OpinionEventLevelChange> opinionEventLevelChangeList = opinionEventLevelChangeDao.selectByExample(levelChangeExample);
            levelChangeExample.createCriteria().andGmtLevelChangeGreaterThan(mgsList.get(0).getSendTime());
            List<OpinionEventLevelChange> opinionEventLevelChangeList = opinionEventLevelChangeDao.selectByExample(levelChangeExample);
        }
        if (mgsList == null || mgsList.size() == 0 || 
                new DateTime(mgsList.get(0).getSendTime()).getHourOfDay() ==  new DateTime(now).getHourOfDay() ) {
            kafkaTemplate.sendDefault("haha111");
           // mgsList.get(0).setSendTime(sendTime);
           // msgSendRecordDao.updateByPrimaryKeySelective(record);
        }
        
         
        
        
    }
    

    
    
    

}

