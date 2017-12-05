/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
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


@RestController
@RequestMapping("/api/report")
@Api(description = "监测事件")
public class ReportController extends AbstractController {

    @Autowired
    EventReportService eventReportService;
    
    @ApiOperation(value = "创建事件", httpMethod = "POST")
    
    @RequestMapping(value = "eventReport", method = RequestMethod.POST)
    public RestResult eventReport() throws Exception{
        eventReportService.generateReport(3, 4l);
        return RestResult.ok(9368+13419+15949+13299);
    }
    
    
}
