/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.controller.param.LoginUser;
import com.bbd.domain.Account;
import com.bbd.domain.User;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.exception.UserErrorCode;
import com.bbd.service.AccountService;
import com.bbd.service.UserService;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 * @author tjwang
 * @version $Id: LoginController.java, v 0.1 2017/9/25 0025 14:48 tjwang Exp $
 */
@RestController
@RequestMapping("/yc/login")
public class LoginController extends AbstractController {

    @Autowired
    private UserService    userService;

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "登录", httpMethod = "POST")
    @ApiImplicitParams({ @ApiImplicitParam(value = "用户名", name = "username", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "密码", name = "password", dataType = "String", paramType = "query", required = false) })
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public RestResult doLogin(@Valid LoginUser param) {
        String username = param.getUsername();
        String password = param.getPassword();
        Optional<User> opt = userService.queryUserByUserame(username);
        if (!opt.isPresent()) {
            logger.debug(String.format("用户名为 %s 的用户不存在", username));
            throw new ApplicationException(UserErrorCode.USERNAME_PASSWORD_ERROR);
        }
        User user = opt.get();
        if (!user.getPassword().equals(password)) {
            logger.debug("密码错误");
            throw new ApplicationException(UserErrorCode.USERNAME_PASSWORD_ERROR);
        }

        Long userId = user.getId();
        Optional<Account> accountOpt = accountService.queryByUserId(userId);
        if (!accountOpt.isPresent()) {
            logger.error(String.format("ID为 %d 的用户，账户不存在", userId));
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "账户不存在");
        }
        Account account = accountOpt.get();

        UserInfo info = new UserInfo();
        info.setUsername(username);
        info.setAccountName(account.getName());

        UserContext.setUser(info);

        return RestResult.ok();
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public RestResult logout() {
        UserContext.removeUser();
        return RestResult.ok();
    }
}
