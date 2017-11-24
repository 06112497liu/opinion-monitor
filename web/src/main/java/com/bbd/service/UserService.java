/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.bean.UserListVO;
import com.bbd.dao.UserDao;
import com.bbd.dao.UserExtDao;
import com.bbd.domain.User;
import com.bbd.domain.UserExample;
import com.bbd.enums.DistrictExtEnum;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.param.AccountCreateVO;
import com.bbd.service.param.UserCreateParam;
import com.bbd.service.param.UserCreateVO;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
import java.util.stream.Collectors;

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

    @Autowired
    private UserExtDao userExtDao;

    /**
     * 查询用户列表
     * @param region
     * @param pb
     * @return
     */
    public List<UserListVO> queryUsers(String region, PageBounds pb) {
        List<UserListVO> list = userExtDao.queryUserList(region, pb);
        list.forEach(p -> {
            p.setReginDesc(DistrictExtEnum.getDescByCode(p.getRegion()));
        });
        list.sort((p1, p2) -> - p1.getGmtCreate().compareTo(p2.getGmtCreate()));
        return list;
    }

    /**
     * 转发用户列表
     * @return
     */
    public List<String> getTransferUsers() {
        List<UserListVO> list = userExtDao.queryTransUserList();
        Joiner joiner = Joiner.on("-").skipNulls();
        List<String> result = list.stream().map(p -> {
            String name = p.getName();
            String dep = p.getDepNote();
            String username = p.getUsername();
            return joiner.join(name, dep, username);
        }).collect(Collectors.toList());
        return result;
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
        Optional<User> op = queryUserByUserame(param.getUsername());
        if(op.isPresent()) {
            throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "用户名重复");
        }
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
