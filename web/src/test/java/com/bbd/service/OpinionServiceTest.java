package com.bbd.service;

import com.bbd.param.OpinionInfo;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceTest.java, v0.1 2017/10/27 Liuweibo Exp $$
 */
public class OpinionServiceTest extends BaseServiceTest {

    @Autowired
    private OpinionService opinionService;

    @Test
    public void testGetWarnRemindJson() throws NoSuchFieldException {
        String str = opinionService.getWarnRemindJson(DateTime.now().plusMonths(-1));
        System.out.println(str);
    }

    @Test
    public void testgetWarnRemindJson() throws NoSuchFieldException {
        String str = opinionService.getWarnRemindJson(DateTime.now().plusYears(-1));
        System.out.println(str);
    }

}
    
    