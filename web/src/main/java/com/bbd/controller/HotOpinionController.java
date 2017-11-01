package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.service.OpinionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 热点舆情控制器
 * @author Liuweibo
 * @version Id: HotOpinionController.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "/hot/opinion/")
@Api(description = "热点舆情控制器")
public class HotOpinionController {

    @Autowired
    private OpinionService opinionService;

    @ApiOperation(value = "热点舆情列表查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询关键词", name = "keyword", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "媒体类型(1-新闻，2-网站，3-微信，4-论坛，5-微博，6-政务，7-其他)", name = "sourceType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public RestResult getHotOpinionList(String keyword, @RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan, Integer emotion, Integer sourceType) {
        return RestResult.ok(opinionService.getHotOpinionList(keyword, timeSpan, emotion, sourceType));
    }

}
    
    