package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.service.EsInitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweibo
 * @version Id: InitEsDataController.java, v0.1 2017/12/4 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "/api/es/")
@Api(description = "es操作数据初始化")
public class InitEsDataController {

    @Autowired
    private EsInitService esInitService;

    @ApiOperation(value = "删除对舆情的opOwner字段", httpMethod = "GET")
    @RequestMapping(value = "/opOwner/del", method = RequestMethod.GET)
    public RestResult delOpOwner() {
        esInitService.delOpOwnerField();
        return RestResult.ok();
    }

    @ApiOperation(value = "删除对舆情的operators字段", httpMethod = "GET")
    @RequestMapping(value = "/operators/del", method = RequestMethod.GET)
    public RestResult delOperators() {
        esInitService.delOperatorsField();
        return RestResult.ok();
    }

    @ApiOperation(value = "删除对舆情的transferType字段", httpMethod = "GET")
    @RequestMapping(value = "/transferType/del", method = RequestMethod.GET)
    public RestResult delTransferType() {
        esInitService.delTransferTypeField();
        return RestResult.ok();
    }

    @ApiOperation(value = "删除对舆情的opStatus字段", httpMethod = "GET")
    @RequestMapping(value = "/opStatus/del", method = RequestMethod.GET)
    public RestResult delOpStatus() {
        esInitService.delOpStatusField();
        return RestResult.ok();
    }

    @ApiOperation(value = "删除舆情操作记录索引", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引", name = "index", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/index/del", method = RequestMethod.GET)
    public RestResult delOpOwner(String index) {
        esInitService.delOpRecord(index);
        return RestResult.ok();
    }

}

























    
    