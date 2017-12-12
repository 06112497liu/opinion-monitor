/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.KeyValueVO;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.OpinionService;
import com.bbd.service.vo.DBStaVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.SystemStaVO;
import com.bbd.service.vo.WarnOpinionTopTenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public RestResult getSystemSta() throws NoSuchFieldException, IllegalAccessException {
        SystemStaVO result = indexStatisticService.getSystemSta();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "预警舆情统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间跨度：1. 本日；2. 本周； 3. 本月； 4. 本年； 5. 全部。", name = "timeSpan", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "/stat/opinion/count", method = RequestMethod.GET)
    public RestResult getOpinionCountStatistic(@RequestParam(value = "timeSpan", defaultValue = "3") Integer timeSpan) throws NoSuchFieldException, IllegalAccessException {
        OpinionCountStatVO result = indexStatisticService.getOpinionCountStatistic(timeSpan);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "预警舆情统计坐标轴", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间跨度：1. 本日；2. 本周； 3. 本月； 4. 本年； 5. 全部。", name = "timeSpan", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "/stat/opinion/count/coordinate", method = RequestMethod.GET)
    public RestResult getOpinionCountStatisticGroupTime(@RequestParam(value = "timeSpan", defaultValue = "3") Integer timeSpan) {
        Map<String, List<KeyValueVO>> result = indexStatisticService.getOpinionCountStatisticGroupTime(timeSpan);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "预警舆情top10", httpMethod = "GET")
    @RequestMapping(value = "/warn/opinion/top10", method = RequestMethod.GET)
    public RestResult getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVO> rs = opinionService.getWarnOpinionTopTen();
        return RestResult.ok(rs);
    }

    @ApiOperation(value = "舆情数据库统计", httpMethod = "GET")
    @RequestMapping(value = "/db/statistic", method = RequestMethod.GET)
    public RestResult getDBsta() throws NoSuchFieldException, IllegalAccessException {
        DBStaVO result = indexStatisticService.getDBsta();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "舆情数据库坐标轴", httpMethod = "GET")
    @RequestMapping(value = "/opinion/db/coordinate", method = RequestMethod.GET)
    public RestResult getOpinionDBCoordinate() {
        Map<String, List<KeyValueVO>> result = indexStatisticService.getOpinionDBCoordinate();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "本月关键词top10", httpMethod = "GET")
    @RequestMapping(value = "/keywords/top10", method = RequestMethod.GET)
    public RestResult getKeywordsTopTen() {
        List<KeyValueVO> result = indexStatisticService.getKeywordsTopTen();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "舆情传播渠道分布", httpMethod = "GET")
    @RequestMapping(value = "/opinion/channel/trend", method = RequestMethod.GET)
    public RestResult getEventChannelTrend() {
        List<KeyValueVO> rs = indexStatisticService.getOpinionChannelTrend();
        return RestResult.ok(rs);
    }
}
