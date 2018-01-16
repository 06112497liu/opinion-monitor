/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;


import com.bbd.RestResult;
import com.bbd.job.service.MsgService;
import com.bbd.service.OpinionPopService;
import com.bbd.util.UserContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


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
        map.put("opinionLevelChange", popService.opinionPopupWindowsMsg(userId,1 ));
        map.put("eventNewOpinion", msgService.getPop(userId, 2));
        map.put("eventLevelChange", msgService.getPop(userId, 3));
        return RestResult.ok(map);
    }
}
