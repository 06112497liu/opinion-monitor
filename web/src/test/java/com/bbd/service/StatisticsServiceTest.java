package com.bbd.service;

import com.bbd.param.ChannelTrend;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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

    @Test
    public void testGetAddOpinionGroupByTime() {
        Map<String, Long> map = statisticsService.getAddOpinionGroupByTime();
        System.out.println(map);
    }

    @Test
    public void testGetOpinionChannelTrend() {
        List<ChannelTrend> list = statisticsService.getOpinionChannelTrend();
        System.out.println(list);
    }

}
    
    