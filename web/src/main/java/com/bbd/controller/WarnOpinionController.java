package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.KeyValueVO;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.OpinionService;
import com.bbd.service.vo.HistoryOpinionDetailVO;
import com.bbd.service.vo.OpinionExtVO;
import com.bbd.service.vo.SimiliarNewsVO;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预警舆情控制器
 * @author Liuweibo
 * @version Id: WarnOpinionController.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "/api/warn/opinion/")
@Api(description = "预警舆情控制器")
public class WarnOpinionController extends AbstractController {

    @Resource
    private OpinionService opinionService;

    @ApiOperation(value = "预警舆情信息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "媒体类型(1-新闻，2-网站，3-微信，4-论坛，5-微博，6-政务，7-其他)", name = "mediaType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public RestResult getWarnOpinionList(@RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan, Integer emotion, Integer mediaType) {
        Map<String, Object> result = opinionService.getWarnOpinionList(timeSpan, emotion, mediaType, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "历史预警舆情列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "预警时间起", name = "startTime", dataType = "Date", paramType = "query", required = false),
            @ApiImplicitParam(value = "预警时间止", name = "endTime", dataType = "Date", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "媒体类型(1-新闻，2-网站，3-微信，4-论坛，5-微博，6-政务，7-其他)", name = "mediaType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "history/list", method = RequestMethod.GET)
    public RestResult getHistoryWarnOpinionList(@DateTimeFormat(pattern="yyyy-MM") Date startTime,
                                                @DateTimeFormat(pattern="yyyy-MM") Date endTime,
                                                Integer emotion, Integer mediaType) {
        if(startTime == null) startTime = DateTime.now().plusYears(-100).toDate();
        if(endTime == null) endTime = DateTime.now().toDate();
        Map<String, Object> result = opinionService.getHistoryWarnOpinionList(startTime, endTime, emotion, mediaType, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "预警舆情详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String uuid", paramType = "query", required = true)
    })
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public RestResult getWarnOpinionDetail(String uuid) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        OpinionExtVO rs = opinionService.getOpinionDetail(uuid);
        return RestResult.ok(rs);
    }

    @ApiOperation(value = "预警舆情相同文章信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String uuid", paramType = "query", required = true),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "news/list", method = RequestMethod.GET)
    public RestResult getWarnOpinionSimiliarNewsList(String uuid) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        List<SimiliarNewsVO> rs = opinionService.getOpinionSimiliarNewsList(uuid, getPageBounds());
        return RestResult.ok(rs);
    }

    @ApiOperation(value = "舆情热度变化趋势", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String uuid", paramType = "query", required = true),
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "hot/trend", method = RequestMethod.GET)
    public RestResult getOpinionHotTrend(String uuid, @RequestParam(name = "timeSpan", defaultValue = "1") Integer timeSpan) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        List<KeyValueVO> result = opinionService.getOpinionHotTrend(uuid, timeSpan);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "历史舆情详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String uuid", paramType = "query", required = true)
    })
    @RequestMapping(value = "/history/detail", method = RequestMethod.GET)
    public RestResult getOpinionHotTrend(String uuid) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        HistoryOpinionDetailVO result = opinionService.getHistoryWarnOpinionDetail(uuid);
        return RestResult.ok(result);
    }
}
    
    