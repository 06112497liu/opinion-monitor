package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweibo
 * @version Id: StatisticsController.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
@RestController
@RequestMapping("/api/statistics")
@Api(description = "统计接口")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation(value = "获取预警舆情top10", httpMethod = "GET")
    @RequestMapping(value = "warn/opinion/top10", method = RequestMethod.GET)
    public RestResult getWarnOpinionTopTen() {
        return RestResult.ok(statisticsService.getWarnOpinionTopTen());
    }

}
    
    