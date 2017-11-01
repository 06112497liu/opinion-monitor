package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.OpinionService;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 预警舆情控制器
 * @author Liuweibo
 * @version Id: WarnOpinionController.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "/api/warn/opinion/")
@Api(description = "预警舆情控制器")
public class WarnOpinionController extends AbstractController {

    @Resource(name = "opinionMockServiceImpl")
    private OpinionService opinionService;

    @ApiOperation(value = "预警舆情信息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "媒体类型(1-新闻，2-网站，3-微信，4-论坛，5-微博，6-政务，7-其他)", name = "sourceType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public RestResult getWarnOpinionList(@RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan, Integer emotion, Integer sourceType) {
        return RestResult.ok(opinionService.getWarnOpinionList(timeSpan, emotion, sourceType));
    }

    @ApiOperation(value = "舆情列表媒体类型分布", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情级别(1-预警舆情，2-热点舆情)", name = "rank", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "media/trend", method = RequestMethod.GET)
    public RestResult getWarnOpinionMediaTrend(@RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan, Integer emotion, Integer rank) {
        ValidateUtil.checkNull(rank, CommonErrorCode.PARAM_ERROR, "rank不能为空");
        return RestResult.ok(opinionService.getWarnOpinionMediaTrend(timeSpan, emotion, rank));
    }



}
    
    