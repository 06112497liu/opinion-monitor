package com.bbd.service.report;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bbd.report.ReportEngine;
import com.bbd.report.enums.ExportEnum;
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

    public Integer generateReportByYear(int year, int quarter, ExportEnum exportEnum) {
        ArrayList<ReportElementString> list = createReportStruct(year,quarter,true);
        HashMap<String,Object> params = new HashMap<>();
        params.put("year",String.valueOf(year));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year+1);
        c.set(Calendar.MONTH,0);
        c.set(Calendar.DAY_OF_MONTH,1);
        params.put("publishDate", DateUtil.formatDateByPatten(c.getTime(),DateUtil.DATE_PATTERN_1));
        params.put("eventName","test");
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
/*
    
    *//** 1、企业活跃度指数概览 **//*
    public void livenessYear(ArrayList<ReportElementString> list, Integer year, Integer quarter,Boolean isYear) {
        ReportElementString livenessLabelElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.LABEL,
                DataModelEnum.TEXT_DATA, "livenessYear", "value");
        List<Object> livenessValues = new ArrayList<>();
        ReportUtil.addDate(livenessValues, year, isYear == false ? quarter : null);
        
        Map<Object, List<OneTrendVO>> trendData= livenessTrendService.getLivenessTrendByType(year, isYear == false ? quarter : null, "企业活跃度");
        String key = isYear == true ? String.valueOf(year) : year + "Q" + quarter;
        OneTrendVO oneTrendVO = ((OneTrendVO)ReportUtil.getDataByKey(trendData, key).get(0));
        livenessValues.add(oneTrendVO.getActivityIndex());	
        
        livenessLabelElement.setData(TextUtil.replaceParams(livenessYear, livenessValues));
        list.add(livenessLabelElement);

        ReportElementString livenessSubElement = new ReportElementString(StructureEnum.REPORT_HEADER, ElementEnum.REPORT_DEFINITION_TABLE,
                DataModelEnum.TABLE_DATA, "livenessTrend", "livenessTrendData");
        List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"type","num"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.DOUBLE.getDesc()};
        value.add(new String[]{"活跃:" + oneTrendVO.getActivityIndex() + "%", String.valueOf(oneTrendVO.getActivityIndex())});
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        value.add(new String[]{"不活跃:" + decimalFormat.format((100 - oneTrendVO.getActivityIndex())) + "%", String.valueOf(100 - oneTrendVO.getActivityIndex())});
    	logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
    	String livenessSubData =  JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
        livenessSubElement.setData(livenessSubData);
        list.add(livenessSubElement);
    }
    */
    /**
     * 构建报表数据
     * @param year
     * @param quarter
     * @return
     */
    private ArrayList<ReportElementString> createReportStruct(Integer year, Integer quarter,Boolean isYear) {
    	ArrayList<ReportElementString> list = new ArrayList<>();
    	if (isYear == false) {
    		 /** 1、企业活跃度指数概览 **/
    		//livenessQuarter(list, year, quarter, isYear);
    	} else {
    		//livenessYear(list, year, quarter, isYear);
    	}
    	 
        return list;
    }

    
}
