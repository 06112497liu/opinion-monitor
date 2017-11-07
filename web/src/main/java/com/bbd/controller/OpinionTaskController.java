package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.service.OpinionTaskService;
import com.bbd.service.vo.OpinionTaskListVO;
import com.mybatis.domain.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 舆情任务控制器
 * @author Liuweibo
 * @version Id: OpinionTaskController.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
@RestController
@RequestMapping(name = "api/opinion/task")
@Api(description = "舆情任务模块")
public class OpinionTaskController extends AbstractController{

    @Autowired
    private OpinionTaskService opinionTaskService;

    @ApiOperation(value = "当前用户待处理舆情列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "转发类型: 1. 请示，2. 回复", name = "transferType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "no/processed/list", method = RequestMethod.GET)
    public RestResult getUnProcessedList(Integer transferType) {
        PageList<OpinionTaskListVO> result = opinionTaskService.getUnProcessedList(transferType, getPageBounds());
        return RestResult.ok(result);
    }
}
    
    