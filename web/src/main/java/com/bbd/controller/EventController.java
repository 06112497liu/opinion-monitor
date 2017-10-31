/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.Api;
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
@Api(description = "监测事件")
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
        //opinionEvent.setCreateBy(createBy);
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
        //opinionEvent.setModifiedBy(modifiedBy);
    	eventService.modifyEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "显示事件", httpMethod = "GET")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "getEvent", method = RequestMethod.GET)
    public RestResult getEvent(OpinionEvent opinionEvent) {
        HashMap map= new HashMap();
        OpinionEvent event = eventService.getEvent(opinionEvent.getId()); 
        map.put("event", event);
        map.put("username", /*获取对应ID的用户名SSO*/event.getCreateBy());
        return RestResult.ok(map);
    }
    
    @ApiOperation(value = "删除事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "deleteEvent", method = RequestMethod.POST)
    public RestResult deleteEvent(OpinionEvent opinionEvent) {
       //opinionEvent.setModifiedBy(modifiedBy);
        eventService.deleteEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "归档事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "归档事由", name = "fileReason", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(value = "备注", name = "remark", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "fileEvent", method = RequestMethod.POST)
    public RestResult fileEvent(OpinionEvent opinionEvent) {
        eventService.modifyEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "事件列表", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "区域代码", name = "region", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(value = "事件分组", name = "eventGroup", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(value = "第几页", name = "pageNo", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "每页大小", name = "pageSize", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventList", method = RequestMethod.GET)
    public RestResult eventList(OpinionEvent opinionEvent, Integer pageNo, Integer pageSize) {
        return RestResult.ok(eventService.eventList(opinionEvent, pageNo, pageSize));
    }
    
    @ApiOperation(value = "事件分组、监管主体、事发区域、事件级别下拉列表", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件分组(A)、监管主体(B)、事发区域(C)、事件级别(D)、归档事由(E)", name = "parent", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "getDictionary", method = RequestMethod.GET)
    public RestResult getDictionary(OpinionDictionary opinionDictionary) {
        return RestResult.ok(eventService.getDictionary(opinionDictionary.getParent()));
    }
    
    @ApiOperation(value = "事件信息列表", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天", name = "cycle", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "舆情类型,空表示全部舆情，0表示中性舆情，1表示正面舆情，2表示负面舆情", name = "emotion", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "媒体类型,空表示全部，001表示新闻，002表示网站，003表示微信，004表示论坛，005表示微博，006表示政务，007表示其他", name = "source", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(value = "起始页号", name = "pageNo", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "每页大小", name = "pageSize", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventInfoList", method = RequestMethod.GET)
    public RestResult eventInfoList(Integer id, Integer cycle, Integer emotion, String source, Integer pageNo, Integer pageSize) {
        return RestResult.ok(eventService.getEventInfoList(id, cycle, emotion, source, pageNo, pageSize));
    }
    
   /* @ApiOperation(value = "图表跟踪分析/事件总体走势", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventInfoList", method = RequestMethod.GET)
    public RestResult eventWholeTrend(Integer id, Integer cycle) {
        return null;
       // return RestResult.ok(eventService.eventWholeTrend(id, cycle));
    }*/
    
}
