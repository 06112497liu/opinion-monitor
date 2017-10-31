/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author tjwang
 * @version $Id: IndexController.java, v 0.1 2017/10/31 0031 10:00 tjwang Exp $
 */
@RestController
@RequestMapping("/api/index")
public class IndexController extends AbstractController {

    @Resource(name = "indexStatisticMockService")
    private IndexStatisticService indexStatisticService;

    @RequestMapping(value = "/stat/opinion/count", method = RequestMethod.GET)
    public RestResult getOpinionCountStatistic(OpinionCountStatQueryParam param) {
        return RestResult.ok(indexStatisticService.getOpinionCountStatistic(param));
    }

}
