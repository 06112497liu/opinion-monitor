/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd;

import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.WarnSetting;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author tjwang
 * @version $Id: WarnSettingDaoTest.java, v 0.1 2017/11/7 0007 19:37 tjwang Exp $
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WarnSettingDaoTest {

    @Autowired
    private WarnSettingDao warnSettingDao;

    @Test
    public void testQuery() {
        WarnSetting item = warnSettingDao.selectByPrimaryKey(1L);
        Assert.assertNotNull(item);
    }
}
