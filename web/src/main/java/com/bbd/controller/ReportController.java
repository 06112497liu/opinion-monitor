/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.bean.OpinionEsVO;
import com.bbd.context.SessionContext;
import com.bbd.domain.WarnSetting;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.*;
import com.bbd.service.vo.OpinionExtVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.DateUtil;
import com.bbd.util.ValidateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bbd.RestResult;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionEvent;
import com.bbd.report.enums.ExportEnum;
import com.bbd.service.report.EventReportService;
import com.bbd.util.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/report")
@Api(description = "报告模块")
public class ReportController extends AbstractController {

    @Autowired
    EventReportService eventReportService;

    @Autowired
    private OpinionReportService opinionReportService;

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private SystemSettingService systemSettingService;
    
    @ApiOperation(value = "事件报告", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天，4表示历史，5表示专报（创建至今）", name = "cycle", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventReport", method = RequestMethod.GET)
    public RestResult eventReport(Long id, Integer cycle) throws Exception{
        HttpServletResponse resp = SessionContext.getResponse();
        OutputStream out = buildResponse(eventReportService.fileName(cycle, id)+".pdf", resp);
        eventReportService.generateReport(cycle, id, out);
        return RestResult.ok("下载成功");
    }

    @ApiOperation(value = "舆情详情简报", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "opinion/detail", method = RequestMethod.GET)
    public RestResult opinionDetailReport(String uuid) throws Exception{
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        OpinionExtVO opinionDetail = getOpinionDetail(uuid);
        HttpServletResponse resp = SessionContext.getResponse();
        String filename = "《" + opinionDetail.getTitle() + "》舆情详情简报.pdf";
        OutputStream out = buildResponse(filename, resp);
        opinionReportService.generateDetailReport(out, opinionDetail);
        return RestResult.ok("下载成功");
    }

    @ApiOperation(value = "预警舆情（日、周、月）报", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "报告类型（1-日、2-周、3-月）", name = "type", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "opinion/sta", method = RequestMethod.GET)
    public RestResult opinionStaReport(Integer type) throws Exception{
        String typeDesc = getTimeSpanStr(type);
        HttpServletResponse resp = SessionContext.getResponse();
        String filename = "消费舆情监测预警系统预警舆情" + typeDesc + "报"+ DateUtil.formatDateByPatten(new Date(), "yyyy-MM-dd HH:mm") +".pdf";
        OutputStream out = buildResponse(filename, resp);
        opinionReportService.generateStaReport(out, type);
        return RestResult.ok("下载成功");
    }

    private OpinionExtVO getOpinionDetail(String uuid) {
        // 舆情详情
        OpinionEsVO o = esQueryService.getOpinionByUUID(uuid);
        OpinionExtVO result = BeanMapperUtil.map(o, OpinionExtVO.class);
        // 判断预警级别
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot(), setting);
        result.setLevel(level);
        return result;
    }

    private String getTimeSpanStr(Integer type) {
        String rs;
        switch (type) {
            case 1:
                rs = "日";
               break;
            case 2:
                rs = "周";
                break;
            case 3:
                rs = "月";
                break;
            default:
                throw new ApplicationException(CommonErrorCode.PARAM_ERROR);
        }
        return rs;
    }

    // 处理下载文件问题
    private OutputStream buildResponse(String fileName, HttpServletResponse response) throws IOException {
        response.addHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8")+";filename*=UTF-8''"+URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/x-msdownload;");
        return response.getOutputStream();
    }
    
}
