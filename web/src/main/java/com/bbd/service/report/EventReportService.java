package com.bbd.service.report;


import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bbd.dao.UserDao;
import com.bbd.domain.Account;
import com.bbd.domain.KeyValueVO;
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
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;

@Service
public class EventReportService {
    private static Logger logger = LoggerFactory.getLogger(EventReportService.class);
    
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Resource
    EventService eventService;
    @Autowired
    private UserDao userDao;
    
    private String opTime;
    private String eventName;
    private String infoTotal;
    private String warnTotal;
    private String eventTime;
    private String fileTime;
    private String allWarn;
    private String firstWarn;
    private String secondWarn;
    private String thirdWarn;
    private String crtWarn;
    private String midWarn;
    private String oppWarn;
    private String timeLabel;
    private String labelTwo;
    private String labelThree;
    
    public void generateReport(int cycle, Long id, OutputStream out) throws Exception {
        Date currentTime = new Date();
        opTime = formatter.format(currentTime);
        ArrayList<ReportElementString> list = createReportStruct(cycle, id, opTime);
        HashMap<String,Object> params = new HashMap<>();
        params.put("opTime", formatter2.format(currentTime));
        params.put("eventName", eventName);
        params.put("infoTotal", infoTotal);
        params.put("warnTotal", warnTotal);
        params.put("eventTime", eventTime);
        
        params.put("allWarn", allWarn);
        params.put("firstWarn", firstWarn);
        params.put("secondWarn", secondWarn);
        params.put("thirdWarn", thirdWarn);
        params.put("crtWarn", crtWarn);
        params.put("midWarn", midWarn);
        params.put("oppWarn", oppWarn);
        
        params.put("timeLabel", timeLabel);
        params.put("labelTwo", labelTwo);
        params.put("labelThree", labelThree);
        
        ArrayListMultimap<StructureEnum,ReportElementModel> array = ModelUtil.stringToModel(list);
        ReportEngine re = new ReportEngine();
       /* File f = new File("E:/"+System.currentTimeMillis()+".pdf");
        OutputStream out= null;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }*/
        if (cycle != 4) {
            re.generateReport(Optional.of("report/opinionEvent.prpt"),array,params,ExportEnum.PDF,out);
        } else {
            re.generateReport(Optional.of("report/opinionEventHis.prpt"),array,params,ExportEnum.PDF,out);
        }
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
        fileName(cycle, id);
        Map map= eventService.getEventUser(id);
        value.add(new String[]{"事件名称：" + hasData(opinionEvent.getEventName())});
        if (cycle != 4) {
            value.add(new String[]{"事件实时总热度：" + hasData(opinionEvent.getHot())});
        }
        Account createUser = (Account)map.get("createUser");
        value.add(new String[]{"监测账号：" + buildUserInfo(createUser)});
        if (cycle != 4) {
            value.add(new String[]{"监测时间：" + formatter.format(opinionEvent.getGmtCreate()) + "——" + opTime + "，" + 
            "已持续监测" + daysBetween(opinionEvent.getGmtCreate(), new Date()) + "天"});
        }
        if (cycle == 4) {//如果已经归档
            fileTime = formatter.format(opinionEvent.getGmtFile());
            value.add(new String[]{"监测时间：" + formatter.format(opinionEvent.getGmtCreate()) + "，共持续监测" + daysBetween(opinionEvent.getGmtCreate(), opinionEvent.getGmtFile()) + "天"});
            Account fileUser = (Account)map.get("fileUser");
            value.add(new String[]{"归档账号：" + buildUserInfo(fileUser)});
            value.add(new String[]{"归档时间：" + formatter.format(opinionEvent.getGmtFile())});
            value.add(new String[]{"归档理由：" + hasData(opinionEvent.getFileReason())});
            value.add(new String[]{"备注：" + hasData(opinionEvent.getRemark())});
        }
        
        value.add(new String[]{"事件分组：" + hasData(opinionEvent.getEventGroup())});
        value.add(new String[]{"监管主体：" + hasData(opinionEvent.getMonitor())});
        value.add(new String[]{"事发区域：" + hasData(opinionEvent.getRegion())});
        value.add(new String[]{"事件级别：" + hasData(opinionEvent.getEventLevel())});
        value.add(new String[]{"事件描述：" + hasData(opinionEvent.getDescription())});
        
        value.add(new String[]{"商家主体：" + hasData(opinionEvent.getMerchant())});
        value.add(new String[]{"品牌：" + hasData(opinionEvent.getBrand())});
        value.add(new String[]{"产品：" + hasData(opinionEvent.getProduct())});
        value.add(new String[]{"商家地址：" + hasData(opinionEvent.getAddress())});
        value.add(new String[]{"商家联系方式：" + hasData(opinionEvent.getMerchantTel())});
        value.add(new String[]{"消费者：" + hasData(opinionEvent.getConsumer())});
        value.add(new String[]{"消费者联系方式：" + hasData(opinionEvent.getConsumerTel())});
        
        value.add(new String[]{"事件关键词库"});
        value.add(new String[]{"包含关键词：" + hasData(opinionEvent.getIncludeWords())});
        value.add(new String[]{"主体关键词：" + hasData(opinionEvent.getKeywords())});
        value.add(new String[]{"排除关键词：" + hasData(opinionEvent.getExcludeWords())});
       
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
    	String eventSubData =  JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    	eventSubElement.setData(eventSubData);
        list.add(eventSubElement);
    }
    
    public String hasData(Object data) {
        if (data == null || String.valueOf(data).trim().equals("")) {
            return "无";
        } else {
            return String.valueOf(data);
        }
    }
    
    public String fileName(int cycle, Long id) {
        OpinionEvent opinionEvent = eventService.getEventChinese(id);
        eventName = opinionEvent.getEventName();
        eventTime = formatter.format(opinionEvent.getGmtCreate());
        if (cycle == 1) {
            eventName = "《"+eventName+"》舆情事件日报";
        } else if (cycle == 2) {
            eventName = "《"+eventName+"》舆情事件周报";
        } else if (cycle == 3) {
            eventName = "《"+eventName+"》舆情事件月报";
        } else if (cycle == 4) {
            eventName = "《"+eventName+"》历史舆情事件归档报告";
        } else if (cycle == 5) {
            eventName = "《"+eventName+"》舆情事件专报";
        }
        return eventName;
    }
    
    public void eventWholeTrend(ArrayList<ReportElementString> list, int cycle, Long id) {
        if (cycle == 1) {
            timeLabel = "";
            labelTwo = "近24小时监测图表跟踪分析";
        } else if (cycle == 2) {
            timeLabel = "";
            labelTwo = "近7天监测图表跟踪分析";
        } else if (cycle == 3) {
            timeLabel = "";
            labelTwo = "近30天监测图表跟踪分析";
        } else if (cycle == 4) {
            timeLabel = "（从监测开始日期至归档日期"+eventTime+"——"+fileTime+"）";
            labelTwo = "历史监测图表跟踪分析";
        } else if (cycle == 5) {
            timeLabel = "（从监测开始日期至今"+eventTime+"——"+opTime+"）";
            labelTwo = "创建事件至今监测图表跟踪分析";
        }
        infoTotal = String.valueOf(eventService.eventInfoTotal(id, cycle, false));
        warnTotal = String.valueOf(eventService.eventInfoTotal(id, cycle, true));
        Map map = eventService.eventWholeTrend(id, cycle);
        List<KeyValueVO> infoList = (List<KeyValueVO>) map.get("infoList");
        for (KeyValueVO vo : infoList) {
            vo.setKey("信息总量");
        }
        List<KeyValueVO> warnList = (List<KeyValueVO>) map.get("warnList");
        for (KeyValueVO vo : warnList) {
            vo.setKey("预警总量");
        }
        List<List<KeyValueVO>> listAll = new ArrayList<List<KeyValueVO>>();
        listAll.add(infoList);
        listAll.add(warnList);
        ReportElementString eventWholeElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "eventWholeTrend", "eventWholeTrendData");
        eventWholeElement.setData(computeTrendDate(listAll));
        eventWholeElement.setIndex(1);
        list.add(eventWholeElement);
        
    }
    
    public void eventSrcDis(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventSrcDis(id, cycle);
        ReportElementString eventSrcDisElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "mediaSpread", "mediaSpreadData");
        eventSrcDisElement.setData(computeTrend(listData));
        eventSrcDisElement.setIndex(1);
        list.add(eventSrcDisElement);
    }
    
    public void eventInfoTrend(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<List<KeyValueVO>> listData = eventService.eventInfoTrend(id, cycle);
        ReportElementString eventSrcDisElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "infoTrend", "infoTrendData");
        eventSrcDisElement.setData(computeTrendDate(listData));
        eventSrcDisElement.setIndex(1);
        list.add(eventSrcDisElement);
    }
    
    public void eventSrcActive(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventSrcActive(id, cycle);
        ReportElementString eventSrcActiveElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "websiteSpread", "websiteSpreadData");
        eventSrcActiveElement.setData(computeTrend(listData));
        eventSrcActiveElement.setIndex(1);
        list.add(eventSrcActiveElement);
        
        ReportElementString eventSrcActive2Element = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "websiteSpread2", "websiteSpread2Data");
        eventSrcActive2Element.setData(computeTrend(listData));
        eventSrcActive2Element.setIndex(1);
        list.add(eventSrcActive2Element);
    }
    
    public void keywords(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventKeywords(id, cycle);
        ReportElementString keywordsElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "keywords", "keywordsData");
        keywordsElement.setData(computeTrend(listData));
        keywordsElement.setIndex(1);
        list.add(keywordsElement);
    }
    
    public void eventDataType(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<KeyValueVO> listData = eventService.eventDataType(id, cycle);
        ReportElementString eventSrcActiveElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "dataType", "dataTypeData");
        
        eventSrcActiveElement.setData(computeTrend(listData));
        eventSrcActiveElement.setIndex(1);
        list.add(eventSrcActiveElement);
    }
    
    public void eventTrend(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        List<OpinionEventTrendStatistic>  opinions = (List<OpinionEventTrendStatistic>) eventService.eventTrend(id, cycle, 1, Integer.MAX_VALUE).get("opinions");
        ReportElementString eventTrendElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "eventTrend", "eventTrendData");
        String[] title = new String[]{"item"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc()};
        List<Object[]> value = new ArrayList<>();
        for (OpinionEventTrendStatistic evtStc : opinions) {
            value.add(new Object[]{"["+formatter2.format(evtStc.getPublishTime())+"]"+evtStc.getTitle()+"["+hasData(evtStc.getSource())+"] 相同文章数："+evtStc.getSimiliarCount()});
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        String value2 = JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
        eventTrendElement.setData(value2);
        eventTrendElement.setIndex(1);
        list.add(eventTrendElement);
    }
    
    public void eventOpinion(ArrayList<ReportElementString> list, int cycle, Long id) throws Exception {
        if (cycle == 1) {
            labelThree = "近24小时预警舆情信息列表";
        } else if (cycle == 2) {
            labelThree = "近7天预警舆情信息列表";
        } else if (cycle == 3) {
            labelThree = "近30天预警舆情信息列表";
        } else if (cycle == 4) {
            labelThree = "事件所有预警舆情信息列表";
        } else if (cycle == 5) {
            labelThree = eventTime +"至今预警舆情信息列表";
        }
        
        HashMap<String, Object> map = eventService.getEventInfoList(id, cycle, null, null, -1, 1, 100);
        
        allWarn = String.valueOf(map.get("total"));
        List<KeyValueVO> hotLevelStats = (List<KeyValueVO>) map.get("hotLevelStats");
        for (KeyValueVO vo : hotLevelStats) {
            String value = String.valueOf(vo.getValue()); 
            if (vo.getKey().equals("levelOne")) {
                firstWarn = value;
            } else if (vo.getKey().equals("levelTwo")) {
                secondWarn = value;
            } else if (vo.getKey().equals("levelThree")) {
                thirdWarn = value;
            }
        }
        List<KeyValueVO> emotionStats = (List<KeyValueVO>) map.get("emotionStats");
        for (KeyValueVO vo : emotionStats) {
            String value = vo.getValue().toString();
            String key = vo.getKey().toString();
            if (key.equals("0")) {
                midWarn = value;
            } else if (key.equals("1")) {
                crtWarn = value;
            } else if (key.equals("2")) {
                oppWarn = value;
            }
        }
        midWarn = midWarn == null ? "0" : midWarn;
        crtWarn = crtWarn == null ? "0" : crtWarn;
        oppWarn = oppWarn == null ? "0" : oppWarn;
        List<OpinionVO> opinions = (List<OpinionVO>) map.get("opinions");
        
        
        ReportElementString eventTrendElement = new ReportElementString(StructureEnum.GROUP_FOOTER, ElementEnum.REPORT_DEFINITION_TABLE,
            DataModelEnum.TABLE_DATA, "opinionInfo", "opinionInfoData");
        String[] title = new String[]{"title","summary","similiarCount","emotion","website","publishTime","hot","level","link"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc()
                                          , ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc()
                                          , ParamTypeEnum.STRING.getDesc()};
        List<Object[]> value = new ArrayList<>();
        int i = 1;
        for (OpinionVO vo : opinions) {
            String emotion = null;
            if (vo.getEmotion() == 0) {
                emotion = "中性";
            } else if (vo.getEmotion() == 1) {
                emotion = "正面";
            } else if (vo.getEmotion() == 2) {
                emotion = "负面";
            }
            value.add(new Object[]{i + "、" + vo.getTitle(), hasData(vo.getSummary()), vo.getSimiliarCount().toString(), emotion,
                                   vo.getWebsite()== null || vo.getWebsite().trim().equals("") ? "未知来源" : vo.getWebsite(), 
                                   formatter2.format(vo.getPublishTime()), vo.getHot().toString(),vo.getLevel().toString()+"级", hasData(vo.getLink())});
            i++;
        }
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        String value2 = JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
        eventTrendElement.setData(value2);
        eventTrendElement.setIndex(0);
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
    	if (cycle != 4) {
    	    eventOpinion(list, cycle, id);
    	}
        return list;
    }

    
}
