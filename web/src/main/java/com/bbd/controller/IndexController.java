/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.exception.BizErrorCode;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.OpinionService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author tjwang
 * @version $Id: IndexController.java, v 0.1 2017/10/31 0031 10:00 tjwang Exp $
 */
@RestController
@RequestMapping("/api/index")
@Api(description = "统计页面")
public class IndexController extends AbstractController {

    @Resource
    private IndexStatisticService indexStatisticService;

    @Resource
    private OpinionService opinionService;

    @ApiOperation(value = "系统运行情况统计", httpMethod = "GET")
    @RequestMapping(value = "/system/statistic", method = RequestMethod.GET)
    public RestResult getSystemSta() {
        return RestResult.ok(indexStatisticService.getSystemSta());
    }

    @ApiOperation(value = "预警舆情统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "状态：0. 未处理; 1. 已处理", name = "state", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(value = "时间跨度：1. 本日；2. 本周； 3. 本月； 4. 本年； 5. 全部。", name = "timeSpan", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "/stat/opinion/count", method = RequestMethod.GET)
    public RestResult getOpinionCountStatistic(Integer state, @RequestParam(value = "timeSpan", defaultValue = "3") Integer timeSpan) {
        return RestResult.ok(indexStatisticService.getOpinionCountStatistic(state, timeSpan));
    }

    @ApiOperation(value = "预警舆情统计坐标轴", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "状态：0. 未处理; 1. 已处理", name = "state", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "时间跨度：1. 本日；2. 本周； 3. 本月； 4. 本年； 5. 全部。", name = "timeSpan", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "/stat/opinion/count/coordinate", method = RequestMethod.GET)
    public RestResult getOpinionCountStatisticGroupTime(Integer state, @RequestParam(value = "timeSpan", defaultValue = "3") Integer timeSpan) {
        return RestResult.ok(indexStatisticService.getOpinionCountStatisticGroupTime(state, timeSpan));
    }

    @ApiOperation(value = "预警舆情top10", httpMethod = "GET")
    @RequestMapping(value = "/warn/opinion/top10", method = RequestMethod.GET)
    public RestResult getWarnOpinionTopTen() {
        return RestResult.ok(opinionService.getWarnOpinionTopTen());
    }

    @ApiOperation(value = "舆情数据库统计", httpMethod = "GET")
    @RequestMapping(value = "/db/statistic", method = RequestMethod.GET)
    public RestResult getDBsta() {
        return RestResult.ok(indexStatisticService.getSystemSta());
    }

    @ApiOperation(value = "舆情数据库坐标轴", httpMethod = "GET")
    @RequestMapping(value = "/opinion/db/coordinate", method = RequestMethod.GET)
    public RestResult getOpinionDBCoordinate() {
        return RestResult.ok(indexStatisticService.getOpinionDBCoordinate());
    }

    @ApiOperation(value = "本月关键词top10", httpMethod = "GET")
    @RequestMapping(value = "/keywords/top10", method = RequestMethod.GET)
    public RestResult getKeywordsTopTen() {
        return RestResult.ok(indexStatisticService.getKeywordsTopTen());
    }

    @ApiOperation(value = "舆情传播渠道分布", httpMethod = "GET")
    @RequestMapping(value = "/opinion/channel/trend", method = RequestMethod.GET)
    public RestResult getEventChannelTrend() {
        return RestResult.ok(indexStatisticService.getEventChannelTrend());
    }

    @ApiOperation(value = "历史舆情事件类别分布", httpMethod = "GET")
    @RequestMapping(value = "/event/class/trend", method = RequestMethod.GET)
    public RestResult getEventClassTrend() {
        return RestResult.ok(indexStatisticService.getEventClassTrend());
    }

    @ApiOperation(value = "历史舆情事件地域分布", httpMethod = "GET")
    @RequestMapping(value = "/event/area/trend", method = RequestMethod.GET)
    public RestResult getEventAreaTrend() {
        return RestResult.ok(indexStatisticService.getEventAreaTrend());
    }

}
