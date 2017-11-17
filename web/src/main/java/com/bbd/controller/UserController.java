/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.User;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.UserService;
import com.bbd.service.param.UserCreateParam;
import com.bbd.util.MD5Util;
import com.bbd.util.ValidateUtil;
import com.mybatis.domain.PageBounds;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: UserController.java, v 0.1 2017/9/27 0027 16:20 tjwang Exp $
 */
@RestController
@RequestMapping("/api/user")
@Api(description = "用户模块")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户", httpMethod = "GET")
    public RestResult query() {
        PageBounds pb = getPageBounds();
        List<User> rs = userService.queryUsers(pb);
        return RestResult.ok(rs);
    }

    @ApiOperation(value = "创建用户", httpMethod = "POST")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public RestResult createUser(@RequestBody @Valid @ApiParam(name = "用户对象", value = "传入JSON") UserCreateParam param) {
        param.setPassword(MD5Util.md5(param.getPassword()));
        userService.createUserAndAccount(param);
        return RestResult.ok();
    }

    @ApiOperation(value = "修改用户信息", httpMethod = "POST")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public RestResult updateUser(@RequestBody @Valid @ApiParam(name = "用户对象", value = "传入JSON") UserCreateParam param) {
        ValidateUtil.checkNull(param.getUserId(), CommonErrorCode.BIZ_ERROR, "修改用户id不能为空");
        param.setPassword(MD5Util.md5(param.getPassword()));
        userService.updateUserAndAccount(param);
        return RestResult.ok();
    }

    @ApiOperation(value = "删除用户", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", dataType = "int", paramType = "query", required = true)
    })
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public RestResult delUser(Long userId) {
        ValidateUtil.checkNull(userId, CommonErrorCode.PARAM_ERROR, "userId不能为空");
        userService.delUser(userId);
        return RestResult.ok();
    }

}
