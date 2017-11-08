package com.bbd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventSourceTrendDao;
import com.bbd.dao.OpinionEventStatisticDao;
import com.bbd.dao.OpinionEventTrendStatisticDao;
import com.bbd.dao.OpinionEventWordsDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.Graph;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventExample.Criteria;
import com.bbd.domain.OpinionEventStatistic;
import com.bbd.domain.OpinionEventStatisticExample;
import com.bbd.domain.OpinionEventTrendStatistic;
import com.bbd.domain.OpinionEventTrendStatisticExample;
import com.bbd.domain.OpinionEventWords;
import com.bbd.domain.OpinionEventWordsExample;
import com.bbd.domain.WarnSetting;
import com.bbd.enums.WebsiteEnum;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.bbd.service.vo.OpinionEsVO;
import com.mybatis.domain.PageBounds;


/** 
 * @author daijinlong 
 * @version $Id: EventService.java, v 0.1 2017年10月25日 下午2:06:26 daijinlong Exp $ 
 */
@Service
public class EventService{

    @Autowired
    OpinionEventDao opinionEventDao;
    @Autowired
    OpinionDictionaryDao opinionDictionaryDao;
    @Autowired
    WarnSettingDao warnSettingDao;
    @Autowired
    OpinionEventSourceTrendDao opinionEventSourceTrendDao;
    @Autowired
    OpinionEventWordsDao opinionEventWordsDao;
    @Autowired
    OpinionEventTrendStatisticDao opinionEventTrendStatisticDao;
    @Autowired
    OpinionEventStatisticDao opinionEventStatisticDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private SystemSettingService systemSettingService;
    /**  
     * 创建事件
     * @param opinionEvent 
     */
    public void createEvent(OpinionEvent opinionEvent) {
        opinionEvent.setIsDelete((byte)0);
        opinionEvent.setGmtCreate(new Date());
        opinionEventDao.insert(opinionEvent);
        
        WarnSetting recordNew = new WarnSetting();
        recordNew.setEventId(opinionEvent.getId());
        recordNew.setType(1);
        recordNew.setTargetType(2);
        recordNew.setPopup(1);
        recordNew.setLevel(1);
        recordNew.setMin(60);
        recordNew.setMax(100);
        recordNew.setName("事件新增观点预警");
        recordNew.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordNew);
        
        WarnSetting recordWhole1 = new WarnSetting();
        recordWhole1.setEventId(opinionEvent.getId());
        recordWhole1.setType(2);
        recordWhole1.setTargetType(2);
        recordWhole1.setPopup(1);
        recordWhole1.setLevel(1);
        recordWhole1.setMin(80);
        recordWhole1.setMax(100);
        recordWhole1.setName("事件总体热度预警");
        recordWhole1.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole1);
        
        WarnSetting recordWhole2 = new WarnSetting();
        recordWhole2.setEventId(opinionEvent.getId());
        recordWhole2.setType(2);
        recordWhole2.setTargetType(2);
        recordWhole2.setPopup(1);
        recordWhole2.setLevel(2);
        recordWhole2.setMin(60);
        recordWhole2.setMax(79);
        recordWhole2.setName("事件总体热度预警");
        recordWhole2.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole2);
        
        WarnSetting recordWhole3 = new WarnSetting();
        recordWhole3.setEventId(opinionEvent.getId());
        recordWhole3.setType(2);
        recordWhole3.setTargetType(2);
        recordWhole3.setPopup(1);
        recordWhole3.setLevel(3);
        recordWhole3.setMin(40);
        recordWhole3.setMax(59);
        recordWhole3.setName("事件总体热度预警");
        recordWhole3.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole3);
    }
    
    
    /**  
     * 获取事件
     * @param id
     * @return 
     */
    public OpinionEvent getEvent(long id) {
        return opinionEventDao.selectByPrimaryKey(id);
    }
    
    /**  
     * 获取事件
     * @param id
     * @return 
     */
    public OpinionEvent getEventChinese(long id) {
        OpinionEvent evt = opinionEventDao.selectByPrimaryKey(id);
        transToChinese(evt);
        return evt;
    }

    
    /**  
     * 修改事件
     * @param opinionEvent 
     */
    public void modifyEvent(OpinionEvent opinionEvent) {
        opinionEvent.setGmtModified(new Date());
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
    }
    
    /**  
     * 归档事件
     * @param opinionEvent 
     */
    public void fileEvent(OpinionEvent opinionEvent) {
        opinionEvent.setGmtFile(new Date());
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
        OpinionEvent tmp =  opinionEventDao.selectByPrimaryKey(opinionEvent.getId());
        DateTime startTime = new DateTime(tmp.getGmtCreate());
        DateTime endTime = DateTime.now();
        DateTime oneYearBefore = endTime.plusYears(-1);
        if (oneYearBefore.getMillis() > startTime.getMillis()) {
            startTime = oneYearBefore;
        }
        List<OpinionEsVO> opinions = new ArrayList<OpinionEsVO>();
        for (int i=1; i<Integer.MAX_VALUE; i++) {
            List<OpinionEsVO> tmpOpinions = esQueryService.queryEventTrendOpinions(tmp.getId(), startTime, endTime, new PageBounds(i,5000)).getOpinions();
            if (tmpOpinions != null && tmpOpinions.size() > 0) {
                opinions.addAll(tmpOpinions);
            } else {
                break;
            }
        }
        
        List<KeyValueVO> mediaSpreadList = esQueryService.getEventOpinionMediaSpread(tmp.getId(), startTime, endTime);
        StringBuilder mediaSpread = new StringBuilder();
        for (KeyValueVO e : mediaSpreadList) {
            mediaSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        List<KeyValueVO> websiteSpreadList = esQueryService.getEventWebsiteSpread(tmp.getId(), startTime, endTime);
        StringBuilder websiteSpread = new StringBuilder();
        for (KeyValueVO e : websiteSpreadList) {
            websiteSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        List<KeyValueVO> emotionSpreadList = esQueryService.getEventEmotionSpread(tmp.getId(), startTime, endTime);
        StringBuilder emotionSpread = new StringBuilder();
        for (KeyValueVO e : emotionSpreadList) {
            emotionSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        
       List<OpinionEventTrendStatistic>  records = new ArrayList<OpinionEventTrendStatistic>();
       for (OpinionEsVO vo : opinions) {
           OpinionEventTrendStatistic evtTrend = new OpinionEventTrendStatistic();
           evtTrend.setEventId(opinionEvent.getId());
           BeanUtils.copyProperties(vo, evtTrend);
           evtTrend.setKeys(StringUtils.join(vo.getKeys(), ","));
           evtTrend.setKeyword(StringUtils.join(vo.getKeyword(), ","));
           evtTrend.setGmtCreate(new Date());
           records.add(evtTrend);
           if (records.size() == 2000) {
               opinionEventTrendStatisticDao.insertBatch(records);
               records.clear();
           }
       }
       if (records.size() > 0) {
           opinionEventTrendStatisticDao.insertBatch(records);
       }
       
       OpinionEventStatistic opinionEventStatistic = new OpinionEventStatistic();
       opinionEventStatistic.setEventId(opinionEvent.getId());
       opinionEventStatistic.setInfoTotal(opinions.size());
       opinionEventStatistic.setDataType(StringUtils.removeEnd(emotionSpread.toString(), "#"));
       opinionEventStatistic.setMediaType(StringUtils.removeEnd(mediaSpread.toString(), "#"));
       opinionEventStatistic.setSource(StringUtils.removeEnd(websiteSpread.toString(), "#"));
       opinionEventStatistic.setGmtCreate(new Date());
       opinionEventStatisticDao.insert(opinionEventStatistic);
    }
    
    /**  
     * 删除事件
     * @param opinionEvent 
     */
    public void deleteEvent(OpinionEvent opinionEvent) {
        opinionEvent.setIsDelete((byte)1);
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
    }
    
    /**  
     * 获取事件列表
     * @param opinionEvent
     * @param pageNo
     * @param pageSize
     * @return 
     */
    public List<OpinionEvent> eventList(OpinionEvent opinionEvent, Integer pageNo, Integer pageSize) {
        PageBounds pageBounds = new PageBounds(pageNo, pageSize);
        OpinionEventExample  example = new OpinionEventExample();
        example.setOrderByClause("gmt_create DESC");
        Criteria criteria = example.createCriteria();
        if (opinionEvent.getRegion() != null) {
            criteria.andRegionEqualTo(opinionEvent.getRegion());
        }
        if (opinionEvent.getEventGroup() != null) {
            criteria.andEventGroupEqualTo(opinionEvent.getEventGroup());
        }
        criteria.andIsDeleteEqualTo((byte)0)
                .andFileReasonIsNull();
        return opinionEventDao.selectByExampleWithPageBounds(example, pageBounds);
    }
    
    
    /** 
     * 获取字典表 
     * @param parent
     * @return 
     */
    public List<OpinionDictionary> getDictionary(String parent) {
        OpinionDictionaryExample example = new OpinionDictionaryExample();
        example.createCriteria().andParentEqualTo(parent);
        return opinionDictionaryDao.selectByExample(example);
    }
    
    
    /**  
     * 获取事件相关的舆情列表
     * @param id
     * @param cycle
     * @param emotion
     * @param source
     * @param pageNo
     * @param pageSize
     * @return 
     */
    public  HashMap<String, Object> getEventInfoList(Long id, Integer cycle, Integer emotion, String source, Integer pageNo, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageBounds pb = new PageBounds(pageNo, pageSize);
        OpinionEsSearchVO esResult = esQueryService.queryEventOpinions(id, buildTimeSpan(cycle), emotion, 
            source != null ? Integer.valueOf(source) : null, pb);
        List<KeyValueVO> mediaTypeSta = esResult.getMediaTypeStats();
        transMediaTypeToChina(mediaTypeSta);
        map.put("opinions", esResult.getOpinions());
        map.put("total", esResult.getTotal());
        map.put("eventHot", opinionEventDao.selectByPrimaryKey(id).getHot());
        mediaTypeSta.add(0, calTotal(mediaTypeSta));
        map.put("mediaTypes", mediaTypeSta);
        return map;
        
    }
    
    public KeyValueVO calTotal(List<KeyValueVO> mediaTypeSta) {
        int total = 0;
        for (KeyValueVO vo : mediaTypeSta) {
            total = total + (int)vo.getValue();
        }
        KeyValueVO tmp = new KeyValueVO();
        tmp.setName("全部");
        tmp.setValue(total);
        return tmp;
        
    }
    
    private DateTime buildTimeSpan(Integer timeSpan) {
            DateTime now = DateTime.now();
            DateTime startTime = null;
            if(2 == timeSpan) startTime = now.plusDays(-7);
            else if(3 == timeSpan) startTime = now.plusMonths(-1);
            else startTime = now.plusHours(-24);
            return startTime;
        }
     
    private void transMediaTypeToChina(List<KeyValueVO> list) {
            for(KeyValueVO v : list) {
                v.setName( WebsiteEnum.getDescByCode( v.getKey().toString() ) );
            }
        }
    
    /**  
     * 获取事件舆情列表的媒体标签
     * @param id
     * @param cycle
     * @param emotion
     * @return 
     */
    public  List<KeyValueVO> eventLabelList(Long id, Integer cycle, Integer emotion){
        PageBounds pb = new PageBounds(1, 10);
        OpinionEsSearchVO esResult = esQueryService.queryEventOpinions(id, buildTimeSpan(cycle), emotion, null, pb);
        List<KeyValueVO> mediaTypeSta = esResult.getMediaTypeStats();
        transMediaTypeToChina(mediaTypeSta);
        return mediaTypeSta;
    }
    
    
    /**
     * 获取事件对应周期的信息总量  
     * @param id
     * @param cycle
     * @return 
     */
    public  long eventInfoTotal(Long id, Integer cycle){
        if (cycle != 4) {
            PageBounds pb = new PageBounds(1, 10);
            OpinionEsSearchVO esResult = esQueryService.queryEventOpinions(id, buildTimeSpan(cycle), null, null, pb);
            return esResult.getTotal();
        } else {
            OpinionEventStatisticExample example = new OpinionEventStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            return opinionEventStatisticDao.selectByExample(example).get(0).getInfoTotal();
        }
    }
    
    
    /**  
     * 获取事件的热度
     * @param id
     * @return 
     */
    public  int eventHotValue(Long id){
        return opinionEventDao.selectByPrimaryKey(id).getHot();
    }
    
    
    /**  
     * 获取事件总体走势
     * @param id
     * @param cycle
     * @return 
     */
    public HashMap<String, Object> eventWholeTrend(Long id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Integer days;
        if (cycle == 1) {
            days = 1;
        } else if (cycle == 2) {
            days = 7;
        } else if (cycle == 3){
            days = 30;
        } else {
            days = 365;
        }
        List<Graph> infoList = opinionEventSourceTrendDao.selectBySourceAndCycle(id, null, "notNull", days);
        map.put("infoList", infoList);
        List<Graph> warnList = opinionEventSourceTrendDao.selectBySourceAndCycle(id, null, null, days);
        map.put("warnList", warnList);
        return map;
    }
    
    
    /**  
     * 事件媒体分布
     * @param id
     * @param cycle
     * @return 
     */
    public List<KeyValueVO> eventSrcDis(Long id, Integer cycle) {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventOpinionMediaSpread(id, buildTimeSpan(cycle), null);
            
        } else {
            OpinionEventStatisticExample example = new OpinionEventStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            OpinionEventStatistic opinionEventStatistic = opinionEventStatisticDao.selectByExampleWithBLOBs(example).get(0);
            rs = new ArrayList<KeyValueVO>();
            for (String e : opinionEventStatistic.getMediaType().split("#")) {
                KeyValueVO vo = new KeyValueVO();
                vo.setKey(e.split(",")[0]);
                vo.setValue(e.split(",")[1]);
                rs.add(vo);
            }
        }
        transToChinese(rs, "F");
        return rs;
       
    }
    
    
    /**  
     * 事件信息走势图
     * @param id
     * @param cycle
     * @return 
     */
    public List<List<Graph>> eventInfoTrend(Long id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<OpinionDictionary> opinionDictionaryList = getDictionary("F");
        Integer days;
        if (cycle == 1) {
            days = 1;
        } else if (cycle == 2) {
            days = 7;
        } else {
            days = 30;
        }
        List<List<Graph>> list = new ArrayList<List<Graph>>();
        List<Graph> allList = opinionEventSourceTrendDao.selectBySourceAndCycle(id, null, "notNull", days);
        list.add(allList);
        for (OpinionDictionary e : opinionDictionaryList) {
            List<Graph> gs = opinionEventSourceTrendDao.selectBySourceAndCycle(id, e.getCode(), "notNull", days);
            if (gs!=null && gs.size()>0) {
                list.add(gs);
            }
        }
        return list;
    }
    
    
    /**  
     * 事件媒体活跃度
     * @param id
     * @param cycle
     * @return 
     */
    public List<KeyValueVO> eventSrcActive(Long id, Integer cycle) {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventWebsiteSpread(id, buildTimeSpan(cycle), null);
        } else {
            OpinionEventStatisticExample example = new OpinionEventStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            OpinionEventStatistic opinionEventStatistic = opinionEventStatisticDao.selectByExampleWithBLOBs(example).get(0);
            rs = new ArrayList<KeyValueVO>();
            for (String e : opinionEventStatistic.getSource().split("#")) {
                KeyValueVO vo = new KeyValueVO();
                vo.setKey(e.split(",")[0]);
                vo.setValue(e.split(",")[1]);
                rs.add(vo);
            }
        }
        return rs;
    }
    
    
    /**  
     * 事件走势
     * @param id
     * @param cycle
     * @param pageNo
     * @param pageSize
     * @return 
     */
    public HashMap<String, Object> eventTrend(Long id, Integer cycle, Integer pageNo, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (cycle != 4) {
            OpinionEsSearchVO vo = esQueryService.queryEventTrendOpinions(id, buildTimeSpan(cycle), null, new PageBounds(pageNo, pageSize));
            map.put("opinions", vo.getOpinions());
        } else {
            OpinionEventTrendStatisticExample example = new OpinionEventTrendStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            map.put("opinions", opinionEventTrendStatisticDao.selectByExampleWithPageBounds(example, new PageBounds(pageNo, pageSize)));
        }
        map.put("eventTime", opinionEventDao.selectByPrimaryKey(id).getGmtCreate());
        return map;
    }
    
    
    /** 
     * 事件关键词云 
     * @param id
     * @param cycle
     * @return 
     */
    public List<KeyValueVO> eventKeywords(Long id, Integer cycle) {
        List<KeyValueVO> words = new ArrayList<KeyValueVO>();
        OpinionEventWordsExample example = new OpinionEventWordsExample();
        example.createCriteria().andIdEqualTo(id).andCycleEqualTo((byte)(int)cycle);
        List<OpinionEventWords> opinionEventWordsList = opinionEventWordsDao.selectByExampleWithBLOBs(example);
        if (cycle == 4 && (opinionEventWordsList == null || opinionEventWordsList.size() == 0)) {
            OpinionEventWordsExample tmp = new OpinionEventWordsExample();
            tmp.createCriteria().andIdEqualTo(id).andCycleEqualTo((byte)3);
            opinionEventWordsList = opinionEventWordsDao.selectByExampleWithBLOBs(tmp);
        }
        if (opinionEventWordsList == null || opinionEventWordsList.size() == 0) {
            return words;
        }
        for (String e : opinionEventWordsList.get(0).getWords().split("#")){
            KeyValueVO vo = new KeyValueVO();
            vo.setName(e.split(",")[0]);
            vo.setValue(e.split(",")[1]);
            words.add(vo);
        }
        return words;
    }
    
    
    /**
     * 事件数据类型  
     * @param id
     * @param cycle
     * @return 
     */
    public List<KeyValueVO> eventDataType(Long id, Integer cycle) {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventEmotionSpread(id, buildTimeSpan(cycle), null);
        } else {
            OpinionEventStatisticExample example = new OpinionEventStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            OpinionEventStatistic opinionEventStatistic = opinionEventStatisticDao.selectByExampleWithBLOBs(example).get(0);
            rs = new ArrayList<KeyValueVO>();
            for (String e : opinionEventStatistic.getDataType().split("#")) {
                KeyValueVO vo = new KeyValueVO();
                vo.setKey(e.split(",")[0]);
                vo.setValue(e.split(",")[1]);
                rs.add(vo);
            }
        }
        transToChinese(rs, "H");
        return rs;
    }  
    
    
    /** 
     * 舆情事件类别分布 
     * @return 
     */
    public List<Graph> eventTypeDis() {
        return opinionEventDao.eventTypeDis();
    }
    /** 
     * 舆情事件地区分布 
     * @return 
     */
    public Map<String, Object> eventRegionDis() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Graph> gs = opinionEventDao.eventRegionDis();
        long max = 0;
        long min = 0;
        for (Graph e : gs) {
            if (max < (long)e.getValue()) {
                max = (long)e.getValue();
            } 
            if (min > (long)e.getValue()) {
                min = (long)e.getValue();
            } 
        }
        map.put("regioins", gs);
        map.put("max", max);
        map.put("min", min);
        return map;
    }
    
    
    public List<OpinionEvent> getHisEventList(String eventLevel, String region, Date startTime, Date endTime, Integer pageNo, Integer pageSize){
        OpinionEventExample example = new OpinionEventExample();
        Criteria criteria = example.createCriteria();
        if (eventLevel != null){
            criteria.andEventLevelEqualTo(eventLevel);
        }
        if (region != null){
            criteria.andRegionEqualTo(region);
        }
        criteria.andGmtCreateGreaterThanOrEqualTo(startTime)
        .andGmtCreateLessThanOrEqualTo(endTime)
        .andIsDeleteEqualTo((byte)0);
        List<OpinionEvent> eventList = opinionEventDao.selectByExampleWithPageBounds(example, new PageBounds(pageNo, pageSize));
        transToChinese(eventList);
        return eventList;
    }
    

    public void transToChinese(List<KeyValueVO> list, String type){
        List<OpinionDictionary> mediaList = getDictionary(type);
        for (KeyValueVO e : list) {
            for (OpinionDictionary f : mediaList) {
                if (e.getKey().equals(f.getCode())) {
                    e.setName(f.getName());
                    break;
                }
            }
        }
    }
    public void transToChinese(OpinionEvent e) {
        List<OpinionDictionary> eventGroupList = getDictionary("A");
        for (OpinionDictionary f : eventGroupList) {
            if (e.getEventGroup().equals(f.getCode())) {
                e.setEventGroup(f.getName());
                break;
            }
        }
        List<OpinionDictionary> monitorList = getDictionary("B");
        for (OpinionDictionary f : monitorList) {
            if (e.getMonitor().equals(f.getCode())) {
                e.setMonitor(f.getName());
                break;
            }
        }
        List<OpinionDictionary> regionList = getDictionary("C");
        for (OpinionDictionary f : regionList) {
            if (e.getRegion().equals(f.getCode())) {
                e.setRegion(f.getName());
                break;
            }
        }
        List<OpinionDictionary> eventLevelList = getDictionary("D");
        for (OpinionDictionary f : eventLevelList) {
            if (e.getEventLevel().equals(f.getCode())) {
                e.setEventLevel(f.getName());
                break;
            }
        }
        List<OpinionDictionary> fileReasonList = getDictionary("E");
        for (OpinionDictionary f : fileReasonList) {
            if (e.getFileReason().equals(f.getCode())) {
                e.setFileReason(f.getName());
                break;
            }
        }
    }
    
    public void transToChinese(List<OpinionEvent> eventList) {
        for (OpinionEvent e : eventList) {
            transToChinese(e);
        }
    }
    
}
