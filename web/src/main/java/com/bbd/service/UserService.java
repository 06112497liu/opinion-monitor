/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.UserDao;
import com.bbd.domain.User;
import com.bbd.domain.UserExample;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.param.AccountCreateVO;
import com.bbd.service.param.UserCreateParam;
import com.bbd.service.param.UserCreateVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tjwang
 * @version $Id: UserService.java, v 0.1 2017/9/25 0025 14:49 tjwang Exp $
 */
@Service
public class UserService {

    @Autowired
    private UserDao        userDao;

    @Autowired
    private AccountService accountService;

    /**
     * 查询用户列表
     * @param pb
     * @return
     */
    public List<User> queryUsers(PageBounds pb) {
        UserExample exam = new UserExample();
        exam.createCriteria().andFlagEqualTo(0); // 未删除的用户
        List<User> list = userDao.selectByExampleWithPageBounds(exam, pb);
        list.sort((u1, u2) -> u2.getGmtCreate().compareTo(u1.getGmtCreate()));
        return list;
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

    /**
     * 获取登陆用户id
     * @return
     */
    public Long getUserId() {
        UserInfo u = UserContext.getUser();
        if (Objects.isNull(u))
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登陆");
        return u.getId();
    }

    /**
     * 创建用户和账户
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserAndAccount(UserCreateParam param) {
        UserCreateVO userVo = new UserCreateVO();
        BeanUtils.copyProperties(param, userVo);
        Long userId = createUser(userVo);

        AccountCreateVO accountVO = new AccountCreateVO();
        BeanUtils.copyProperties(param, accountVO);
        accountVO.setUserId(userId);
        accountService.createAccount(accountVO);

    }

    /**
     * 创建用户
     * @param userCreateVO
     * @return
     */
    public Long createUser(UserCreateVO userCreateVO) {
        Preconditions.checkNotNull(userCreateVO, "创建用户参数不能为空");

        userCreateVO.validate();

        User user = new User();
        BeanUtils.copyProperties(userCreateVO, user);
        user.setGmtCreate(new Date());

        userDao.insertSelective(user);

        return user.getId();
    }

    /**
     * 修改用户和账户
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndAccount(UserCreateParam param) {

        // step-1：修改用户信息
        UserCreateVO userVo = new UserCreateVO();
        BeanUtils.copyProperties(param, userVo);
        updateUser(param.getUserId(), userVo);

        // step-2：修改账户信息
        AccountCreateVO accountVO = new AccountCreateVO();
        BeanUtils.copyProperties(param, accountVO);
        accountService.updateAccout(accountVO);

    }


    // 更新舆情用户信息
    private void updateUser(Long userId, UserCreateVO userCreateVO) {
        Preconditions.checkNotNull(userCreateVO, "修改用户参数不能为空");

        userCreateVO.validate();
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(userId);

        User user = new User();
        BeanUtils.copyProperties(userCreateVO, user);
        user.setGmtModified(new Date());

        userDao.updateByExampleSelective(user, example);
    }

    /**
     * 删除用户
     * @param usderId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delUser(Long usderId) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(usderId);
        User record = new User();
        record.setFlag(1); // 删除标记
        int num = userDao.updateByExampleSelective(record, example);
        if(num == 0)
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "操作对象不存在");
    }

}
