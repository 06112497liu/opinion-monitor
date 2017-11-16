package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.WarnNotifier;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.util.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
            @ApiImplicitParam(value = "1级预警下限", name = "first", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(value = "2级预警下限", name = "second", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "3级预警下限", name = "third", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "hot/modify", method = RequestMethod.GET)
    public RestResult modifyHeatValue(Long eventId,
                                      Integer type,
                                      Integer first,
                                      @RequestParam(value = "second", defaultValue = "0") Integer second,
                                      @RequestParam(value = "third", defaultValue = "0") Integer third) {
        ValidateUtil.checkNull(type, CommonErrorCode.PARAM_ERROR, "预警类型不能为空");
        if(type != 1) ValidateUtil.checkAllNull(CommonErrorCode.PARAM_ERROR, first, second, third);
        else ValidateUtil.checkNull(first, CommonErrorCode.PARAM_ERROR);
        if(type != 3) ValidateUtil.checkNull(eventId, CommonErrorCode.BIZ_ERROR, "配置所属事件id不能为空");
        return RestResult.ok(settingService.modifyHeat(eventId, type, first, second, third));
    }

    @ApiOperation(value = "添加预警通知人", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "所属预警配置id", name = "settingId", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(value = "通知人", name = "notifier", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "邮件通知（0-否，1-是）", name = "emailNotify", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "邮件", name = "email", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "短信通知（0-否，1-是）", name = "smsNotify", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "电话", name = "phone", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "notifier/add", method = RequestMethod.GET)
    public RestResult addNotifier(WarnNotifier w) {
        ValidateUtil.checkAllNull(CommonErrorCode.PARAM_ERROR, w.getSettingId(), w.getNotifier());
        ValidateUtil.checkNotAllNull(CommonErrorCode.PARAM_ERROR, w.getEmail(), w.getPhone());
        Integer result = settingService.addNotifier(w);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "修改预警通知人信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(value = "通知人", name = "notifier", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "邮件通知（0-否，1-是）", name = "emailNotify", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "邮件", name = "email", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "短信通知（0-否，1-是）", name = "smsNotify", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "电话", name = "phone", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "notifier/modify", method = RequestMethod.GET)
    public RestResult ModifyNotifier(WarnNotifier w) {
        ValidateUtil.checkAllNull(CommonErrorCode.PARAM_ERROR, w.getId(), w.getNotifier());
        ValidateUtil.checkNotAllNull(CommonErrorCode.PARAM_ERROR, w.getEmail(), w.getPhone());
        return RestResult.ok(settingService.modifyNotifier(w));
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














    
    