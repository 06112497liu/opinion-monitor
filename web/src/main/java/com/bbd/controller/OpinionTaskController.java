package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.controller.param.RemoveWarnParam;
import com.bbd.domain.KeyValueVO;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.OpinionTaskService;
import com.bbd.service.param.TransferParam;
import com.bbd.service.vo.OpinionTaskListVO;
import com.bbd.util.ValidateUtil;
import com.mybatis.domain.PageList;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 舆情任务控制器
 * @author Liuweibo
 * @version Id: OpinionTaskController.java, v0.1 2017/11/7 Liuweibo Exp $$
 */
@RestController
@RequestMapping(value = "api/opinion/task/")
@Api(description = "舆情任务模块")
public class OpinionTaskController extends AbstractController{

    @Autowired
    private OpinionTaskService opinionTaskService;

    @ApiOperation(value = "当前用户待处理舆情列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "转发类型: 1/2/3：请示，4/5/6：回复", name = "transferType", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "un/processed/list", method = RequestMethod.GET)
    public RestResult getUnProcessedList(Integer transferType) {
        PageList<OpinionTaskListVO> result = opinionTaskService.getUnProcessedList(transferType, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "当前用户转发、解除、监测列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "1.转发（介入）；2.已解除； 3.已监控", name = "opStatus", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "起始页号", name = "page", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(value = "每页大小", name = "limit", dataType = "Integer", paramType = "query", required = false)
    })
    @RequestMapping(value = "processed/list", method = RequestMethod.GET)
    public RestResult getProcessedList(Integer opStatus) {
        ValidateUtil.checkNull(opStatus, CommonErrorCode.PARAM_ERROR, "opStatus不能为null");
        PageList<OpinionTaskListVO> result = opinionTaskService.getProcessedList(opStatus, getPageBounds());
        return RestResult.ok(result);
    }

    @ApiOperation(value = "当前用户任务列表统计", httpMethod = "GET")
    @RequestMapping(value = "sta", method = RequestMethod.GET)
    public RestResult getTaskSta() {
        List<KeyValueVO> result = opinionTaskService.getTaskSta();
        return RestResult.ok(result);
    }

    @ApiOperation(value = "转发舆情", httpMethod = "POST")
    @RequestMapping(value = "transfer", method = RequestMethod.POST)
    public RestResult transferOpinion(@RequestBody @Valid @ApiParam(name = "转发对象", value = "传入的json") TransferParam param) throws IOException, ExecutionException, InterruptedException {
        opinionTaskService.transferOpinion(param);
        return RestResult.ok();
    }

    @ApiOperation(value = "查询处于任务舆情中的舆情详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "舆情uuid", name = "uuid", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "详情类型(1-待处理详情、2-转发详情)", name = "type", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value = "transfer/detail", method = RequestMethod.GET)
    public RestResult getWarnOpinionDetail(String uuid, Integer type) {
        ValidateUtil.checkNull(uuid, CommonErrorCode.PARAM_ERROR, "uuid不能为空");
        ValidateUtil.checkNull(type, CommonErrorCode.PARAM_ERROR, "type不能为空");
        OpinionTaskListVO result = opinionTaskService.getTransferDetail(uuid, type);
        return RestResult.ok(result);
    }

    @ApiOperation(value = "解除舆情预警", httpMethod = "POST")
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public RestResult removeWarn(@RequestBody @Valid @ApiParam(name = "解除对象", value = "传入的json") RemoveWarnParam param) throws InterruptedException, ExecutionException, IOException {
        String uuid = param.getUuid();
        Integer removeReason = param.getRemoveReason();
        String removeNote = param.getRemoveNote();
        ValidateUtil.checkAllNull(CommonErrorCode.PARAM_ERROR, uuid, removeReason);
        opinionTaskService.removeWarn(uuid, removeReason, removeNote);
        return RestResult.ok();
    }
}
    
    