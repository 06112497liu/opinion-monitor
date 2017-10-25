/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bbd.RestResult;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionEvent;
import com.bbd.service.EventService;


@RestController
@RequestMapping("/api/event")
public class EventController extends AbstractController {

	@Autowired
	EventService eventService;
	
    @ApiOperation(value = "创建事件", httpMethod = "POST")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(value = "事件名称", name = "eventName", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件分组", name = "eventGroup", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "监管主体", name = "monitor", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事发区域", name = "region", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件级别", name = "eventLevel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件描述", name = "description", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家主体", name = "merchant", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "品牌", name = "brand", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家地址", name = "address", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家联系方式", name = "merchantTel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "消费者", name = "consumer", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "消费者联系方式", name = "consumerTel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "包含关键词", name = "includeWords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "主体关键词", name = "keywords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "排除关键词", name = "excludeWords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "包含关键词", name = "includeWords", dataType = "String", paramType = "query", required = true),
        })
    @RequestMapping(value = "createEvent", method = RequestMethod.POST)
    public RestResult createEvent(OpinionEvent opinionEvent) {
    	eventService.createEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "修改事件", httpMethod = "POST")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件名称", name = "eventName", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件分组", name = "eventGroup", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "监管主体", name = "monitor", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事发区域", name = "region", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件级别", name = "eventLevel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "事件描述", name = "description", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家主体", name = "merchant", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "品牌", name = "brand", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家地址", name = "address", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "商家联系方式", name = "merchantTel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "消费者", name = "consumer", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "消费者联系方式", name = "consumerTel", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "包含关键词", name = "includeWords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "主体关键词", name = "keywords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "排除关键词", name = "excludeWords", dataType = "String", paramType = "query", required = true),
    	@ApiImplicitParam(value = "包含关键词", name = "includeWords", dataType = "String", paramType = "query", required = true),
        })
    @RequestMapping(value = "modifyEvent", method = RequestMethod.POST)
    public RestResult modifyEvent(OpinionEvent opinionEvent) {
    	eventService.modifyEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "显示事件", httpMethod = "GET")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "getEvent", method = RequestMethod.GET)
    public RestResult getEvent(OpinionEvent opinionEvent) {
        return RestResult.ok(eventService.getEvent(opinionEvent.getId()));
    }
    
    @ApiOperation(value = "事件分组、监管主体、事发区域、事件级别下拉列表", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件分组(A)、监管主体(B)、事发区域(C)、事件级别(D)", name = "parent", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "getDictionary", method = RequestMethod.GET)
    public RestResult getDictionary(OpinionDictionary opinionDictionary) {
        return RestResult.ok(eventService.getDictionary(opinionDictionary.getParent()));
    }
    
}
