/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import com.bbd.domain.User;
import com.bbd.service.UserService;
import com.mybatis.domain.PageBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: UserController.java, v 0.1 2017/9/27 0027 16:20 tjwang Exp $
 */
@RestController
@RequestMapping("/yc/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    public RestResult query() {
        PageBounds pb = getPageBounds();
        List<User> rs = userService.queryUsers(pb);
        return RestResult.ok(rs);
    }

}
