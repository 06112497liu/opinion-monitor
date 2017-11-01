package com.study.service;

import com.bbd.service.OpinionCountStaJob;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Liuweibo
 * @version Id: OpinionCountStaJobTest.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class OpinionCountStaJobTest extends BaseServiceTest {

    @Autowired
    private OpinionCountStaJob opinionCountStaJob;

    @Test
    public void testWarnOpinionCountSta() {
        opinionCountStaJob.warnOpinionCountSta(20, 50, 300);
        opinionCountStaJob.warnOpinionCountSta(25, 50, 300);
        opinionCountStaJob.warnOpinionCountSta(21, 50, 300);
        opinionCountStaJob.warnOpinionCountSta(70, 50, 300);
        opinionCountStaJob.warnOpinionCountSta(50, 50, 300);
        opinionCountStaJob.warnOpinionCountSta(24, 50, 300);
    }

}
    
    