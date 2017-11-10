package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.OpinionService;
import com.bbd.service.vo.OpinionVO;
import com.bbd.util.ValidateUtil;
import com.mybatis.domain.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 热点舆情控制器
 * @author Liuweibo
 * @version Id: HotOpinionController.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "/hot/opinion/")
@Api(description = "热点舆情控制器")
public class HotOpinionController extends AbstractController {

    @Resource
    private OpinionService opinionService;

    @ApiOperation(value = "推荐热点舆情top100", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "recommend/list", method = RequestMethod.GET)
    public RestResult getHotOpinionListTop100(@RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan, Integer emotion) {
        PageList<OpinionVO> result = opinionService.getHotOpinionListTop100(timeSpan, emotion, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "热点舆情关键词模糊查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "搜索关键词", name = "keyword", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "时间周期(1-24小时，2-7天，3-30天)", name = "timeSpan", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "舆情类型(0-中性舆情，1-正面舆情，2-负面舆情)", name = "emotion", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "search/list", method = RequestMethod.GET)
    public RestResult getHotOpinionList(@RequestParam(value = "timeSpan", defaultValue = "1") Integer timeSpan,
                                        String keyword,
                                        Integer emotion) {
        PageList<OpinionVO> result = opinionService.getHotOpinionList(keyword, timeSpan, emotion, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "热点舆情详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public RestResult getWarnOpinionDetail(String uuid) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        return RestResult.ok(opinionService.getOpinionDetail(uuid));
    }

    @ApiOperation(value = "历史关键词搜索", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "搜索关键词", name = "keyWord", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "history/keywords", method = RequestMethod.GET)
    public RestResult getHistoryWordSearch(String keyWord) {
        ValidateUtil.checkNull(keyWord, CommonErrorCode.PARAM_ERROR, "keyWord不能为空");
        return RestResult.ok(opinionService.getHistoryWordSearch(keyWord));
    }

}
    
    