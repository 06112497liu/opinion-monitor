/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 首页统计Mock服务
 * @author tjwang
 * @version $Id: IndexStatisticMockService.java, v 0.1 2017/10/31 0031 9:56 tjwang Exp $
 */
@Service("indexStatisticMockServiceImpl")
public class IndexStatisticMockServiceImpl implements IndexStatisticService {

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(OpinionCountStatQueryParam param) {
        OpinionCountStatVO r = new OpinionCountStatVO();
        r.setTotal(1000);
        r.setLevelOne(20);
        r.setLevelTwo(100);
        r.setLevelThree(880);
        return r;
    }

    @Override
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param) {
        Map<String, List<KeyValueVO>> map = Maps.newHashMap();
        for(int i=0; i<4; i++) {
            List<KeyValueVO> list = Lists.newArrayList();
            for(int j=0; j<10; j++) {
                KeyValueVO o = new KeyValueVO("10月" + (j+1) + "日", "111",(int)(Math.random()*99));
                list.add(o);
            }
            map.put(i + "", list);
        }
        return map;
    }

    @Override
    public List<KeyValueVO> getOpinionDBCoordinate() {
        List<KeyValueVO> list = Lists.newArrayList();
        for(int i=0; i<12; i++) {
            KeyValueVO v = new KeyValueVO(i+1, "iii", (int)(Math.random()*1000));
            list.add(v);
        }
        return list;
    }

    @Override
    public Map<String, Object> getSystemSta() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("runDays", 350);
        map.put("opinionCount", 5436543);
        map.put("warnOpinion", 514);
        map.put("event", 20);
        return map;
    }

    @Override
    public Map<String, Object> getDBsta() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("historyTotal", 545435434);
        map.put("dayAdd", 1244);
        map.put("weekAdd", 454654);
        map.put("monthAdd", 6546544);
        return map;
    }

    @Override
    public List<KeyValueVO> getKeywordsTopTen() {
        List<KeyValueVO> list = Lists.newArrayList();
        for(int i=0; i<10; i++) {
            KeyValueVO v = new KeyValueVO("假货", "111", (int)(Math.random()*1000));
            list.add(v);
        }
        return list;
    }

    @Override
    public List<KeyValueVO> getEventChannelTrend() {
        List<KeyValueVO> list = Lists.newArrayList();
        for(int i=0; i<7; i++) {
            KeyValueVO v = new KeyValueVO("微博", "111", "64.15");
            list.add(v);
        }
        return list;
    }

    @Override
    public List<KeyValueVO> getEventClassTrend() {
        List<KeyValueVO> list = Lists.newArrayList();
        for(int i=0; i<11; i++) {
            KeyValueVO v = new KeyValueVO("奶粉", "111", (int)(Math.random()*100));
            list.add(v);
        }
        return list;
    }

    @Override
    public List<KeyValueVO> getEventAreaTrend() {
        List<KeyValueVO> list = Lists.newArrayList();
        for(int i=0; i<11; i++) {
            KeyValueVO v = new KeyValueVO("清镇市", "111", (int)(Math.random()*100));
            list.add(v);
        }
        return list;
    }

}
