/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.vo.PermissionView;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: PermissionServiceTest.java, v 0.1 2017/9/27 0027 15:00 tjwang Exp $
 */
public class PermissionServiceTest extends BaseServiceTest {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void testQueryUserPermissions() {
        Long userId = 1L;
        List<PermissionView> ps = permissionService.queryUserPermissions(userId);
        assertTrue(ps.size() >= 0);
    }

    @Test
    @Rollback
    public void testUpdateUserPermission() {
        Long userId = 2L;
        List<Long> pIds = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L);

        permissionService.setUserPermission(true, userId, pIds);

        List<PermissionView> ps = permissionService.queryUserPermissions(userId);
        assertTrue(ps.size() > 0);
    }

}
