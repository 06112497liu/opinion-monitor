

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

import com.alibaba.fastjson.JSON;
import com.bbd.dao.MsgSendRecordDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventLevelChangeDao;
import com.bbd.dao.WarnNotifierDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.MsgSendRecord;
import com.bbd.domain.MsgSendRecordExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventLevelChange;
import com.bbd.domain.OpinionEventLevelChangeExample;
import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnNotifierExample;
import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.bbd.job.vo.EventMsgModel;
import com.bbd.job.vo.MsgVO;
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;
import com.bbd.util.JsonUtil;

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
    WarnNotifierDao warnNotifierDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private EventService eventService;
   
    @Autowired  
    private KafkaTemplate<Integer, String> kafkaTemplate;  
    
    public WarnSetting getLevel(OpinionEvent e){
        WarnSettingExample warnSettingExample = new WarnSettingExample();
        warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(2).andTargetTypeEqualTo(2)
        .andMinGreaterThanOrEqualTo(e.getHot()).andMaxGreaterThanOrEqualTo(e.getHot());
        List<WarnSetting> eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        warnSettingExample.clear();
        if (eventWarnList == null || eventWarnList.size() == 0){
            return new WarnSetting();
        } else {
            return  eventWarnList.get(0);
        }
    }
    
    public boolean changeLevel(OpinionEvent e, int level, Date now) {
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
            return true;
        } 
        opinionEventLevelChange = opinionEventLevelChangeList.get(0);
        if (level != opinionEventLevelChange.getLevel()) {
            opinionEventLevelChange.setLevel((byte)level);
            opinionEventLevelChange.setGmtLevelChange(now);
            opinionEventLevelChange.setGmtModified(now);
            opinionEventLevelChangeDao.updateByPrimaryKeySelective(opinionEventLevelChange);
            return true;
        }
        return false;
    }
    
    public List<WarnNotifier> getNotifiers (WarnSetting warnSetting) {
        WarnNotifierExample example = new WarnNotifierExample();
        example.createCriteria().andSettingIdEqualTo(warnSetting.getId());
        return warnNotifierDao.selectByExample(example);
    }
    
    public List<MsgVO> buildMsg(OpinionEvent e, Integer level, List<WarnNotifier> warnNotifierList, boolean isEmail) {
        List<MsgVO> msgVOList = new ArrayList<MsgVO>();
        for (WarnNotifier warnNotifier : warnNotifierList) {
            if (isEmail == true && warnNotifier.getEmailNotify() == 1) {
                MsgVO msgVO = new MsgVO();
                EventMsgModel eventMsgModel = new EventMsgModel();
                eventMsgModel.setEvent(e.getEventName());
                eventMsgModel.setLevel(String.valueOf(level));
                eventMsgModel.setScore(e.getHot());
                eventMsgModel.setLink("XXXXXXXX（系统PC端舆情事件信息列表地址，登录后直接跳转到列表页");
                eventMsgModel.setUsername(warnNotifier.getNotifier());
                msgVO.setModel(eventMsgModel);
                msgVO.setRetry(3);
                msgVO.setSubject("事件总体热度预警");
                msgVO.setTo(warnNotifier.getEmail());
                msgVO.setTemplate("event_overall_heatLevel");
                msgVOList.add(msgVO);
            }
        }
        return msgVOList;
    }
    
    public void updateMsgRecord(int sendType, int msgType) {
        MsgSendRecordExample msgExample = new MsgSendRecordExample();
        msgExample.createCriteria().andSendTypeEqualTo(sendType).andMsgTypeEqualTo(msgType);
        List<MsgSendRecord> mgsList = msgSendRecordDao.selectByExample(msgExample);
        if (mgsList != null && mgsList.size() > 0) {
            mgsList.get(0).setSendTime(new Date());
            msgSendRecordDao.updateByPrimaryKeySelective(mgsList.get(0));
        } else {
            MsgSendRecord record = new MsgSendRecord();
            record.setSendType(sendType);
            record.setMsgType(msgType);
            record.setGmtCreate(new Date());
            record.setSendTime(new Date());
            msgSendRecordDao.insert(record);
        }
    }
    
    @Scheduled(cron="0 0 * * * ?")
    public void eventKafka() {
        
        //事件总体热度级别变化发送至kafka
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        List<OpinionEvent> opinionEventList = opinionEventDao.selectByExample(example);
        Integer level;
        Date now = new Date();
        for (OpinionEvent e : opinionEventList) {
            WarnSetting warnSetting = getLevel(e);
            level = warnSetting.getLevel();
            if (level == null) {
                continue;
            }
            boolean change = changeLevel(e, level, now);
            if (change == true) {
                List<WarnNotifier> warnNotifierList= getNotifiers(warnSetting);
                kafkaTemplate.sendDefault(JsonUtil.fromJson(buildMsg(e, level, warnNotifierList, true)));//发送邮件
                //发送
                //
            }
        }
        updateMsgRecord(3, 2); 
    }
    
    

}

