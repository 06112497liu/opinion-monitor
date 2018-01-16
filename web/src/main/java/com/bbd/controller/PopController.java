/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.job.service.MsgService;
import com.bbd.service.OpinionPopService;
import com.bbd.service.OpinionService;
import com.bbd.util.UserContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pop")
public class PopController extends AbstractController {
    @Autowired
    MsgService msgService;

    @Autowired
    private OpinionPopService popService;
    
    @ApiOperation(value = "事件弹窗", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "类型,1表示新增分级舆情,2表示事件新增舆情，3表示事件热度级别变化", name = "type", dataType = "Integer", paramType = "query", required = true),
        })
    @RequestMapping(method = RequestMethod.GET)
    public RestResult pop(Integer type) {
        Long userId = UserContext.getUser().getId();
       if (type == 2 || type == 3) {
           return RestResult.ok(msgService.getPop(userId, type));
       } else if (type == 1){
           return RestResult.ok(popService.opinionPopupWindowsMsg(userId, type));
       } else {
           throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "参数非法");
       }
    }
}
