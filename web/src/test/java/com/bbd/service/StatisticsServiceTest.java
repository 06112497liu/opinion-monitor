package com.bbd.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Liuweibo
 * @version Id: StatisticsServiceTest.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
public class StatisticsServiceTest extends BaseServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testGetWarnOpinionTopTen() {
        statisticsService.getWarnOpinionTopTen();
    }

}
    
    