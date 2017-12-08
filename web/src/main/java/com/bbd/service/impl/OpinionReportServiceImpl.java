package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.domain.KeyValueVO;
import com.bbd.report.ReportEngine;
import com.bbd.report.enums.ElementEnum;
import com.bbd.report.enums.ExportEnum;
import com.bbd.report.enums.StructureEnum;
import com.bbd.report.model.ReportElementModel;
import com.bbd.report.model.TableDataModel;
import com.bbd.report.model.TextDataModel;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionReportService;
import com.bbd.service.OpinionService;
import com.bbd.service.param.OpinionBaseInfoReport;
import com.bbd.service.param.ReportTitle;
import com.bbd.service.report.ReportUtil;
import com.bbd.service.vo.OpinionExtVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: OpinionReportServiceImpl.java, v0.1 2017/12/7 Liuweibo Exp $$
 */
@Service
public class OpinionReportServiceImpl implements OpinionReportService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private OpinionService opinionService;

    private static final Logger           logger = LoggerFactory.getLogger(OpinionReportServiceImpl.class);
    private static final     Optional<String> detailSource = Optional.of("report/opinionDetail.prpt");

    // 处理下载文件问题
    private OutputStream buildResponse(String fileName, HttpServletResponse response) throws IOException {
        response.addHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8")+";filename*=UTF-8''"+URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/x-msdownload;");
        return response.getOutputStream();
    }

    /**
     * 舆情详情报告
     * @param out
     * @param detail
     */
    @Override
    public void generateDetailReport(OutputStream out, OpinionEsVO detail) {

        // step-1：报表参数
        Map<String, Object> params = Maps.newHashMap();
        params.put("time", DateUtil.formatDateByPatten(new Date(), "yyyy年MM月dd日 HH:mm"));
        params.put("content", detail.getContent());

        // step-2：报表元素数据
        // 舆情基本信息
        OpinionBaseInfoReport baseInfo = BeanMapperUtil.map(detail, OpinionBaseInfoReport.class);
        baseInfo.setEmotionDesc(buildEmotionDesc(baseInfo.getEmotion()));
        baseInfo.setLevelDesc(buildLevleDesc(baseInfo.getLevel()));
        ReportElementModel baseModel = buildReportElementModel("baseDetail", "baseDetailData", Arrays.asList(baseInfo), 3, ReportTitle.opinionBaseInfoTitle);

            // 舆情热度趋势
        List<KeyValueVO> hotTrendInfo = opinionService.getOpinionHotTrend(detail.getUuid(), 3);
        ReportElementModel hotTrendModel = buildReportElementModel("hotTrend", "hotTrendData", hotTrendInfo, 2, ReportTitle.keyValueTile);

            // 关键词云
        List<KeyValueVO> wordCloudInfo = detail.getKeywords();
        ReportElementModel wordCloudModel = buildReportElementModel("keywords", "keywordsData", wordCloudInfo, 1, ReportTitle.keyValueTile);

        ArrayListMultimap<StructureEnum, ReportElementModel> elements = buildArrayListMultimap(StructureEnum.GROUP_FOOTER, baseModel, hotTrendModel, wordCloudModel);

        ReportEngine reportEngine = new ReportEngine();
        reportEngine.generateReport(detailSource, elements, params, ExportEnum.PDF, out);
    }

    /**
     * 预警舆情（日、周、月）报
     * @param out
     * @param type
     */
    @Override
    public void generateStaReport(OutputStream out, String type) {

    }

    private String buildEmotionDesc(Integer emotion) {
        if (emotion == 0) return "中性";
        if (emotion == 1) return "正面";
        if (emotion == 2) return "负面";
        return "未知";
    }

    private String buildLevleDesc(Integer level) {
        if (level == 1) return "1级";
        if (level == 2) return "2级";
        if (level == 3) return "3级";
        return "热点";
    }

    // 构建ReportElementModel
    private <T> ReportElementModel buildReportElementModel(String name, String dataName, List<T> lists, int index, Object[] title) {
        ReportElementModel model = new ReportElementModel();
        Object[][] arrays = ReportUtil.buildTwoArray(lists);
        TableDataModel dataModel = new TableDataModel(arrays, title);
        model.setName(name);
        model.setDataName(dataName);
        model.setIndex(index);
        model.setElementEnum(ElementEnum.REPORT_DEFINITION_TABLE);
        model.setDataModel(dataModel);
        return model;
    }

    private ArrayListMultimap<StructureEnum, ReportElementModel> buildArrayListMultimap(StructureEnum structureEnum, ReportElementModel... params) {
        ArrayListMultimap<StructureEnum, ReportElementModel> list = ArrayListMultimap.create();
        for (ReportElementModel model : params) {
            list.put(structureEnum, model);
        }
        return list;
    }


}

































    