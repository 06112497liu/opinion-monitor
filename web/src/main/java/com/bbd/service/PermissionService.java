/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.PermissionDao;
import com.bbd.dao.UserPermissionDao;
import com.bbd.domain.*;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.param.AccountUpdateVo;
import com.bbd.vo.PermissionView;
import com.bbd.util.BeanMapperUtil;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 权限服务
 * @author tjwang
 * @version $Id: PermissionService.java, v 0.1 2017/9/27 0027 14:48 tjwang Exp $
 */
@Service
public class PermissionService {

    private Logger            logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PermissionDao     permissionDao;

    @Autowired
    private UserPermissionDao userPermissionDao;

    @Autowired
    private AccountService    accountService;

    /**
     * 查询用户权限
     * @param userId
     */
    public List<PermissionView> queryUserPermissions(Long userId) {
        Preconditions.checkNotNull(userId, "用户ID不能为空");

        List<UserPermission> ups = queryPermissions(userId);
        if (ups.size() == 0) {
            return Lists.newArrayList();
        }

        List<Long> pids = Lists.newArrayList();
        for (UserPermission up : ups) {
            pids.add(up.getPermissionId());
        }

        PermissionExample exam = new PermissionExample();
        exam.createCriteria().andIdIn(pids);

        List<Permission> ds = permissionDao.selectByExample(exam);
        List<PermissionView> ps = BeanMapperUtil.mapList(ds, PermissionView.class);

        return ps;
    }

    private List<UserPermission> queryPermissions(Long userId) {
        UserPermissionExample exam = new UserPermissionExample();
        exam.createCriteria().andUserIdEqualTo(userId);
        return userPermissionDao.selectByExample(exam);
    }

    /**
     * 设置用户权限
     * @param pIds 权限ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUserPermission(boolean isAdmin, Long userId, List<Long> pIds) {
        Preconditions.checkNotNull(userId);
        // 校验操作对象是否存在
        checkAccountExists(userId);

        // 修改账户级别
        updateAccountGrade(userId, isAdmin);

        // 删除操作对象的所有权限，之后在添加
        deleteUserPermission(userId);
        addUserPermission(userId, pIds);
    }

    // 修改账户级别
    private void updateAccountGrade(Long userId, boolean isAdmin) {
        AccountUpdateVo v = new AccountUpdateVo();
        v.setAdmin(isAdmin);
        v.setId(userId);
        accountService.updateAccount(v);
    }

    /**
     * 查询权限列表
     * @return
     */
    public List<PermissionView> queryPermissionViewList() {
        PermissionExample example = new PermissionExample();
        List<Permission> list = permissionDao.selectByExample(example);
        List<PermissionView> result = BeanMapperUtil.mapList(list, PermissionView.class);
        return result;
    }

    private void checkAccountExists(Long userId) {
        Optional<Account> opt = accountService.loadByUserId(userId);
        if (opt.isPresent()) {
            return;
        }
        logger.warn(String.format("ID为 %d 的用户没有账户", userId));
        throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "账户不存在");
    }

    private void deleteUserPermission(Long userId) {
        UserPermissionExample exam = new UserPermissionExample();
        exam.createCriteria().andUserIdEqualTo(userId);
        userPermissionDao.deleteByExample(exam);
    }

    private void addUserPermission(Long userId, List<Long> pIds) {
        Date now = new Date();
        for (Long pId : pIds) {
            UserPermission up = new UserPermission();
            up.setUserId(userId);
            up.setPermissionId(pId);
            up.setGmtCreate(now);
            up.setGmtModified(now);
            userPermissionDao.insertSelective(up);
        }
    }

}
