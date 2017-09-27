/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.param.AccountUpdateVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tjwang
 * @version $Id: AccountServiceTest.java, v 0.1 2017/9/27 0027 16:29 tjwang Exp $
 */
public class AccountServiceTest extends BaseServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testUpdateAccount() {
        AccountUpdateVo vo = new AccountUpdateVo();
        vo.setId(2L);
        vo.setAdmin(false);
        vo.setName("BBD");
        vo.setDepNote("成都");
        vo.setPhone("1312312");
        vo.setEmail("bbd@qq.com");
        vo.setRegion("5201");
        accountService.updateAccount(vo);
    }

}
