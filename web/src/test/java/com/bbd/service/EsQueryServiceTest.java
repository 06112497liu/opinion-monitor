/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.Reptile;
import com.bbd.domain.KeyValueVO;
import com.bbd.service.vo.OpinionEsSearchVO;
import com.bbd.util.HttpUtil;
import com.bbd.util.RemoteConfigUtil;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
        OpinionEsVO opinion = esQueryService.getOpinionByUUID("16510889051924266776");
        String content = opinion.getContent();
        JSONArray arr = JSONArray.parseArray(content);
        List<String> list = new ArrayList(arr);
        StringBuilder sb = buildContent(list);
        System.out.println(sb);
    }

    private StringBuilder buildContent(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            String trimStr = str.trim();
            if (trimStr.startsWith("pic_rowkey")) {
                String pictureCode = trimStr.substring(trimStr.indexOf(":")+1);
                String picAddress = getPicParse(pictureCode);
                sb.append(picAddress);
            } else {
                sb.append(trimStr);
            }
            sb.append("<br/>");
        }
        return sb;
    }

    private String getPicParse(String picCode) {

        String url = RemoteConfigUtil.get(Reptile.PIC_PARSE_URL);
        String ak = RemoteConfigUtil.get(Reptile.PIC_PARSE_AK);
        String path = RemoteConfigUtil.get(Reptile.PIC_PARSE_PATH);
        String full_url = url + path;
        Map<String, String> params = Maps.newHashMap();
        params.put("key", picCode);
        params.put("appkey", ak);
        String resp = HttpUtil.getHttp(full_url, params);

        JSONObject obj = JSONObject.parseObject(resp);
        JSONArray imgResp = obj.getJSONArray("results");
        String imgUrl = null;
        for (int i=0; i<imgResp.size(); i++) {
            JSONObject o = imgResp.getJSONObject(i);
            if(o.containsKey("img_url")) {
                imgUrl = o.getString("img_url");
            }
        }
        return imgUrl;
    }

    @Test
    public void stringTest() {
        String s = "3::3:";
        System.out.println(s.length());
        System.out.println(s.indexOf(":"));
        String str = s.trim();
        System.out.println(str.length());
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

    @Test
    public void testQueryAffectionSta() {
        DateTime time = DateTime.now().plusMonths(-5);
        List<KeyValueVO> list = esQueryService.queryAffectionSta(time);
        System.out.println(list);
    }

    @Test
    public void testCalSimilarCount() {
        Integer count = esQueryService.calSimilarCount("7158796291244568116");
        System.out.println(count);
        List<String> uuids = Arrays.asList("7158796291244568116", "15702127494206401901");
        Map<String, Object> rs = esQueryService.calSimilarCount(uuids);
        System.out.println(rs);
    }

    @Test
    public void testCheckOpinionTasking() {
        Boolean flag = esQueryService.checkOpinionTasking("10957094175423247910");
        System.out.println(flag);
    }

}
