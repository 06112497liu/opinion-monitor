/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.UserDao;
import com.bbd.domain.User;
import com.bbd.domain.UserExample;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.mybatis.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: UserService.java, v 0.1 2017/9/25 0025 14:49 tjwang Exp $
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 查询用户列表
     * @param pb
     * @return
     */
    public List<User> queryUsers(PageBounds pb) {
        UserExample exam = new UserExample();
        return userDao.selectByExampleWithPageBounds(exam, pb);
    }

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    public Optional<User> queryUserByUserame(String username) {
        Preconditions.checkArgument(StringUtils.isNotBlank(username), "用户名不能为空");

        UserExample user = new UserExample();
        user.createCriteria().andUsernameEqualTo(username);

        List<User> ds = userDao.selectByExample(user);
        if (ds.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(ds.get(0));
    }

}
