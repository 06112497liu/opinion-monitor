

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.job.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bbd.bean.EventEsVO;
import com.bbd.bean.OpinionEsVO;
import com.bbd.dao.MsgSendRecordDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventLevelChangeDao;
import com.bbd.dao.OpinionEventLevelRecordDao;
import com.bbd.dao.OpinionPopDao;
import com.bbd.dao.WarnNotifierDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.MsgSendRecord;
import com.bbd.domain.MsgSendRecordExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventLevelChange;
import com.bbd.domain.OpinionEventLevelChangeExample;
import com.bbd.domain.OpinionEventLevelRecord;
import com.bbd.domain.OpinionEventLevelRecordExample;
import com.bbd.domain.OpinionPop;
import com.bbd.domain.OpinionPopExample;
import com.bbd.domain.PopMsg;
import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnNotifierExample;
import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.bbd.job.vo.EmailContent;
import com.bbd.job.vo.EventIncMsgModel;
import com.bbd.job.vo.EventMsgModel;
import com.bbd.job.vo.MsgVO;
import com.bbd.job.vo.SMSContent;
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;
import com.bbd.service.OpinionService;
import com.bbd.service.vo.OpinionMsgSend;
import com.bbd.util.JsonUtil;

/** 
 * @author daijinlong 
 * @version $Id: EventStatisticService.java, v 0.1 2017年11月14日 上午10:23:47 daijinlong Exp $ 
 */
//@Service
public class MsgService {
    private static final Logger  logger = LoggerFactory.getLogger(MsgService.class);
    
    private static final int SEND_TYPE_OPINION = 1;
    private static final int SEND_TYPE_EVENT_NEW = 2;
    private static final int SEND_TYPE_EVENT_HOT = 3;
    private static final int MSG_TYPE_SMS = 1;
    private static final int MSG_TYPE_EMAIL = 2;
    
    private static final int WARN_EVENT_TYPE_NEW = 1;
    private static final int WARN_EVENT_TYPE_WHOLE = 2;
    private static final int WARN_EVENT_TARGET_TYPE = 2;
    @Value("#{propertiesConfig[address]}")
    private String address;
    @Autowired
    OpinionEventDao opinionEventDao;
    @Autowired
    WarnSettingDao warnSettingDao;
    @Autowired
    OpinionEventLevelChangeDao opinionEventLevelChangeDao;
    @Autowired
    OpinionEventLevelRecordDao opinionEventLevelRecordDao;
    @Autowired
    MsgSendRecordDao msgSendRecordDao;
    @Autowired
    WarnNotifierDao warnNotifierDao;
    @Autowired
    OpinionPopDao opinionPopDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private EventService eventService;
    @Autowired
    private OpinionService opinionService;
    @Autowired  
    private KafkaTemplate<Integer, String> kafkaTemplate;  
    
    
    /** 获取事件预警设置 
     * @param e
     * @param type
     * @return 
     */
    public WarnSetting getLevel(OpinionEvent e, int type){
        WarnSettingExample warnSettingExample = new WarnSettingExample();
        List<WarnSetting> eventWarnList = null;
        if (type == WARN_EVENT_TYPE_NEW) {//事件新增
            warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(WARN_EVENT_TYPE_NEW).andTargetTypeEqualTo(WARN_EVENT_TARGET_TYPE);
             eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        } else if (type == WARN_EVENT_TYPE_WHOLE){//事件总体
            if (e.getHot() == null) {
                return null;
            }
            warnSettingExample.createCriteria().andEventIdEqualTo(e.getId()).andTypeEqualTo(WARN_EVENT_TYPE_WHOLE).andTargetTypeEqualTo(WARN_EVENT_TARGET_TYPE)
            .andMinLessThanOrEqualTo(e.getHot()).andMaxGreaterThanOrEqualTo(e.getHot());
             eventWarnList = warnSettingDao.selectByExample(warnSettingExample);
        } else {
            return null;
        }
        warnSettingExample.clear();
        if (eventWarnList == null || eventWarnList.size() == 0){
            return null;
        } else {
            return  eventWarnList.get(0);
        }
       
    }
    
    
    /**判断事件总体热度级别是否改变  
     * @param e
     * @param level
     * @param now
     * @return 
     */
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
    
    /**判断事件总体热度级别是否改变  
     * @param e
     * @param level
     * @param now
     * @return 
     */
    public boolean changeLevelRecord(OpinionEvent e, int level, Date now) {
        OpinionEventLevelRecordExample  levelRecordExample = new OpinionEventLevelRecordExample();
        levelRecordExample.createCriteria().andEventIdEqualTo(e.getId());
        List<OpinionEventLevelRecord> opinionEventLevelRecordList = opinionEventLevelRecordDao.selectByExample(levelRecordExample);
        levelRecordExample.clear();
        
        OpinionEventLevelRecord opinionEventLevelRecord;
        if (opinionEventLevelRecordList == null || opinionEventLevelRecordList.size() == 0) {
            opinionEventLevelRecord = new OpinionEventLevelRecord();
            opinionEventLevelRecord.setEventId(e.getId());
            opinionEventLevelRecord.setLevel((byte)level);
            opinionEventLevelRecord.setHot(e.getHot());
            opinionEventLevelRecord.setGmtPick(now);
            opinionEventLevelRecord.setGmtCreate(now);
            opinionEventLevelRecordDao.insert(opinionEventLevelRecord);
            return true;
        } 
        opinionEventLevelRecord = opinionEventLevelRecordList.get(opinionEventLevelRecordList.size() - 1);
        if (level != opinionEventLevelRecord.getLevel()) {
            opinionEventLevelRecord = new OpinionEventLevelRecord();
            opinionEventLevelRecord.setEventId(e.getId());
            opinionEventLevelRecord.setLevel((byte)level);
            opinionEventLevelRecord.setHot(e.getHot());
            opinionEventLevelRecord.setGmtPick(now);
            opinionEventLevelRecord.setGmtCreate(now);
            opinionEventLevelRecordDao.insert(opinionEventLevelRecord);
            return true;
        }
        return false;
    }
    
    
    /**获取通知人  
     * @param warnSetting
     * @return 
     */
    public List<WarnNotifier> getNotifiers (WarnSetting warnSetting) {
        WarnNotifierExample example = new WarnNotifierExample();
        example.createCriteria().andSettingIdEqualTo(warnSetting.getId());
        return warnNotifierDao.selectByExample(example);
    }
    
    
    /**构造事件总体热度消息  
     * @param e
     * @param level
     * @param warnNotifierList
     * @param isEmail
     * @return 
     */
    public List<MsgVO> buildEventWholeMsg(OpinionEvent e, Integer level, List<WarnNotifier> warnNotifierList, boolean isEmail) {
        List<MsgVO> msgVOList = new ArrayList<MsgVO>();
        for (WarnNotifier warnNotifier : warnNotifierList) {
            if (warnNotifier.getEmailNotify() == 0 && warnNotifier.getSmsNotify() == 0) {
                continue;
            }
            MsgVO msgVO = new MsgVO();
            EventMsgModel eventMsgModel = new EventMsgModel();
            eventMsgModel.setEvent(e.getEventName());
            eventMsgModel.setLevel(String.valueOf(level));
            eventMsgModel.setScore(String.valueOf(e.getHot()));
            
            eventMsgModel.setUsername(warnNotifier.getNotifier());
            if (isEmail == true && warnNotifier.getEmailNotify() == 1) {
                eventMsgModel.setLink(address + "/monitor?id=" + e.getId());
                EmailContent content = new EmailContent();
                content.setModel(eventMsgModel);
                content.setRetry(3);
                content.setSubject("事件总体热度预警");
                content.setTo(warnNotifier.getEmail());
                content.setTemplate("event_overall_heatLevel");
                msgVO.setContent(content);
                msgVO.setType("email");
            } else if (isEmail == false && warnNotifier.getSmsNotify() == 1){
                //eventMsgModel.setLink("XXXXXXXX（系统移动端舆情事件信息列表地址，登录后直接跳转到列表页）");
                SMSContent content = new SMSContent();
                content.setModel(eventMsgModel);
                content.setTel(warnNotifier.getPhone());
                content.setTemplateCode("SMS_113461205");
                msgVO.setContent(content);
                msgVO.setType("sms");
            }
            msgVOList.add(msgVO);
        }
        return msgVOList;
    }
    
    
    /**构造事件新增舆情消息    
     * @param e
     * @param count
     * @param hot
     * @param warnNotifierList
     * @param isEmail
     * @return 
     */
    public List<MsgVO> buildEventNewMsg(OpinionEvent e, Integer count, Integer hot, List<WarnNotifier> warnNotifierList, boolean isEmail) {
        List<MsgVO> msgVOList = new ArrayList<MsgVO>();
        for (WarnNotifier warnNotifier : warnNotifierList) {
            if (warnNotifier.getEmailNotify() == 0 && warnNotifier.getSmsNotify() == 0) {
                continue;
            }
            MsgVO msgVO = new MsgVO();
            EventIncMsgModel eventIncMsgModel = new EventIncMsgModel();
            eventIncMsgModel.setEvent(e.getEventName());
            eventIncMsgModel.setCount(String.valueOf(count));
            eventIncMsgModel.setScore(String.valueOf(hot));
            eventIncMsgModel.setUsername(warnNotifier.getNotifier());
            if (isEmail == true && warnNotifier.getEmailNotify() == 1) {
                EmailContent content = new EmailContent();
                eventIncMsgModel.setLink(address + "/monitor?id=" + e.getId());
                content.setModel(eventIncMsgModel);
                content.setRetry(3);
                content.setSubject("事件新增观点预警");
                content.setTo(warnNotifier.getEmail());
                content.setTemplate("event_opinion_warnning");
                msgVO.setContent(content);
                msgVO.setType("email");
            } else if (isEmail == false && warnNotifier.getSmsNotify() == 1){
                //eventIncMsgModel.setLink("XXXXXXXX（系统移动端舆情事件信息列表地址，登录后直接跳转到列表页）");
                SMSContent content = new SMSContent();
                content.setModel(eventIncMsgModel);
                content.setTel(warnNotifier.getPhone());
                content.setTemplateCode("SMS_113456211");
                msgVO.setContent(content);
                msgVO.setType("sms");
            }
            msgVOList.add(msgVO);
        }
        return msgVOList;
    }
    
    
    /**更新消息发送记录  
     * @param sendType
     * @param msgType
     * @param sendTime 
     */
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
    
    
    /**获取消息记录  
     * @param sendType
     * @param msgType
     * @return 
     */
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
    
    /**获取正在监测的事件列表  
     * @return 
     */
    public List<OpinionEvent> getEventList() {
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        return opinionEventDao.selectByExample(example);
    }
    
    /**事件新增舆情定时任务   
     */
    //@Scheduled(cron="0 30 * * * ?")
    public void eventNewOpinionKafka(){
        //事件热点舆情变化发送至kafka
        List<OpinionEvent> opinionEventList = getEventList();
        MsgSendRecord msgSendRecord = getMsgSendRecord(SEND_TYPE_EVENT_NEW, MSG_TYPE_EMAIL);
        DateTime startTime = null;
        if (msgSendRecord != null) {
            startTime = new DateTime(msgSendRecord.getSendTime());
        }
        DateTime endTime = new DateTime();
        for (OpinionEvent e : opinionEventList) {
            List<EventEsVO> evtList = esQueryService.queryEventNewInfoTotal(e, startTime, endTime);
            if (evtList ==null || evtList.size() == 0 ) {
                continue;
            }
            OpinionEsVO op = esQueryService.getMaxOpinionByUUIDs(buildUids(evtList));
            WarnSetting warnSetting = getLevel(e, WARN_EVENT_TYPE_NEW);
            if (warnSetting == null) {
                continue;
            }
            List<WarnNotifier> warnNotifierList= getNotifiers(warnSetting);
            for (MsgVO msgVO : buildEventNewMsg(e, evtList.size(), op.getHot(), warnNotifierList, true)) {
                kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送邮件
                logger.info("发送邮件事件新增舆情成功: {}", JsonUtil.fromJson(msgVO));
            }
            for (MsgVO msgVO : buildEventNewMsg(e, evtList.size(), op.getHot(), warnNotifierList, false)) {
                kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送短信
                logger.info("发送短信事件新增舆情成功: {}", JsonUtil.fromJson(msgVO));
            }
        }
        //由于同一批短信和邮件发送send_time相同，故bbd_msg_send_record区分msg_type没有意义，用同一条记录即可
        updateMsgRecord(SEND_TYPE_EVENT_NEW, MSG_TYPE_EMAIL, endTime.toDate()); 
    }
    
    
    List<String> buildUids(List<EventEsVO> evtList) {
        List<String> uids = new ArrayList<String>();
        for (EventEsVO  e : evtList) {
            uids.add(e.getOpinionId());
        }
        return uids;
    }
    
    
    /**舆情定时任务  
     * @throws NoSuchFieldException 
     */
    //@Scheduled(cron="0 30 * * * ?")
    public void opinionKafka() throws NoSuchFieldException {
        MsgSendRecord msgSendRecord = getMsgSendRecord(SEND_TYPE_OPINION, MSG_TYPE_EMAIL);
        OpinionMsgSend opinionMsgSend = opinionService.getWarnRemindJson(msgSendRecord != null ? new DateTime(msgSendRecord.getSendTime()) : null);
        for (MsgVO msgVO : opinionMsgSend.getSendMsg()) {
            kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送邮件和短信
            logger.info("发送邮件或者短信舆情成功: {}", JsonUtil.fromJson(msgVO));
        }
        updateMsgRecord(SEND_TYPE_OPINION, MSG_TYPE_EMAIL, opinionMsgSend.getClaTime()); 
    }
    
    
    /**事件总体热度级别变化定任务   
     */
    //@Scheduled(cron="0 30 * * * ?")
    public void eventWholeHotKafka() {
        //事件总体热度级别变化发送至kafka
        List<OpinionEvent> opinionEventList = getEventList();
        Integer level;
        Date now = new Date();
        for (OpinionEvent e : opinionEventList) {
            WarnSetting warnSetting = getLevel(e, WARN_EVENT_TYPE_WHOLE);
            if (warnSetting == null) {
                continue;
            }
            level = warnSetting.getLevel();
            if (level == null) {
                continue;
            }
            boolean change = changeLevel(e, level, now);
            if (change == true) {
                List<WarnNotifier> warnNotifierList= getNotifiers(warnSetting);
                for (MsgVO msgVO : buildEventWholeMsg(e, level, warnNotifierList, true)) {
                    kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送邮件
                    logger.info("发送邮件事件总体热度级别变化成功: {}", JsonUtil.fromJson(msgVO));
                }
                for (MsgVO msgVO : buildEventWholeMsg(e, level, warnNotifierList, false)) {
                    kafkaTemplate.sendDefault(JsonUtil.fromJson(msgVO));//发送短信
                    logger.info("发送短信事件总体热度级别变化成功: {}", JsonUtil.fromJson(msgVO));
                }
            }
        }
        //由于同一批短信和邮件发送send_time相同，故bbd_msg_send_record区分msg_type没有意义，用同一条记录即可
        updateMsgRecord(SEND_TYPE_EVENT_HOT, MSG_TYPE_EMAIL, now);
    }
    
    /**事件总体热度级别变化定任务--弹窗   
     */
    //@Scheduled(cron="0 0/10 * * * ?")
    public void eventLevelRecordPop() {
        List<OpinionEvent> opinionEventList = getEventList();
        Integer level;
        Date now = new Date();
        for (OpinionEvent e : opinionEventList) {
            WarnSetting warnSetting = getLevel(e, WARN_EVENT_TYPE_WHOLE);
            if (warnSetting == null) {
                continue;
            }
            level = warnSetting.getLevel();
            if (level == null) {
                continue;
            }
            boolean change = changeLevelRecord(e, level, now);
        }
    }
    
    public List<PopMsg> getPop(Long userId, Integer type){
        OpinionPopExample popEx = new OpinionPopExample();
        popEx.createCriteria().andUserIdEqualTo(userId).andTypeEqualTo((byte)type.intValue());
        List<OpinionPop> opinionPopList = opinionPopDao.selectByExample(popEx);
        OpinionEventLevelRecordExample recordEx = new  OpinionEventLevelRecordExample();
        recordEx.setOrderByClause("event_id DESC, gmt_pick ASC");
        
        List<PopMsg> popMsgs = null;
        OpinionPop record = new OpinionPop();
        Date now = new Date();
        if (opinionPopList != null && opinionPopList.size() > 0) {
            List<OpinionEventLevelRecord> opinionEventLevelRecordList = opinionEventLevelRecordDao.selectByExample(recordEx);
            popMsgs = buildPopMsg(opinionEventLevelRecordList);
            record.setId(opinionPopList.get(0).getId());
            record.setGmtPopLatest(now);
            record.setGmtModified(now);
            opinionPopDao.updateByPrimaryKeySelective(record);
        } else {
            recordEx.createCriteria().andGmtPickGreaterThan(opinionPopList.get(0).getGmtPopLatest());
            List<OpinionEventLevelRecord> opinionEventLevelRecordList = opinionEventLevelRecordDao.selectByExample(recordEx);
            popMsgs = buildPopMsg(opinionEventLevelRecordList);
            record.setUserId(userId);
            record.setType((byte)type.intValue());
            record.setGmtPopLatest(now);
            record.setGmtCreate(now);
            opinionPopDao.insert(record);
        }
        return popMsgs;
    }
    
    public List<PopMsg> buildPopMsg(List<OpinionEventLevelRecord> opinionEventLevelRecordList) {
        List<Long> ids = new ArrayList<Long>();
        List<PopMsg> popMsgs = new ArrayList<PopMsg>(); 
        for (OpinionEventLevelRecord record : opinionEventLevelRecordList) {
            ids.add(record.getEventId());
        }
        List<OpinionEvent> eventList = eventService.getEventList(ids);
        for (OpinionEventLevelRecord record : opinionEventLevelRecordList) {
            String msg = "";
            for (OpinionEvent evt : eventList) {
                if (record.getEventId() == evt.getId()) {
                    msg = "通知：事件"+getLevel(record.getLevel())+"级预警：“"+evt.getEventName()+"”"+"事件总热度已达"+record.getHot();
                    PopMsg popMsg = new PopMsg();
                    popMsg.setMsg(msg);
                    popMsg.setUrl(address + "/monitor?id=" + evt.getId());
                    popMsgs.add(popMsg);
                    break;
                }
            }
            
        }
        return popMsgs;
    }
    
    public String getLevel(byte level) {
        if (level == 1) {
            return "一";
        }else if (level == 2) {
            return "二";
        }else if (level == 3) {
            return "三";
        }
        return "";
    }
    
    
}

