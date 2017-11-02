package com.study.service;

import com.bbd.service.OpinionCountStaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Liuweibo
 * @version Id: OpinionCountStaJobTest.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class OpinionCountStaJobTest extends BaseServiceTest {

    @Autowired
    private OpinionCountStaService opinionCountStaService;

    @Test
    public void testWarnOpinionCountSta() {
        opinionCountStaService.warnOpinionCountSta(1, 20, 50, 300);
        opinionCountStaService.warnOpinionCountSta(2, 25, 50, 300);
        opinionCountStaService.warnOpinionCountSta(2, 21, 50, 300);
        opinionCountStaService.warnOpinionCountSta(3, 70, 50, 300);
        opinionCountStaService.warnOpinionCountSta(3, 50, 50, 300);
        opinionCountStaService.warnOpinionCountSta(3, 24, 50, 300);
    }

}
    
    