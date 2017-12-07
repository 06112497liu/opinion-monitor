package com.bbd.service.report;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bbd.domain.KeyValueVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bbd.dao.UserDao;
import com.bbd.domain.Account;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventTrendStatistic;
import com.bbd.report.ReportEngine;
import com.bbd.report.enums.DataModelEnum;
import com.bbd.report.enums.ElementEnum;
import com.bbd.report.enums.ExportEnum;
import com.bbd.report.enums.ParamTypeEnum;
import com.bbd.report.enums.StructureEnum;
import com.bbd.report.model.ReportElementModel;
import com.bbd.report.model.ReportElementString;
import com.bbd.report.util.ModelUtil;
import com.bbd.service.EventService;
import com.bbd.service.vo.OpinionVO;
import com.bbd.util.BigDecimalUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;

@Service
public class EventReportService {
    private static Logger logger = LoggerFactory.getLogger(EventReportService.class);
    
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH");
    @Resource
    EventService eventService;
    @Autowired
    private UserDao userDao;
    
    private String opTime;
    private String eventName;
    private String infoTotal;
    private String warnTotal;
    private String eventTime;
    private String allWarn;
    private String firstWarn;
    private String secondWarn;
    private String thirdWarn;
    private String crtWarn;
    private String oppWarn;
    private String timeLabel;
    private String labelTwo;
    private String labelThree;
    
    @Value("#{propertiesConfig['lifeCycle1']}")
    private String lifeCycle1;//生命周期
    
    @Value("#{propertiesConfig['lifeCycle2']}")
    private String lifeCycle2;//生命周期
    
    public Integer test(ExportEnum exportEnum) {
        return 0;
    }

    public Integer generateReport(int cycle, Long id) throws Exception {
        Date currentTime = new Date();
        opTime = formatter.format(currentTime);
        ArrayList<ReportElementString> list = createReportStruct(cycle, id, opTime);
        HashMap<String,Object> params = new HashMap<>();
        params.put("opTime", opTime);
        params.put("eventName", eventName);
        params.put("infoTotal", "888");
        params.put("warnTotal", "200");
        params.put("eventTime", eventTime);
        params.put("allWarn", "60");
        params.put("firstWarn", "10");
        params.put("secondWarn", "20");
        params.put("thirdWarn", "30");
        params.put("crtWarn", "40");
        params.put("oppWarn", "20");
        params.put("timeLabel", "从事件创建至今2017-11-01到" + opTime);
        params.put("labelTwo", "监测历史图表跟踪分析");
        params.put("labelThree", "事件今日预警舆情信息列表");
        
        
        ArrayListMultimap<StructureEnum,ReportElementModel> array = ModelUtil.stringToModel(list);
        ReportEngine re = new ReportEngine();
        File f = new File("E:/"+System.currentTimeMillis()+".pdf");
        OutputStream out= null;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        re.generateReport(Optional.of("report/opinionEvent.prpt"),array,params,ExportEnum.PDF,out);

        return null;
    }
    
    public int daysBetween(Date start, Date end) {
        return (int) Math.ceil((double)(end.getTime() - start.getTime()) / (24 * 3600 * 1000));
    }
    
    public String buildUserInfo(Account account) {
        return account.getName() + "-"+ account.getDepNote() + "-" + 
                userDao.selectByPrimaryKey(account.getUserId()).getUsername();
    }

    
    public void eventDetail(ArrayList<ReportElementString> list, int cycle, Long id) {
        ReportElementString eventSubElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
                DataModelEnum.TABLE_DATA, "eventDetail", "eventDetailData");
        List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"item"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc()};
        OpinionEvent opinionEvent = eventService.getEventChinese(id);
        eventName = opinionEvent.getEventName();
        eventTime = formatter.format(opinionEvent.getGmtCreate());
        Map map= eventService.getEventUser(id);
        value.add(new String[]{"事件名称：" + opinionEvent.getEventName()});
        if (cycle != 4) {
            value.add(new String[]{"事件实时总热度：" + opinionEvent.getHot()});
        }
        Account createUser = (Account)map.get("createUser");
        value.add(new String[]{"监测账号：" + buildUserInfo(createUser)});
        if (cycle != 4) {
            value.add(new String[]{"监测时间：" + formatter.format(opinionEvent.getGmtCreate()) + "--" + opTime + "," + 
            "已持续监测" + daysBetween(opinionEvent.getGmtCreate(), new Date()) + "天"});
        }
        if (cycle == 4) {//如果已经归档
            value.add(new String[]{"监测时间：" + formatter.format(opinionEvent.getGmtCreate()) + ",共持续监测" + daysBetween(opinionEvent.getGmtCreate(), opinionEvent.getGmtFile()) + "天"});
            Account fileUser = (Account)map.get("fileUser");
            value.add(new String[]{"归档账号：" + buildUserInfo(fileUser)});
            value.add(new String[]{"归档时间：" + formatter.format(opinionEvent.getGmtFile())});
            value.add(new String[]{"归档理由：" + opinionEvent.getFileReason()});
            value.add(new String[]{"备注：" + opinionEvent.getRemark()});
        }
        
        value.add(new String[]{"事件分组：" + opinionEvent.getEventGroup()});
        value.add(new String[]{"监管主体：" + opinionEvent.getMonitor()});
        value.add(new String[]{"事发区域：" + opinionEvent.getRegion()});
        value.add(new String[]{"事件级别：" + opinionEvent.getEventLevel()});
        value.add(new String[]{"事件描述：" + opinionEvent.getDescription()});
        
        value.add(new String[]{"商家主体：" + opinionEvent.getMerchant()});
        value.add(new String[]{"品牌：" + opinionEvent.getBrand()});
        value.add(new String[]{"产品：" + opinionEvent.getProduct()});
        value.add(new String[]{"商家地址：" + opinionEvent.getAddress()});
        value.add(new String[]{"商家联系方式：" + opinionEvent.getMerchantTel()});
        value.add(new String[]{"消费者：" + opinionEvent.getConsumer()});
        value.add(new String[]{"消费者联系方式：" + opinionEvent.getConsumerTel()});
        
        value.add(new String[]{"事件关键词库"});
        value.add(new String[]{"包含关键词：" + opinionEvent.getIncludeWords()});
        value.add(new String[]{"主体关键词：" + opinionEvent.getKeywords()});
        value.add(new String[]{"排除关键词：" + opinionEvent.getExcludeWords()});
       
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
    	String eventSubData =  JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    	eventSubElement.setData(eventSubData);
        list.add(eventSubElement);
    }
    
    public void eventWholeTrend(ArrayList<ReportElementString> list, int cycle, Long id) {
        if (cycle == 1) {
            labelTwo = "近24小时监测图表跟踪分析";
        } else if (cycle == 2) {
            labelTwo = "近7天监测图表跟踪分析";
        } else if (cycle == 3) {
            labelTwo = "近30天监测图表跟踪分析";
        } else if (cycle == 4) {
            labelTwo = "历史监测图表跟踪分析";
        } else if (cycle == 5) {
            labelTwo = "创建事件至今监测图表跟踪分析";
        }
        infoTotal = String.valueOf(eventService.eventInfoTotal(id, cycle, false));
        warnTotal = String.valueOf(eventService.eventInfoTotal(id, cycle, true));
        Map map = eventService.eventWholeTrend(id, cycle);
        List<KeyValueVO> infoList = (List<KeyValueVO>) map.get("infoList");
        List<KeyValueVO> warnList = (List<KeyValueVO>) map.get("warnList");
        List<List<KeyValueVO>> listAll = new ArrayList<List<KeyValueVO>>();
        listAll.add(infoList);
        listAll.add(warnList);
        ReportElementString eventWholeElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "eventWholeTrend", "eventWholeTrendData");
        eventWholeElement.setData(computeTrendDate(listAll));
        list.add(eventWholeElement);
    }
    
    public void eventSrcDis(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventSrcDis(id, cycle);
        ReportElementString eventSrcDisElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "mediaSpread", "mediaSpreadData");
        eventSrcDisElement.setData(computeTrend(listData));
        list.add(eventSrcDisElement);
    }
    
    public void eventInfoTrend(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<List<KeyValueVO>> listData = eventService.eventInfoTrend(id, cycle);
        ReportElementString eventSrcDisElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "infoTrend", "infoTrendData");
        eventSrcDisElement.setData(computeTrendDate(listData));
        list.add(eventSrcDisElement);
    }
    
    public void eventSrcActive(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventSrcActive(id, cycle);
        ReportElementString eventSrcActiveElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "websiteSpread", "websiteSpreadData");
        eventSrcActiveElement.setData(computeTrend(listData));
        list.add(eventSrcActiveElement);
        
        ReportElementString eventSrcActive2Element = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "websiteSpread2", "websiteSpread2Data");
        eventSrcActive2Element.setData(computeTrend(listData));
        list.add(eventSrcActive2Element);
    }
    
    public void keywords(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventKeywords(id, cycle);
        ReportElementString keywordsElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "keywords", "keywordsData");
        keywordsElement.setData(computeTrend(listData));
        list.add(keywordsElement);
    }
    
    public void eventDataType(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventDataType(id, cycle);
        ReportElementString eventSrcActiveElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "dataType", "dataTypeData");
        
        eventSrcActiveElement.setData(computeTrend(listData));
        list.add(eventSrcActiveElement);
    }
    
    public void eventTrend(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<OpinionEventTrendStatistic>  opinions = (List<OpinionEventTrendStatistic>) eventService.eventTrend(id, cycle, 1, Integer.MAX_VALUE).get("opinions");
        ReportElementString eventTrendElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "dataType", "dataTypeData");
        String[] title = new String[]{"item"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc()};
        List<Object[]> value = new ArrayList<>();
        for (OpinionEventTrendStatistic evtStc : opinions) {
            value.add(new Object[]{"["+formatter2.format(evtStc.getPublishTime())+"]"+evtStc.getTitle()+"["+evtStc.getWebsite()+"] 相同文章数："+evtStc.getSimiliarCount()});
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        String value2 = JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
        eventTrendElement.setData(value2);
        list.add(eventTrendElement);
    }
    
    public void eventOpinion(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<OpinionVO> opinions = (List<OpinionVO>) eventService.getEventInfoList(id, cycle, null, null, 1, Integer.MAX_VALUE).get("opinions");
        ReportElementString eventTrendElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "opinionInfo", "opinionInfoData");
        String[] title = new String[]{"title","summary","similiarCount","emotion","website","publishTime","hot","level","link"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc()
                                          , ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc()
                                          , ParamTypeEnum.STRING.getDesc()};
        List<Object[]> value = new ArrayList<>();
        for (OpinionVO vo : opinions) {
            value.add(new Object[]{vo.getTitle(), vo.getSummary(), vo.getSimiliarCount(), vo.getEmotion(), vo.getWebsite(), 
                                   vo.getPublishTime(), vo.getHot(),vo.getLevel(), vo.getLink()});
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        String value2 = JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
        eventTrendElement.setData(value2);
        list.add(eventTrendElement);
    }
    
    
    public String computeTrendDate(List<List<KeyValueVO>> parentList){
       
        List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"date","type","num"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc()};
        if (parentList != null && parentList.size() > 0) {
            for (int i = 0; i< parentList.get(0).size(); i++) {
                for (int j = 0; j < parentList.size(); j++) {
                    value.add(new Object[]{parentList.get(j).get(i).getName(),
                                           parentList.get(j).get(i).getKey(),
                                           parentList.get(j).get(i).getValue()});
                }
            }
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        return JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    }
    
    public String computeTrend(List<KeyValueVO> list){
        List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"type","num"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc()};
        if (list != null && list.size() > 0) {
            for (int i = 0; i< list.size(); i++) {
                value.add(new Object[]{list.get(i).getName(),
                                       list.get(i).getValue()});
            }
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        return JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    }
    
    /**
     * 构建报表数据
     * @param year
     * @param quarter
     * @return
     * @throws Exception 
     */
    private ArrayList<ReportElementString> createReportStruct(int cycle, Long id, String opTime) throws Exception {
    	ArrayList<ReportElementString> list = new ArrayList<>();
    	eventDetail(list, cycle, id);
    	eventWholeTrend(list, cycle, id);
    	eventSrcDis(list, cycle, id);
    	eventInfoTrend(list, cycle, id);
    	eventSrcActive(list, cycle, id);
    	keywords(list, cycle, id);
    	eventDataType(list, cycle, id);
    	eventTrend(list, cycle, id);
    	eventOpinion(list, cycle, id);
        return list;
    }

    
}
