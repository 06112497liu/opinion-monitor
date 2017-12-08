package com.bbd.service.impl;

import com.bbd.bean.OpinionEsVO;
import com.bbd.domain.KeyValueVO;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.report.ReportEngine;
import com.bbd.report.enums.ElementEnum;
import com.bbd.report.enums.ExportEnum;
import com.bbd.report.enums.StructureEnum;
import com.bbd.report.model.ReportElementModel;
import com.bbd.report.model.TableDataModel;
import com.bbd.report.model.TextDataModel;
import com.bbd.service.EsQueryService;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.OpinionReportService;
import com.bbd.service.OpinionService;
import com.bbd.service.param.OpinionBaseInfoReport;
import com.bbd.service.param.OpinionStaReport;
import com.bbd.service.param.ReportTitle;
import com.bbd.service.report.ReportUtil;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.OpinionExtVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
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

    @Autowired
    private IndexStatisticService statisticService;

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
        ReportElementModel hotTrendModel = buildReportElementModel("hotTrend", "hotTrendData", hotTrendInfo, 2, ReportTitle.keyValueTitle);

            // 关键词云
        List<KeyValueVO> wordCloudInfo = detail.getKeywords();
        ReportElementModel wordCloudModel = buildReportElementModel("keywords", "keywordsData", wordCloudInfo, 1, ReportTitle.keyValueTitle);

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
    public void generateStaReport(OutputStream out, String type) throws NoSuchFieldException, IllegalAccessException {
        Date now = new Date();
        Integer state = buildType(type);
        DateTime firstWarnTime = BusinessUtils.getDateTimeWithStartTime(state);

        // step-1：报表全局参数
        Map<String, Object> params = Maps.newHashMap();
        params.put("time1", DateUtil.formatDateByPatten(now, "yyyy年MM月dd日 HH:mm"));
        params.put("time2", DateUtil.formatDateByPatten(now, "yyyy-MM-dd"));
        params.put("timeSpan", type);

        // step-2：报表元素
            // 1、2、3 级预警条数
        OpinionCountStatVO opinionLevelSta = statisticService.getOpinionCountStatistic(state);
        OpinionStaReport opinionStaInfo = BeanMapperUtil.map(opinionLevelSta, OpinionStaReport.class);
            // 舆情情感数量
        List<KeyValueVO> assectionSta = esQueryService.queryAffectionSta(firstWarnTime);
        for (KeyValueVO v : assectionSta) {
            Integer emotion = Integer.parseInt(v.getKey().toString());
            if ("0".equals(emotion)) opinionStaInfo.setNeutral((Long) v.getValue());
            if ("1".equals(emotion)) opinionStaInfo.setPositive((Long) v.getValue());
            if ("2".equals(emotion)) opinionStaInfo.setNegative((Long) v.getValue());
        }
        ReportElementModel opinionStaModel = buildReportElementModel("opinionSta", "opinionStaData", Arrays.asList(opinionStaInfo), ReportTitle.opinionBaseInfoTitle);

            // 舆情传播渠道
        List<KeyValueVO> channelDistributionInfo = statisticService.getOpinionChannelTrend(firstWarnTime);
        ReportElementModel channelModel = buildReportElementModel("channelDistribution", "channelDistributionData", channelDistributionInfo, ReportTitle.keyValueTitle);

            // 舆情信息概要
        esQueryService.queryWarningOpinion(firstWarnTime);
    }

    private Integer buildType(String type) {
        if ("日".equals(type)) return 1;
        if ("周".equals(type)) return 2;
        if ("月".equals(type)) return 3;
        else throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
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

    // TableDataModel --> ReportElementModel
    private <T> ReportElementModel buildReportElementModel(String name, String dataName, List<T> lists, Object[] title) {
        ReportElementModel model = new ReportElementModel();
        Object[][] arrays = ReportUtil.buildTwoArray(lists);
        TableDataModel dataModel = new TableDataModel(arrays, title);
        model.setName(name);
        model.setDataName(dataName);
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

































    