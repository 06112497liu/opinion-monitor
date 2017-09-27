/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.PermissionDao;
import com.bbd.dao.UserPermissionDao;
import com.bbd.domain.Permission;
import com.bbd.domain.PermissionExample;
import com.bbd.domain.UserPermission;
import com.bbd.domain.UserPermissionExample;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限服务
 * @author tjwang
 * @version $Id: PermissionService.java, v 0.1 2017/9/27 0027 14:48 tjwang Exp $
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionDao     permissionDao;

    @Autowired
    private UserPermissionDao userPermissionDao;

    /**
     * 查询用户权限
     * @param userId
     */
    public List<Permission> queryUserPermissions(Long userId) {
        Preconditions.checkNotNull(userId, "用户ID不能为空");

        List<UserPermission> ups = queryPermissions(userId);
        List<Long> pids = Lists.newArrayList();
        for (UserPermission up : ups) {
            pids.add(up.getPermissionId());
        }

        PermissionExample exam = new PermissionExample();
        exam.createCriteria().andIdIn(pids);

        return permissionDao.selectByExample(exam);
    }

    private List<UserPermission> queryPermissions(Long userId) {
        UserPermissionExample exam = new UserPermissionExample();
        exam.createCriteria().andUserIdEqualTo(userId);
        return userPermissionDao.selectByExample(exam);
    }

}
