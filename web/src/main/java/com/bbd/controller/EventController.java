/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bbd.RestResult;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionEvent;
import com.bbd.service.EventService;
import com.bbd.util.UserContext;


@RestController
@RequestMapping("/api/event")
@Api(description = "监测事件")
public class EventController extends AbstractController {

    @Autowired
    EventService eventService;
    
    @ApiOperation(value = "创建事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "舆情ID", name = "uuid", dataType = "String", paramType = "query", required = true),
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
        @ApiImplicitParam(value = "排除关键词", name = "excludeWords", dataType = "String", paramType = "query", required = true)
        })
    @RequestMapping(value = "createEvent", method = RequestMethod.POST)
    public RestResult createEvent(OpinionEvent opinionEvent) throws IOException, ExecutionException, InterruptedException {
        opinionEvent.setCreateBy(UserContext.getUser().getId());
        eventService.createEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "修改事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
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
        @ApiImplicitParam(value = "排除关键词", name = "excludeWords", dataType = "String", paramType = "query", required = true)
        })
    @RequestMapping(value = "modifyEvent", method = RequestMethod.POST)
    public RestResult modifyEvent(OpinionEvent opinionEvent) {
        opinionEvent.setModifiedBy(UserContext.getUser().getId());
        eventService.modifyEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "显示事件", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "getEvent", method = RequestMethod.GET)
    public RestResult getEvent(OpinionEvent opinionEvent) {
        HashMap map= new HashMap();
        OpinionEvent event = eventService.getEvent(opinionEvent.getId()); 
        map.put("event", event);
        map.put("user", UserContext.getUser());
        return RestResult.ok(map);
    }
    @ApiOperation(value = "显示事件", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "getEventChinese", method = RequestMethod.GET)
    public RestResult getEventChinese(OpinionEvent opinionEvent) {
        HashMap map= new HashMap();
        OpinionEvent event = eventService.getEventChinese(opinionEvent.getId()); 
        map.put("event", event);
        return RestResult.ok(map);
    }
    
    @ApiOperation(value = "删除事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "deleteEvent", method = RequestMethod.POST)
    public RestResult deleteEvent(OpinionEvent opinionEvent) {
        opinionEvent.setModifiedBy(UserContext.getUser().getId());
        eventService.deleteEvent(opinionEvent);
        return RestResult.ok();
    }
    
    @ApiOperation(value = "归档事件", httpMethod = "POST")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "归档事由", name = "fileReason", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(value = "备注", name = "remark", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "fileEvent", method = RequestMethod.POST)
    public RestResult fileEvent(OpinionEvent opinionEvent) throws ParseException {
        opinionEvent.setFileBy(UserContext.getUser().getId());
        eventService.fileEvent(opinionEvent);
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
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天", name = "cycle", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "舆情类型,空表示全部舆情，0表示中性舆情，1表示正面舆情，2表示负面舆情", name = "emotion", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(value = "媒体类型(1-新闻，2-网站，3-微信，4-论坛，5-微博，6-政务，7-其他)", name = "mediaType", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(value = "起始页号", name = "pageNo", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "每页大小", name = "pageSize", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventInfoList", method = RequestMethod.GET)
    public RestResult eventInfoList(Long id, Integer cycle, Integer emotion, Integer mediaType, Integer pageNo, Integer pageSize) {
        return RestResult.ok(eventService.getEventInfoList(id, cycle, emotion, mediaType, pageNo, pageSize));
    }
    
    @ApiOperation(value = "事件信息列表/媒体类型标签", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天", name = "cycle", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "舆情类型,空表示全部舆情，0表示中性舆情，1表示正面舆情，2表示负面舆情", name = "emotion", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "eventLabelList", method = RequestMethod.GET)
    public RestResult eventLabelList(Long id, Integer cycle, Integer emotion) {
        return RestResult.ok(eventService.eventLabelList(id, cycle, emotion));
    }
    
    @ApiOperation(value = "事件信息列表/图表跟踪分析/信息总量", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventInfoTotal", method = RequestMethod.GET)
    public RestResult eventInfoTotal(Long id, Integer cycle) {
        return RestResult.ok(eventService.eventInfoTotal(id, cycle));
    }
    
    @ApiOperation(value = "事件信息列表/预警事件总热度", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "eventHotValue", method = RequestMethod.GET)
    public RestResult eventHotValue(Long id) {
        return RestResult.ok(eventService.eventHotValue(id));
    }
    
    @ApiOperation(value = "图表跟踪分析/事件总体走势", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventWholeTrend", method = RequestMethod.GET)
    public RestResult eventWholeTrend(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventWholeTrend(id, cycle));
    }
    
    @ApiOperation(value = "图表跟踪分析/舆情事件信息传播渠道分布", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventSrcDis", method = RequestMethod.GET)
    public RestResult eventSrcDis(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventSrcDis(id, cycle));
    }
    
    @ApiOperation(value = "图表跟踪分析/信息走势图", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventInfoTrend", method = RequestMethod.GET)
    public RestResult eventInfoTrend(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventInfoTrend(id, cycle));
    }
    
    @ApiOperation(value = "图表跟踪分析/媒体活跃度和媒体来源占比", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventSrcActive", method = RequestMethod.GET)
    public RestResult eventSrcActive(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventSrcActive(id, cycle));
    }
    
    @ApiOperation(value = "图表跟踪分析/事件走势", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "起始页号", name = "pageNo", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "每页大小", name = "pageSize", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventTrend", method = RequestMethod.GET)
    public RestResult eventTrend(Long id, Integer cycle, Integer pageNo, Integer pageSize) {
       return RestResult.ok(eventService.eventTrend(id, cycle, pageNo, pageSize));
    }
    
    @ApiOperation(value = "图表跟踪分析/事件关键词云", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventKeywords", method = RequestMethod.GET)
    public RestResult eventKeywords(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventKeywords(id, cycle));
    }
    
    @ApiOperation(value = "图表跟踪分析/数据类型", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件ID", name = "id", dataType = "Long", paramType = "query", required = true),
        @ApiImplicitParam(value = "时间周期,1表示24小时，2表示7天，3表示30天, 4表示历史", name = "cycle", dataType = "Integer", paramType = "query", required = true)
        })
    @RequestMapping(value = "eventDataType", method = RequestMethod.GET)
    public RestResult eventDataType(Long id, Integer cycle) {
       return RestResult.ok(eventService.eventDataType(id, cycle));
    }
    
    @ApiOperation(value = "舆情首页/舆情事件类别分布", httpMethod = "GET")
    @RequestMapping(value = "eventTypeDis", method = RequestMethod.GET)
    public RestResult eventTypeDis() {
       return RestResult.ok(eventService.eventTypeDis());
    }
    
    @ApiOperation(value = "舆情首页/舆情事件地区分布", httpMethod = "GET")
    @RequestMapping(value = "eventRegionDis", method = RequestMethod.GET)
    public RestResult eventRegionDis() {
       return RestResult.ok(eventService.eventRegionDis());
    }
    
    @ApiOperation(value = "历史舆情事件", httpMethod = "GET")
    @ApiImplicitParams({ 
        @ApiImplicitParam(value = "事件级别,1表示一级事件，2表示二级事件，3表示三级事件", name = "eventLevel", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(value = "地区,空表示全市", name = "region", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(value = "开始时间", name = "startTime", dataType = "Date", paramType = "query", required = true),
        @ApiImplicitParam(value = "结束时间", name = "endTime", dataType = "Date", paramType = "query", required = true),
        @ApiImplicitParam(value = "起始页号", name = "pageNo", dataType = "Integer", paramType = "query", required = true),
        @ApiImplicitParam(value = "每页大小", name = "pageSize", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "hisEventList", method = RequestMethod.GET)
    public RestResult hisEventList(String eventLevel, String region, @DateTimeFormat(pattern="yyyy-MM-dd")Date startTime, 
                                   @DateTimeFormat(pattern="yyyy-MM-dd")Date endTime, Integer pageNo, Integer pageSize) {
        return RestResult.ok(eventService.getHisEventList(eventLevel, region, startTime, endTime, pageNo, pageSize));
    }
    
}
