package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.OpinionDictionary;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.DictionaryService;
import com.bbd.service.UserService;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Liuweibo
 * @version Id: DictionaryController.java, v0.1 2017/11/17 Liuweibo Exp $$
 */
@RestController
@RequestMapping("/api/dictionary/")
@Api(description = "字典控制器")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private UserService userService;


    @ApiOperation(value = "字典列表查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "下拉列表类型", name = "type", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public RestResult deleteEvent(String type) {
        ValidateUtil.checkNull(type, CommonErrorCode.PARAM_ERROR, "type不能为空");
        List<OpinionDictionary> list = dictionaryService.queryDictionary(type);
        return RestResult.ok(list);
    }

    @ApiOperation(value = "转发用户列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "区域", name = "region", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "transfer/list", method = RequestMethod.GET)
    public RestResult getTransferUsers(String region) {
        List<OpinionDictionary> rs = userService.getTransferUsers(region);
        return RestResult.ok(rs);
    }
}