package com.bbd.service;

import com.bbd.util.RemoteConfigUtil;
import org.junit.Test;

/**
 * @author Liuweibo
 * @version Id: RemoteConfigUtilTest.java, v0.1 2017/11/23 Liuweibo Exp $$
 */
public class RemoteConfigUtilTest {

    @Test
    public void testGetProperties() {
        String str = RemoteConfigUtil.get("bbd_xg_newqadd_prov");
        System.out.println(str);
    }

}
    
    