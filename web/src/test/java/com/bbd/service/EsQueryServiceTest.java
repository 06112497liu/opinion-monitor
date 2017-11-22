/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.mybatis.domain.PageBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tjwang
 * @version $Id: EsQueryServiceTest.java, v 0.1 2017/10/31 0031 18:00 tjwang Exp $
 */
public class EsQueryServiceTest extends BaseServiceTest {

    @Resource
    private EsQueryService esQueryService;

    @Test
    public void testGetOpinionCountStatistic() throws NoSuchFieldException, IllegalAccessException {
        DateTime now = new DateTime();
        now = now.plusMonths(-3);
        esQueryService.getOpinionCountStatistic(now, now);
    }

    @Test
    public void testGetKeywordsTopTen() {
        List<KeyValueVO> r = esQueryService.getKeywordsTopTen();
        assertTrue(r.size() >= 0);
    }

    @Test
    public void testGetEventSpreadChannelInfo() {
        List<KeyValueVO> r = esQueryService.getOpinionMediaSpread();
        assertTrue(r.size() >= 0);
    }

    @Test
    public void testQueryWarningOpinion() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMonths(-3);

        Integer emotion = null;
        PageBounds pb = new PageBounds(1, 10);
        OpinionEsSearchVO r = esQueryService.queryWarningOpinion(dateTime, emotion, 2, pb);
        assertNotNull(r);
    }

    @Test
    public void testQueryTop100HotOpinion() {
        String param = "会";
        DateTime startTime = new DateTime().plusMonths(-3);
        Integer emotion = null;
        OpinionEsSearchVO r = esQueryService.queryTop100HotOpinion(startTime, emotion);
        assertNotNull(r);
    }

    @Test
    public void testQueryEventOpinionCounts() {
        List<KeyValueVO> r = esQueryService.getEventOpinionCounts();
        assertNotNull(r);
    }

    @Test
    public void testGetEventOpinionMediaSpread() {
        long eventId = 6L;
        List<KeyValueVO> r = esQueryService.getEventOpinionMediaSpread(eventId, DateTime.now(), null);
        assertNotNull(r);
    }

    @Test
    public void testGetEventWebsiteSpread() {
        long eventId = 6L;
        List<KeyValueVO> r = esQueryService.getEventWebsiteSpread(eventId, DateTime.now(), null);
        assertNotNull(r);
    }

    @Test
    public void testGetEventEmotionSpread() {
        long eventId = 6L;
        List<KeyValueVO> r = esQueryService.getEventEmotionSpread(eventId, DateTime.now(), null);
        assertNotNull(r);
    }

    @Test
    public void testGetOpinionByUUID() {
        esQueryService.getOpinionByUUID("88");
    }

    @Test
    public void testGetOpinionCountStatisticGroupTime() {
        DateTime dateTime = new DateTime();
        DateTime startTime = dateTime.plusDays(-10);
        DateTime endTime = dateTime;
        DateHistogramInterval interval = DateHistogramInterval.HOUR;

        Map<String, List<KeyValueVO>> map = esQueryService.getOpinionCountStatisticGroupTime(startTime, endTime, interval);
        System.out.println("size: " + map.size());
    }

    @Test
    public void testQueryAddWarnCount() {
        DateTime lastSendTime = DateTime.now().plusMonths(-1);
        Map<Integer, Integer> map = esQueryService.queryAddWarnCount(lastSendTime);
        System.out.println(map);
    }

    @Test
    public void testGetMaxHot() {
        DateTime lastSendTime = DateTime.now().plusMonths(-1);
        esQueryService.queryMaxHot(lastSendTime, 1);
    }
}
