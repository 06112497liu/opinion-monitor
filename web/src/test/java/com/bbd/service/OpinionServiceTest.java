package com.bbd.service;

import com.bbd.param.OpinionInfo;
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
    public void testOpinionDetail() {
        OpinionInfo o = opinionService.getOpinionDetail("6");
        System.out.println(o);
    }

    @Test
    public void testRemoveWarnOpinion() {
        Integer i = opinionService.removeWarnOpinion("99");
        System.out.println(i);
    }

}
    
    