/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.context.SessionContext;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.OpinionReportService;
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
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;
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
    
    @ApiOperation(value = "创建事件", httpMethod = "POST")
    
    @RequestMapping(value = "eventReport", method = RequestMethod.POST)
    public RestResult eventReport() throws Exception{
        eventReportService.generateReport(3, 4l);
        return RestResult.ok(9368+13419+15949+13299);
    }

    @ApiOperation(value = "舆情详情简报", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "opinion/detail", method = RequestMethod.GET)
    public RestResult opinionDetailReport(String uuid) throws Exception{
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        HttpServletResponse resp = SessionContext.getResponse();
        String filename = "舆情详情简报.pdf";
        OutputStream out = buildResponse(filename, resp);
        opinionReportService.generateDetailReport(uuid, out);
        return RestResult.ok("下载成功");
    }

    // 处理下载文件问题
    private OutputStream buildResponse(String fileName, HttpServletResponse response) throws IOException {
        response.addHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8")+";filename*=UTF-8''"+URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/x-msdownload;");
        return response.getOutputStream();
    }
    
}
