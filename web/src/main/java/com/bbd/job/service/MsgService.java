

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

import com.bbd.bean.EventEsVO;
import com.bbd.bean.OpinionEsVO;
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
import com.bbd.job.vo.EventIncMsgModel;
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
    private static final int SEND_TYPE_OPINION = 1;
    private static final int SEND_TYPE_EVENT_NEW = 2;
    private static final int SEND_TYPE_EVENT_HOT = 3;
    private static final int MSG_TYPE_SMS = 1;
    private static final int MSG_TYPE_EMAIL = 2;
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
    
    public WarnSetting getLevel(OpinionEvent e, int type){
        WarnSettingExample warnSettingExample = new WarnSettingExample();
        List<WarnSetting> eventWarnList = null;
        if (type == 1) {
            warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(1).andTargetTypeEqualTo(2);
             eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        } else {
            warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(2).andTargetTypeEqualTo(2)
            .andMinGreaterThanOrEqualTo(e.getHot()).andMaxGreaterThanOrEqualTo(e.getHot());
             eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        }
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
    
    public List<MsgVO> buildEventWholeMsg(OpinionEvent e, Integer level, List<WarnNotifier> warnNotifierList, boolean isEmail) {
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
    
    public List<MsgVO> buildEventNewMsg(OpinionEvent e, Integer count, Integer hot, List<WarnNotifier> warnNotifierList, boolean isEmail) {
        List<MsgVO> msgVOList = new ArrayList<MsgVO>();
        for (WarnNotifier warnNotifier : warnNotifierList) {
            if (isEmail == true && warnNotifier.getEmailNotify() == 1) {
                MsgVO msgVO = new MsgVO();
                EventIncMsgModel eventIncMsgModel = new EventIncMsgModel();
                eventIncMsgModel.setEvent(e.getEventName());
                eventIncMsgModel.setCount(String.valueOf(count));
                eventIncMsgModel.setScore(hot);
                eventIncMsgModel.setLink("XXXXXXXX（系统PC端舆情事件信息列表地址，登录后直接跳转到列表页）");
                eventIncMsgModel.setUsername(warnNotifier.getNotifier());
                msgVO.setModel(eventIncMsgModel);
                msgVO.setRetry(3);
                msgVO.setSubject("事件新增观点预警");
                msgVO.setTo(warnNotifier.getEmail());
                msgVO.setTemplate("event_opinion_warnning");
                msgVOList.add(msgVO);
            }
        }
        return msgVOList;
    }
    
    public void updateMsgRecord(int sendType, int msgType, Date sendTime) {
        MsgSendRecord msgSendRecord = getMsgSendRecord(sendType, msgType); 
        if (msgSendRecord != null ) {
            msgSendRecord.setSendTime(sendTime);
            msgSendRecordDao.updateByPrimaryKeySelective(msgSendRecord);
        } else {
            MsgSendRecord record = new MsgSendRecord();
            record.setSendType(sendType);
            record.setMsgType(msgType);
            record.setGmtCreate(new Date());
            record.setSendTime(sendTime);
            msgSendRecordDao.insert(record);
        }
    }
    
    public MsgSendRecord getMsgSendRecord(int sendType, int msgType){
        MsgSendRecordExample msgExample = new MsgSendRecordExample();
        msgExample.createCriteria().andSendTypeEqualTo(sendType).andMsgTypeEqualTo(msgType);
        List<MsgSendRecord> mgsList = msgSendRecordDao.selectByExample(msgExample);
        if (mgsList != null && mgsList.size() > 0) {
            return mgsList.get(0);
        } else {
            return null;
        }
    }
    
    
    public List<OpinionEvent> getEventList() {
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        return opinionEventDao.selectByExample(example);
    }
    
    @Scheduled(cron="0 0 * * * ?")
    public void eventNewOpinionKafka(){
        //事件热点舆情变化发送至kafka
        List<OpinionEvent> opinionEventList = getEventList();
        MsgSendRecord msgSendRecord = getMsgSendRecord(2, 2);
        DateTime startTime = null;
        if (msgSendRecord != null) {
            startTime = new DateTime(getMsgSendRecord(2, 2).getSendTime());
        }
        DateTime endTime = new DateTime();
        for (OpinionEvent e : opinionEventList) {
            List<EventEsVO> evtList = esQueryService.queryEventNewInfoTotal(e, startTime, endTime);
            OpinionEsVO op = esQueryService.getMaxOpinionByUUIDs(buildUids(evtList));
            WarnSetting warnSetting = getLevel(e, 1);
            List<WarnNotifier> warnNotifierList= getNotifiers(warnSetting);
            for (MsgVO msgVO : buildEventNewMsg(e, evtList.size(), op.getHot(), warnNotifierList, true)) {
                kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送邮件
            }
        }
        updateMsgRecord(SEND_TYPE_EVENT_NEW, MSG_TYPE_EMAIL, endTime.toDate()); 
    }
    
    
    List<String> buildUids(List<EventEsVO> evtList) {
        List<String> uids = new ArrayList<String>();
        for (EventEsVO  e : evtList) {
            uids.add(e.getOpinionId());
        }
        return uids;
    }
    
    @Scheduled(cron="0 0 * * * ?")
    public void opinionKafka() {
        
    }
    
    @Scheduled(cron="0 0 * * * ?")
    public void eventWholeHotKafka() {
        
        //事件总体热度级别变化发送至kafka
        List<OpinionEvent> opinionEventList = getEventList();
        Integer level;
        Date now = new Date();
        for (OpinionEvent e : opinionEventList) {
            WarnSetting warnSetting = getLevel(e, 2);
            level = warnSetting.getLevel();
            if (level == null) {
                continue;
            }
            boolean change = changeLevel(e, level, now);
            if (change == true) {
                List<WarnNotifier> warnNotifierList= getNotifiers(warnSetting);
                for (MsgVO msgVO : buildEventWholeMsg(e, level, warnNotifierList, true)) {
                    kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送邮件
                }
               
                //发送
                //
            }
        }
        updateMsgRecord(SEND_TYPE_EVENT_HOT, MSG_TYPE_EMAIL, now); 
    }
}

