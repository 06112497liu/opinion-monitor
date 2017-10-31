/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;

/**
 *
 * @author tjwang
 * @version $Id: EsQueryServiceTest.java, v 0.1 2017/10/31 0031 18:00 tjwang Exp $
 */
public class EsQueryServiceTest extends BaseServiceTest {

    @Resource
    private EsQueryService esQueryService;

    @Test
    public void testGetOpinionCountStatistic() {
        DateTime now = new DateTime();
        now = now.plusMonths(-3);
        esQueryService.getOpinionCountStatistic(now);
    }

}
