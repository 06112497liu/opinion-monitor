/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.domain.Permission;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        List<Permission> ps = permissionService.queryUserPermissions(userId);
        assertTrue(ps.size() >= 0);
    }

}
