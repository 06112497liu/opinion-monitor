/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.annotation.TimeUsed;
import com.bbd.controller.param.LoginUser;
import com.bbd.domain.Account;
import com.bbd.domain.User;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.exception.UserErrorCode;
import com.bbd.service.AccountService;
import com.bbd.service.PermissionService;
import com.bbd.service.UserService;
import com.bbd.vo.PermissionView;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Optional;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author tjwang
 * @version $Id: LoginController.java, v 0.1 2017/9/25 0025 14:48 tjwang Exp $
 */
@RestController
@RequestMapping("/api/login")
@Api(description = "登陆模块")
public class LoginController extends AbstractController {

    @Autowired
    private UserService       userService;

    @Autowired
    private AccountService    accountService;

    @Autowired
    private PermissionService permissionService;

    @TimeUsed(threshold = 10)
    @ApiOperation(value = "登录", httpMethod = "POST")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public RestResult doLogin(@RequestBody @Valid @ApiParam(name = "用户登陆对象", value = "传入JSON") LoginUser param) {
        if (UserContext.getUser() != null) {
            return RestResult.ok();
        }

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
        Optional<Account> accountOpt = accountService.loadByUserId(userId);
        if (!accountOpt.isPresent()) {
            logger.error(String.format("ID为 %d 的用户，账户不存在", userId));
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "账户不存在");
        }
        Account account = accountOpt.get();

        UserInfo info = new UserInfo();
        info.setId(userId);
        info.setAdmin(account.getAdmin());
        info.setUsername(username);
        info.setAccountName(account.getName());

        setUserPermissions(userId, info);

        UserContext.setUser(info);

        return RestResult.ok(info);
    }

    /**
     * 设置用户权限
     * @param userId
     * @param user
     */
    private void setUserPermissions(Long userId, UserInfo user) {
        List<PermissionView> ps = permissionService.queryUserPermissions(userId);
        user.setPermissions(ps);
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public RestResult logout() {
        UserContext.removeUser();
        return RestResult.ok();
    }
}
