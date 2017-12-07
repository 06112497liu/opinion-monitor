package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.domain.KeyValueVO;
import com.bbd.report.enums.ElementEnum;
import com.bbd.report.model.ReportElementModel;
import com.bbd.report.model.TableDataModel;
import com.bbd.report.model.TextDataModel;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionReportService;
import com.bbd.service.OpinionService;
import com.bbd.service.param.OpinionBaseInfoReport;
import com.bbd.service.param.ReportTitle;
import com.bbd.service.report.ReportUtil;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: OpinionReportServiceImpl.java, v0.1 2017/12/7 Liuweibo Exp $$
 */
public class OpinionReportServiceImpl implements OpinionReportService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private OpinionService opinionService;

    private static final Logger           logger = LoggerFactory.getLogger(OpinionReportServiceImpl.class);
    private static final     Optional<String> source = Optional.of("report/opinionDetail.prpt");

    /**
     * 舆情详情报告
     * @param uuid
     */
    @Override
    public void generateDetailReport(String uuid) {

        // step-1：报表参数
        Map<String, Object> params = Maps.newHashMap();
        params.put("time", DateUtil.formatDateByPatten(new Date(), "yyyy年MM月dd日 HH:mm"));

        // step-2：报表元素数据
            // 舆情基本信息
        OpinionEsVO detail = esQueryService.getOpinionByUUID(uuid);
        OpinionBaseInfoReport baseInfo = BeanMapperUtil.map(detail, OpinionBaseInfoReport.class);
        Object[][] baseArr = ReportUtil.buildTwoArray(Arrays.asList(baseInfo));
        ReportElementModel baseModel = buildReportElementModel("baseDetail", "baseDetailData", baseArr, ReportTitle.opinionBaseInfoTitle);

            // 舆情热度趋势
        List<KeyValueVO> hotTrendInfo = opinionService.getOpinionHotTrend(uuid, 3);
        Object[][] hotTrendArr = ReportUtil.buildTwoArray(hotTrendInfo);
        ReportElementModel hotTrendModel = buildReportElementModel("hotTrend", "hotTrendData", hotTrendArr, ReportTitle.keyValueTile);

            // 关键词云
        List<KeyValueVO> wordCloudInfo = detail.getKeywords();
    }

    // TextDataModel --> ReportElementModel
    private ReportElementModel buildReportElemtModel(String name, Optional<String> source) {
        ReportElementModel model = new ReportElementModel();
        TextDataModel dataModel = new TextDataModel(source);
        model.setName(name);
        model.setDataName("value");
        model.setElementEnum(ElementEnum.LABEL);
        model.setDataModel(dataModel);
        return model;
    }

    // TableDataModel --> ReportElementModel
    private <T> ReportElementModel buildReportElementModel(String name, String dataName, Object[][] arrays, Object[] title) {
        ReportElementModel model = new ReportElementModel();
        TableDataModel dataModel = new TableDataModel(arrays, title);
        model.setName(name);
        model.setDataName(dataName);
        model.setElementEnum(ElementEnum.REPORT_DEFINITION_TABLE);
        model.setDataModel(dataModel);
        return model;
    }
}

































    