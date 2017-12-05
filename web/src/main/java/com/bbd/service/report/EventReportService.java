package com.bbd.service.report;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bbd.domain.OpinionEvent;
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
import com.bbd.util.DateUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;

@Service
public class EventReportService {
    private static Logger logger = LoggerFactory.getLogger(EventReportService.class);
    @Resource
    EventService eventService;
   
    
    @Value("#{propertiesConfig['lifeCycle1']}")
    private String lifeCycle1;//生命周期
    
    @Value("#{propertiesConfig['lifeCycle2']}")
    private String lifeCycle2;//生命周期
    
    public Integer test(ExportEnum exportEnum) {
        return 0;
    }

    public Integer generateReport(int cycle, Long id) {
        ArrayList<ReportElementString> list = createReportStruct(cycle, id);
        HashMap<String,Object> params = new HashMap<>();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String opTime = formatter.format(currentTime);
        params.put("opTime", opTime);
        params.put("eventName","ABCD");
        params.put("infoTotal","888");
        params.put("warnTotal","200");
        params.put("eventTime","2017-11-01");
        params.put("allWarn","60");
        params.put("firstWarn","10");
        params.put("secondWarn","20");
        params.put("thirdWarn","30");
        params.put("crtWarn","40");
        params.put("oppWarn","20");
        params.put("timeLabel","从事件创建至今2017-11-01到" + opTime);
        params.put("labelTwo","监测历史图表跟踪分析");
        params.put("labelThree","事件今日预警舆情信息列表");
        
        
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

    
    public void eventDetail(ArrayList<ReportElementString> list, int cycle, Long id) {
        ReportElementString eventSubElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
                DataModelEnum.TABLE_DATA, "eventDetail", "eventDetailData");
        List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"item"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc()};
        OpinionEvent opinionEvent = eventService.getEventChinese(id);
        value.add(new String[]{"事件名称:" + opinionEvent.getEventName()});
        value.add(new String[]{"事件实时总热度:" + opinionEvent.getHot()});
        value.add(new String[]{"监测账号:" + opinionEvent.getCreateBy()});
        value.add(new String[]{"监测时间:" + opinionEvent.getGmtCreate() + opinionEvent.getGmtFile()});
        
        logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
    	String eventSubData =  JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    	eventSubElement.setData(eventSubData);
        list.add(eventSubElement);
    }
    
    /**
     * 构建报表数据
     * @param year
     * @param quarter
     * @return
     */
    private ArrayList<ReportElementString> createReportStruct(int cycle, Long id) {
    	ArrayList<ReportElementString> list = new ArrayList<>();
    	eventDetail(list, 4, id);
        return list;
    }

    
}
