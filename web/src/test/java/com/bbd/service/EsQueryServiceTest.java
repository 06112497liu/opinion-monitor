/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.vo.KeyValueVO;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

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

    @Test
    public void testGetKeywordsTopTen() {
        List<KeyValueVO> r = esQueryService.getKeywordsTopTen();
        assertTrue(r.size() >= 0);
    }

    @Test
    public void testGetEventSpreadChannelInfo() {
        List<KeyValueVO> r = esQueryService.getEventSpreadChannelInfo();
        assertTrue(r.size() >= 0);
    }

}
