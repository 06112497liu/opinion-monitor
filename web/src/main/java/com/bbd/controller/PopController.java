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
import java.util.HashMap;
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
    @RequestMapping(method = RequestMethod.GET)
    public RestResult pop() {
        Long userId = UserContext.getUser().getId();
        HashMap map = new HashMap();
        map.put("eventNewOpinion", msgService.getPop(userId, 2));
        map.put("000", "000");
        map.put("eventLevelChange", msgService.getPop(userId, 3));
        return RestResult.ok(map);
    }
}
