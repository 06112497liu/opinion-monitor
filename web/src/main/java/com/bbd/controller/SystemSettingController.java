package com.bbd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbd.RestResult;
import com.bbd.domain.MonitorKeywords;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.EsQueryService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.param.WarnNotifierParam;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: SystemSettingController.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
@RestController
@RequestMapping("/api/system")
@Api(description = "预警配置")
public class SystemSettingController {

    @Autowired
    private SystemSettingService settingService;

    @Autowired
    private EsQueryService esQueryService;

    @ApiOperation(value = "获取预警配置列表信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "预警类型（1. 事件新增观点预警；2.事件总体热度预警；3.舆情预警。）", name = "type", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(value = "配置所属事件id（预警类型为1或2，才设置这个参数）", name = "eventId", dataType = "Long", paramType = "query", required = false)
    })
    @RequestMapping(value = "setting/list", method = RequestMethod.GET)
    public RestResult getWarnSettingList(Integer type, Long eventId) {
        ValidateUtil.checkNull(type, CommonErrorCode.PARAM_ERROR, "预警类型不能为空");
        if(type != 3) ValidateUtil.checkNull(eventId, CommonErrorCode.PARAM_ERROR, "配置所属事件id不能为空");
        return RestResult.ok(settingService.getWarnSettingList(type, eventId));
    }

    @ApiOperation(value = "修改预警配置信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "预警类型（1. 事件新增观点预警；2.事件总体热度预警；3.舆情预警。）", name = "type", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(value = "配置所属事件id（预警类型为1或2，才设置这个参数）", name = "eventId", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(value = "是否启用弹窗（0-不启用；1-启用）", name = "popup", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(value = "1级预警下限", name = "first", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(value = "2级预警下限", name = "second", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "3级预警下限", name = "third", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "hot/modify", method = RequestMethod.GET)
    public RestResult modifyHeatValue(Long eventId,
                                      Integer type,
                                      Integer first,
                                      Integer popup,
                                      @RequestParam(value = "second", defaultValue = "0") Integer second,
                                      @RequestParam(value = "third", defaultValue = "0") Integer third) {
        ValidateUtil.checkNull(type, CommonErrorCode.PARAM_ERROR, "预警类型不能为空");
        if(type != 1) ValidateUtil.checkAllNull(CommonErrorCode.PARAM_ERROR, first, second, third);
        else ValidateUtil.checkNull(first, CommonErrorCode.PARAM_ERROR);
        if(type != 3) ValidateUtil.checkNull(eventId, CommonErrorCode.BIZ_ERROR, "配置所属事件id不能为空");
        return RestResult.ok(settingService.modifyHeat(eventId, type, popup, first, second, third));
    }

    @ApiOperation(value = "创建或修改通知人信息", httpMethod = "POST")
    @RequestMapping(value = "notifier/operate", method = RequestMethod.POST)
    public RestResult operateNotifier(@ApiParam(name = "通知人信息", value = "传入JSON") @RequestBody String data) {
        JSONObject obj = (JSONObject) JSONObject.parse(data);
        String jsonArr = obj.getString("data");
        List<WarnNotifierParam> list = JSONArray.parseArray(jsonArr, WarnNotifierParam.class);
        for (WarnNotifierParam w : list) {
            w.validate();
        }
        Integer result = settingService.operateNotifier(list);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "删除预警通知人信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "notifier/del", method = RequestMethod.GET)
    public RestResult delNotifier(Long id) {
        ValidateUtil.checkNull(id, CommonErrorCode.PARAM_ERROR, "预警通知人id不能为空");
        return RestResult.ok(settingService.delNotifier(id));
    }

    @ApiOperation(value = "添加舆情预警关键词库", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词字符串（用空格隔开）", name = "keywords", dataType = "Long", paramType = "query", required = true)
    })
    @RequestMapping(value = "keywords/add", method = RequestMethod.GET)
    public RestResult addKeywords(String keywords) {
        ValidateUtil.checkNull(keywords, CommonErrorCode.PARAM_ERROR, "关键词不能为空");
        Integer result = settingService.addKeyWords(keywords);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "删除舆情关键词", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词id", name = "id", dataType = "Long", paramType = "query", required = false)
    })
    @RequestMapping(value = "keywords/del", method = RequestMethod.GET)
    public RestResult delKeywords(Long id) {
        ValidateUtil.checkNull(id, CommonErrorCode.PARAM_ERROR, "id不能为空");
        Integer result = settingService.delKeyWords(id);
        return RestResult.ok(result);
    }


    @ApiOperation(value = "获取关键词列表", httpMethod = "GET")
    @RequestMapping(value = "keywords/list", method = RequestMethod.GET)
    public RestResult getKeywords() {
        Map<String, List<MonitorKeywords>> result = settingService.getKeywords();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "实时预警数量统计", httpMethod = "GET")
    @RequestMapping(value = "ins/warn/sta", method = RequestMethod.GET)
    public RestResult getInsWarnSta() {
        List<KeyValueVO> result = esQueryService.opinionInstant();
        return RestResult.ok(result);
    }

}














    
    