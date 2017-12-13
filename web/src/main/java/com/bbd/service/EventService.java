package com.bbd.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.bbd.domain.*;
import com.bbd.util.DateUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.constant.EsConstant;
import com.bbd.dao.AccountDao;
import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventMediaStatisticDao;
import com.bbd.dao.OpinionEventStatisticDao;
import com.bbd.dao.OpinionEventTrendStatisticDao;
import com.bbd.dao.OpinionEventWholeTrendStatisticDao;
import com.bbd.dao.OpinionEventWordsDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.OpinionEventExample.Criteria;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.BizErrorCode;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.bbd.service.vo.OpinionOpRecordVO;
import com.bbd.service.vo.OpinionVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.BigDecimalUtil;
import com.bbd.util.UserContext;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;


/** 
 * @author daijinlong 
 * @version $Id: EventService.java, v 0.1 2017年10月25日 下午2:06:26 daijinlong Exp $ 
 */
@Service
public class EventService{
    @Autowired
    OpinionEventDao opinionEventDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    OpinionDictionaryDao opinionDictionaryDao;
    @Autowired
    WarnSettingDao warnSettingDao;
    @Autowired
    OpinionEventWordsDao opinionEventWordsDao;
    @Autowired
    OpinionEventTrendStatisticDao opinionEventTrendStatisticDao;
    @Autowired
    OpinionEventMediaStatisticDao opinionEventMediaStatisticDao;
    @Autowired
    OpinionEventStatisticDao opinionEventStatisticDao;
    @Autowired
    OpinionEventWholeTrendStatisticDao opinionEventWholeTrendStatisticDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private EsModifyService esModifyService;
    @Autowired
    private SystemSettingService systemSettingService;
    @Autowired
    private UserService userService;
    /**  
     * 创建事件
     * @param opinionEvent 
     * @throws InterruptedException 
     * @throws ExecutionException 
     * @throws IOException 
     */
    public synchronized Long createEvent(OpinionEvent opinionEvent) throws IOException, ExecutionException, InterruptedException {
        List<OpinionEvent> evtList = eventList(new OpinionEvent(), 1, Integer.MAX_VALUE);
        if (evtList.size() == 50) {
            throw new ApplicationException(BizErrorCode.EVENT_UPTO_50);
        }
        for (OpinionEvent evt : eventList(opinionEvent)) {
            if (evt.getEventName().trim().equals(opinionEvent.getEventName().trim())) {
                throw new ApplicationException(BizErrorCode.EVENT_NAME_EXIST);
            }
        }
        opinionEvent.setIsDelete((byte)0);
        opinionEvent.setGmtCreate(new Date());
        opinionEventDao.insert(opinionEvent);
        //设置事件预警初始值
        WarnSetting recordNew = new WarnSetting();
        recordNew.setEventId(opinionEvent.getId());
        recordNew.setType(1);
        recordNew.setTargetType(2);
        recordNew.setPopup(1);
        recordNew.setLevel(0);
        recordNew.setMin(60);
        recordNew.setMax(Integer.MAX_VALUE);
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
        recordWhole1.setMax(Integer.MAX_VALUE);
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
        
        //更新事件关联舆情
        OpinionOpRecordVO recordVO = new OpinionOpRecordVO();
        recordVO.setOperatorId(UserContext.getUser().getId());
        recordVO.setOpTime(new Date());
        recordVO.setOpType(3);
        recordVO.setUuid(opinionEvent.getUuid());
        recordVO.setTargeterId(-1L);
        esModifyService.recordOpinionOp(recordVO);
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put(EsConstant.opStatusField, 3);
        fieldMap.put(EsConstant.recordTimeField, DateUtil.formatDateByPatten(new Date(), "yyyy-MM-dd HH:mm:ss"));
        esModifyService.updateOpinion(UserContext.getUser(), opinionEvent.getUuid(), fieldMap);
        return opinionEvent.getId();
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
        List<OpinionEvent> evtList = eventList(opinionEvent);
        for (OpinionEvent evt : evtList) {
            if (evt.getEventName().trim().equals(opinionEvent.getEventName().trim())
                    && evt.getId() != opinionEvent.getId()) {
                throw new ApplicationException(BizErrorCode.EVENT_NAME_EXIST);
            }
        }
        opinionEvent.setGmtModified(new Date());
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
    }
    
    public Map getEventUser(Long id) {
        HashMap map = new HashMap();
        OpinionEvent evt = opinionEventDao.selectByPrimaryKey(id);
        AccountExample example = new AccountExample();
        example.createCriteria().andUserIdEqualTo(evt.getCreateBy());
        Account createUser = accountDao.selectByExample(example).get(0);
        map.put("createUser", createUser);
        if (evt.getFileBy() != null) {
            example.clear();
            example.createCriteria().andUserIdEqualTo(evt.getFileBy());
            Account fileUser = accountDao.selectByExample(example).get(0);
            map.put("fileUser", fileUser);
        }
        return map;
    }
    
    /**  
     * 归档事件
     * @param opinionEvent 
     * @throws ParseException 
     */
    public void fileEvent(OpinionEvent opinionEvent) throws ParseException {
        opinionEvent.setGmtFile(new Date());
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
        OpinionEvent tmp =  opinionEventDao.selectByPrimaryKey(opinionEvent.getId());
        //DateTime startTime = new DateTime(tmp.getGmtCreate());
        DateTime endTime = DateTime.now();
       /* DateTime oneYearBefore = endTime.plusYears(-1);
        if (oneYearBefore.getMillis() > startTime.getMillis()) {
            startTime = oneYearBefore;
        }*/
        
        long infototal = esQueryService.queryEventInfoTotal(opinionEvent.getId(), null, endTime, false);
        long warntotal = esQueryService.queryEventInfoTotal(opinionEvent.getId(), null, endTime, true);
        
        //归档事件媒体分布、媒体活跃度、媒体来源、数据情感信息
        List<KeyValueVO> mediaSpreadList = esQueryService.getEventOpinionMediaSpread(opinionEvent.getId(), null, endTime);
        StringBuilder mediaSpread = new StringBuilder();
        for (KeyValueVO e : mediaSpreadList) {
            mediaSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        List<KeyValueVO> websiteSpreadList = esQueryService.getEventWebsiteSpread(opinionEvent.getId(), null, endTime);
        StringBuilder websiteSpread = new StringBuilder();
        for (KeyValueVO e : websiteSpreadList) {
            websiteSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        List<KeyValueVO> emotionSpreadList = esQueryService.getEventEmotionSpread(opinionEvent.getId(), null, endTime);
        StringBuilder emotionSpread = new StringBuilder();
        for (KeyValueVO e : emotionSpreadList) {
            emotionSpread.append(e.getKey()).append(",").append(e.getValue()).append("#");
        }
        OpinionEventStatistic opinionEventStatistic = new OpinionEventStatistic();
        opinionEventStatistic.setEventId(opinionEvent.getId());
        opinionEventStatistic.setInfoTotal(Integer.valueOf(String.valueOf(infototal)));
        opinionEventStatistic.setWarnTotal(Integer.valueOf(String.valueOf(warntotal)));
        opinionEventStatistic.setDataType(StringUtils.removeEnd(emotionSpread.toString(), "#"));
        opinionEventStatistic.setMediaType(StringUtils.removeEnd(mediaSpread.toString(), "#"));
        opinionEventStatistic.setSource(StringUtils.removeEnd(websiteSpread.toString(), "#"));
        
        OpinionEventWordsExample example = new OpinionEventWordsExample();
        example.createCriteria().andEventIdEqualTo(opinionEvent.getId()).andCycleEqualTo((byte)(int)3);
        List<OpinionEventWords> opinionEventWordsList = opinionEventWordsDao.selectByExampleWithBLOBs(example);
        if (opinionEventWordsList != null && opinionEventWordsList.size() > 0) {
            opinionEventStatistic.setWords(opinionEventWordsList.get(0).getWords());
        }
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
        criteria.andIsDeleteEqualTo((byte)0);
        criteria.andFileReasonIsNull();
        return opinionEventDao.selectByExampleWithPageBounds(example, pageBounds);
    }
    
    public List<OpinionEvent> eventList(OpinionEvent opinionEvent) {
        OpinionEventExample  example = new OpinionEventExample();
        Criteria criteria = example.createCriteria();
        criteria.andEventNameEqualTo(opinionEvent.getEventName().trim());
        criteria.andIsDeleteEqualTo((byte)0);
        return opinionEventDao.selectByExample(example);
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
    public  HashMap<String, Object> getEventInfoList(Long id, Integer cycle, Integer emotion, Integer source, Integer hot, Integer pageNo, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageBounds pb = new PageBounds(pageNo, pageSize);
        OpinionEsSearchVO esResult = esQueryService.queryEventOpinions(id, new DateTime(getStartDate(cycle)), emotion, 
            source, hot, pb);
       
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot(), setting));
        });
        map.put("opinions", opinions);
        map.put("total", esResult.getTotal());
        map.put("mediaTypes",  calAllMedia(esResult.getMediaTypeStats()));
        List<KeyValueVO> emotionStats = esResult.getEmotionStats();
        transToChinese(emotionStats, "H");
        map.put("emotionStats",  emotionStats);
        map.put("hotLevelStats",  esResult.getHotLevelStats());
        return map;
        
    }
    
    public KeyValueVO calTotal(List<KeyValueVO> mediaTypeSta) {
        long total = 0;
        for (KeyValueVO vo : mediaTypeSta) {
            total = total + (long)vo.getValue();
        }
        KeyValueVO tmp = new KeyValueVO();
        tmp.setName("全部");
        tmp.setValue(total);
        return tmp;
        
    }
    
    public List<KeyValueVO> calAllMedia(List<KeyValueVO> mediaTypeSta) {
        List<KeyValueVO> allMediaType =  new ArrayList<KeyValueVO>();
        List<OpinionDictionary> opinionDictionaryList = getDictionary("F");
        boolean flag = false;
        for (OpinionDictionary e : opinionDictionaryList) {
            for (KeyValueVO f : mediaTypeSta) {
                if (e.getCode().equals(String.valueOf(f.getKey()))) {
                    f.setName(e.getName());
                    allMediaType.add(f);
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                KeyValueVO vo = new KeyValueVO();
                vo.setKey(e.getCode());
                vo.setName(e.getName());
                vo.setValue(0l);
                allMediaType.add(vo);
            }
            flag = false;
            
        }
        KeyValueVO tmp = calTotal(allMediaType);
        allMediaType.add(0, tmp);
        return allMediaType;
    }
    
    /**
     * 获取事件对应周期的信息总量  
     * @param id
     * @param cycle
     * @return 
     */
    public  long eventInfoTotal(Long id, Integer cycle, boolean isWarn){
        if (cycle != 4) {
            return esQueryService.queryEventInfoTotal(id, new DateTime(getStartDate(cycle)), null, isWarn);
        } else {
            OpinionEventStatisticExample example = new OpinionEventStatisticExample();
            example.createCriteria().andEventIdEqualTo(id);
            if (isWarn == false) {
                return opinionEventStatisticDao.selectByExample(example).get(0).getInfoTotal();
            } else {
                return opinionEventStatisticDao.selectByExample(example).get(0).getWarnTotal();
            }
            
        }
    }
    
    
    /**  
     * 获取事件的热度
     * @param id
     * @return 
     */
    public  Integer eventHotValue(Long id){
        return opinionEventDao.selectByPrimaryKey(id).getHot();
    }
    
    
    /**  
     * 获取事件总体走势
     * @param id
     * @param cycle
     * @return 
     */
    public Map<String, List<KeyValueVO>> eventWholeTrend(Long id, Integer cycle) {
        Map<String, List<KeyValueVO>> map = new HashMap<String, List<KeyValueVO>>();
        OpinionEvent opinionEvent = opinionEventDao.selectByPrimaryKey(id);
        List<Date> dates = getDates(cycle, opinionEvent);
        List<KeyValueVO> infoList;
        List<KeyValueVO> warnList;
        if (dates == null || dates.size() == 0) {
            infoList = new ArrayList<KeyValueVO>();
            warnList = new ArrayList<KeyValueVO>();
        } else {
            infoList = esQueryService.queryEventInfoTotal(opinionEvent, false, cycle);
            warnList = esQueryService.queryEventInfoTotal(opinionEvent, true, cycle);
        }
        map.put("infoList", infoList);
        map.put("warnList", warnList);
        return map;
    }
    
    
    /**  
     * 事件媒体分布
     * @param id
     * @param cycle
     * @return 
     * @throws Exception 
     */
    public List<KeyValueVO> eventSrcDis(Long id, Integer cycle) throws Exception {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventOpinionMediaSpread(id, new DateTime(getStartDate(cycle)), null);
        } else {
            rs = getEventStatistic("mediaType", id);
        }
        transToChinese(rs, "F");
        toPercent(rs);
        return rs;
       
    }
    
    public void toPercent(List<KeyValueVO> list) {
        double total = 0;
        for (KeyValueVO vo : list) {
            total = total + Double.valueOf(String.valueOf(vo.getValue()));
        }
        for (KeyValueVO vo : list) {
            vo.setValue(BigDecimalUtil.div(Double.valueOf(String.valueOf(vo.getValue())) * 100, total, 2));
        }
    }
    
    
    /**  
     * 事件信息走势图
     * @param id
     * @param cycle
     * @return 
     */
    public List<List<KeyValueVO>> eventInfoTrend(Long id, Integer cycle) {
        List<OpinionDictionary> opinionDictionaryList = getDictionary("F");
        addAllToDic(opinionDictionaryList);
        //Date startDate = getStartDate(cycle);
        OpinionEventMediaStatisticExample example = new OpinionEventMediaStatisticExample();
        example.setOrderByClause("pick_time ASC");
        OpinionEvent opinionEvent = opinionEventDao.selectByPrimaryKey(id);
        List<List<KeyValueVO>> list = new ArrayList<List<KeyValueVO>>();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (OpinionDictionary e : opinionDictionaryList) {
            com.bbd.domain.OpinionEventMediaStatisticExample.Criteria criteria =  example.createCriteria();
            List<Date> dates = getDates(cycle, opinionEvent);
            if (dates == null || dates.size() == 0) {
                break;
            }
            criteria.andEventIdEqualTo(id).andPickTimeIn(dates)
            .andMediaCodeEqualTo(e.getCode());
            List<OpinionEventMediaStatistic> evtMediaStaList = opinionEventMediaStatisticDao.selectByExample(example);
            if (evtMediaStaList == null || evtMediaStaList.size() == 0) {
                break;
            }
            List<KeyValueVO> listSub = new ArrayList<KeyValueVO>();
            for (OpinionEventMediaStatistic f : evtMediaStaList) {
                KeyValueVO vo = new KeyValueVO();
                vo.setKey(e.getName());
                vo.setName(dateFormater.format(f.getPickTime()));
                vo.setValue(f.getMediaCount());
                listSub.add(vo);
            }
            list.add(listSub);
            example.clear();
        }
      return list;
       
    }
    
    public void addAllToDic(List<OpinionDictionary> opinionDictionaryList) {
        OpinionDictionary opinionDictionary = new OpinionDictionary();
        opinionDictionary.setCode("all");
        opinionDictionary.setName("全部");
        opinionDictionaryList.add(0, opinionDictionary);
    }
    
     public List<Date> getDates(int cycle, OpinionEvent opinionEvent) {
         DateTime now = DateTime.now();
         DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
         DateTime createTime = new  DateTime(opinionEvent.getGmtCreate());
         String yearMonDay = now.getYear() + "-" + now.getMonthOfYear() + "-" + now.getDayOfMonth() + " ";
         DateTime latestTime;
         List<Date> dates = new ArrayList<Date>();
         if (cycle == 1) {
             latestTime = DateTime.parse(
                 yearMonDay + (now.getHourOfDay() % 2 == 0 ? now.getHourOfDay() : now.getHourOfDay() - 1) + ":00:00", format);
             addToDates(dates, createTime, null, latestTime, 12, 2);
         } else if (cycle == 2) {
             latestTime = DateTime.parse((yearMonDay + (now.getHourOfDay() / 12 == 0 ? 0 : 12) + ":00:00"), format);
             addToDates(dates, createTime, null, latestTime, 14, 12);
         } else if (cycle == 3 || cycle == 5) {
             latestTime = DateTime.parse(yearMonDay + "00:00:00", format);
             addToDates(dates, createTime, null, latestTime, cycle == 3 ? 30 : 365, 24);
         } else if (cycle == 4) {
             now = new DateTime(opinionEvent.getGmtFile());
             latestTime = DateTime.parse(now.getYear() + "-" + now.getMonthOfYear() + "-" + now.getDayOfMonth() + " " + "00:00:00", format);
             addToDates(dates, createTime, now, latestTime, 365, 24);
         } 
         return dates;
     } 
     

     public void addToDates(List<Date> dates, DateTime createTime, DateTime fileTime,DateTime latestDate, int days, int interval){
         for (int i = 0; i <= days -1; i++) {
             DateTime endTime = latestDate.minusHours(i * interval);
             if (createTime.getMillis() > endTime.getMillis()) {
                 break;
             }
             if (fileTime != null && 
                     endTime.getMillis() < fileTime.minusYears(1).getMillis()){
                 break;
             }
             dates.add(endTime.toDate());
         }
     }
    
     public Date getStartDate(int cycle) {
         Date startDate = null;
         if (cycle == 1) {
             startDate = DateUtils.addDays(new Date(), -1);
         } else if (cycle == 2) {
             startDate = DateUtils.addDays(new Date(), -7);
         } else if (cycle == 3) {
             startDate = DateUtils.addDays(new Date(), -30);
         } 
         return startDate;
     }
    
    /**  
     * 事件媒体活跃度
     * @param id
     * @param cycle
     * @return 
     * @throws Exception 
     */
    public List<KeyValueVO> eventSrcActive(Long id, Integer cycle) throws Exception {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventWebsiteSpread(id, new DateTime(getStartDate(cycle)), null);
            for (KeyValueVO vo : rs) {
                vo.setName((String)vo.getKey());
                vo.setKey(null);
            }
            return rs;
        } else {
            return getEventStatistic("source", id);
        }
    }
    
    public List<KeyValueVO> getEventStatistic(String column, Long id) throws Exception{
        List<KeyValueVO> rs = new ArrayList<KeyValueVO>();
        OpinionEventStatisticExample example = new OpinionEventStatisticExample();
        example.createCriteria().andEventIdEqualTo(id);
        List<OpinionEventStatistic> opinionEventStatisticList = opinionEventStatisticDao.selectByExampleWithBLOBs(example);
        OpinionEventStatistic opinionEventStatistic = null;
        if (opinionEventStatisticList != null && opinionEventStatisticList.size() > 0) {
            opinionEventStatistic = opinionEventStatisticDao.selectByExampleWithBLOBs(example).get(0);
        } else {
            return rs;
        }
        if (opinionEventStatistic.getSource() == null || opinionEventStatistic.getSource().trim().equals("")) {
            return rs;
        }
        for (String e : BeanUtils.getProperty(opinionEventStatistic, column).split("#")) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(e.split(",")[0]);
            vo.setName(e.split(",")[0]);
            vo.setValue(e.split(",")[1]);
            rs.add(vo);
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
        OpinionEventTrendStatisticExample example = new OpinionEventTrendStatisticExample();
        example.setOrderByClause("publish_time ASC");//00//取发布时间还是摘录时间排序？
        example.createCriteria().andEventIdEqualTo(id);
        map.put("opinions", opinionEventTrendStatisticDao.selectByExampleWithPageBoundsWithBLOBs(example, new PageBounds(pageNo, pageSize)));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(opinionEventDao.selectByPrimaryKey(id).getGmtCreate());
        map.put("eventTime", dateString);
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
        example.createCriteria().andEventIdEqualTo(id).andCycleEqualTo((byte)(int)(cycle >= 3 ? 3 : cycle));
        List<OpinionEventWords> opinionEventWordsList = opinionEventWordsDao.selectByExampleWithBLOBs(example);
        if (opinionEventWordsList == null || opinionEventWordsList.size() == 0) {
            return words;
        }
        String word = opinionEventWordsList.get(0).getWords();
        if (word == null || word.trim().equals("")) {
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
     * @throws Exception 
     */
    public List<KeyValueVO> eventDataType(Long id, Integer cycle) throws Exception {
        List<KeyValueVO> rs = null;
        if (cycle.intValue() != 4) {
            rs = esQueryService.getEventEmotionSpread(id, new DateTime(getStartDate(cycle)), null);
        } else {
            rs = getEventStatistic("dataType", id);
        }
        transToChinese(rs, "H");
        List<KeyValueVO> rs2 = new ArrayList<KeyValueVO>();
        long crt = 0;
        long opp = 0;
        for (KeyValueVO vo : rs) {
            long value = Long.valueOf(String.valueOf(vo.getValue()));
            if (String.valueOf(vo.getKey()).equals("0") || String.valueOf(vo.getKey()).equals("1")) {
                crt = crt + value;
            } else {
                opp = opp + value;
            }
        }
        if (opp > 0) {
            KeyValueVO oppVO = new KeyValueVO();
            oppVO.setName("敏感");
            oppVO.setValue(opp);
            rs2.add(oppVO);
        } 
        if (crt > 0) {
            KeyValueVO crtVO = new KeyValueVO();
            crtVO.setName("非敏感");
            crtVO.setValue(crt);
            rs2.add(crtVO);
        } 
        return rs2;
    }  
    
    
    /** 
     * 舆情事件类别分布 
     * @return 
     */
    public List<KeyValueVO> eventTypeDis() {
        return opinionEventDao.eventTypeDis();
    }
    /** 
     * 舆情事件地区分布 
     * @return 
     */
    public Map<String, Object> eventRegionDis() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<KeyValueVO> gs = opinionEventDao.eventRegionDis();
        long max = 0;
        long min = 0;
        for (KeyValueVO e : gs) {
            long value = Long.valueOf(String.valueOf(e.getValue()));
            if (max < value) {
                max = value;
            } 
            if (min > value) {
                min = value;
            } 
        }
        map.put("regioins", gs);
        map.put("max", max);
        map.put("min", min);
        return map;
    }
    
    
    public List<OpinionEvent> getHisEventList(String eventLevel, String region, Date startTime, Date endTime, Integer pageNo, Integer pageSize){
        OpinionEventExample example = new OpinionEventExample();
        example.setOrderByClause("gmt_file DESC");
        Criteria criteria = example.createCriteria();
        if (eventLevel != null){
            criteria.andEventLevelEqualTo(eventLevel);
        }
        if (region != null){
            criteria.andRegionEqualTo(region);
        }
        if (startTime != null){
            criteria.andGmtCreateGreaterThanOrEqualTo(startTime);
        }
        if (endTime != null){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        criteria
        .andIsDeleteEqualTo((byte)0)
        .andFileReasonIsNotNull();
        List<OpinionEvent> eventList = opinionEventDao.selectByExampleWithPageBounds(example, new PageBounds(pageNo, pageSize));
        transToChinese(eventList);
        return eventList;
    }
    

    public void transToChinese(List<KeyValueVO> list, String type){
        List<OpinionDictionary> mediaList = getDictionary(type);
        for (KeyValueVO e : list) {
            for (OpinionDictionary f : mediaList) {
                if (String.valueOf(e.getKey()).equals(f.getCode())) {
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
        if (e.getFileReason() == null) {
            return;
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
