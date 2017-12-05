/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd;

import com.bbd.bean.OpinionEsSyncVO;
import com.bbd.bean.OpinionEventRecordVO;
import com.bbd.bean.OpinionWarnTime;
import com.bbd.constant.EsConstant;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.google.common.collect.Maps;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author tjwang
 * @version $Id: EsTest.java, v 0.1 2017/11/9 0009 15:06 tjwang Exp $
 */
public class EsTest {

    /**
     * 构建一级预警舆情
     *
     * @return
     */
    public static OpinionEsSyncVO buildLevelOne() {
        OpinionEsSyncVO vo = new OpinionEsSyncVO();

        vo.setUuid("222");
        vo.setTitle("美国总统访华");
        vo.setSummary("美国总统访华");
        vo.setHot(99);
        vo.setContent("川普总统访华");
        vo.setSource("腾讯");
        vo.setLink("http://www.qq.com");
        vo.setSimiliarCount(9000);
        vo.setCommentCount(200);
        vo.setEmotion(1);
        vo.setKeywords("");
        vo.setWebsite("腾讯");
        vo.setMediaType(2);
        vo.setEvents(Arrays.asList(3L));
        vo.setPublishTime(new Date());

        return vo;
    }

    /**
     * 构建二级预警舆情
     *
     * @return
     */
    public static OpinionEsSyncVO buildLevelTwo() {
        OpinionEsSyncVO vo = new OpinionEsSyncVO();

        vo.setUuid("333");
        vo.setTitle("苹果");
        vo.setSummary("IPhone X");
        vo.setHot(70);
        vo.setContent("苹果估计近万亿");
        vo.setSource("腾讯");
        vo.setLink("http://www.qq.com");
        vo.setSimiliarCount(100);
        vo.setCommentCount(20);
        vo.setEmotion(1);
        vo.setKeywords("");
        vo.setWebsite("腾讯");
        vo.setMediaType(2);
        vo.setEvents(Arrays.asList(3L));
        vo.setPublishTime(new Date());

        return vo;
    }

    /**
     * 构建三级预警舆情
     *
     * @return
     */
    public static OpinionEsSyncVO buildLevelThree() {
        OpinionEsSyncVO vo = new OpinionEsSyncVO();

        vo.setUuid("444");
        vo.setTitle("成都空气质量预警");
        vo.setSummary("成都雾霾严重");
        vo.setHot(65);
        vo.setContent("成都雾霾严重");
        vo.setSource("腾讯");
        vo.setLink("http://www.qq.com");
        vo.setSimiliarCount(300);
        vo.setCommentCount(30);
        vo.setEmotion(-1);
        vo.setKeywords("");
        vo.setWebsite("腾讯");
        vo.setMediaType(2);
        vo.setEvents(Arrays.asList(3L));
        vo.setPublishTime(new Date());

        Date now = new Date();
        OpinionWarnTime warnTime = new OpinionWarnTime();
        warnTime.setFirstWarnTime(now);
        warnTime.setFirstWarnTimeOne(now);
        warnTime.setFirstWarnTimeTwo(now);
        warnTime.setFirstWarnTimeThree(now);

        vo.setWarnTime(warnTime);

        return vo;
    }

    @Test
    public void testMultiIndex() {
        OpinionEsSyncVO one = buildLevelOne();
        OpinionEsSyncVO two = buildLevelTwo();
        OpinionEsSyncVO three = buildLevelThree();

        String index = EsUtil.INDEX;
        String type = EsUtil.TYPE;

        //        BulkRequestBuilder bulk = EsUtil.getClient().prepareBulk();
        BulkRequestBuilder bulk = null;
        bulk.add(new IndexRequest(index, type, one.getUuid()).source(JsonUtil.fromJson(one), XContentType.JSON));
        bulk.add(new IndexRequest(index, type, two.getUuid()).source(JsonUtil.fromJson(two), XContentType.JSON));
        bulk.add(new IndexRequest(index, type, three.getUuid()).source(JsonUtil.fromJson(three), XContentType.JSON));

        BulkResponse resp = bulk.get();
        System.out.println("Has failure: " + resp.hasFailures());
        System.out.println("Time used : " + resp.getTookInMillis());
    }

    @Test
    public void testIndexOpinionEventRecord() {

        String index = EsConstant.IDX_OPINION_EVENT_RECORD;
        String type = EsConstant.OPINION_EVENT_RECORD_TYPE;

        OpinionEventRecordVO vo = new OpinionEventRecordVO();

        vo.setOpinionId("123123213");
        vo.setEventId(123L);
        String id = String.valueOf(vo.hashCode());
        vo.setId(id);

        DateTime dateTime = new DateTime();
        DateTime trimTime = dateTime.withMinuteOfHour(0);
        trimTime = trimTime.withSecondOfMinute(0);
        trimTime = trimTime.withMillisOfSecond(0);

        vo.setMatchTime(dateTime.toDate());
        vo.setMatchTimeTrim(trimTime.toDate());

        TransportClient client = null;
        BulkRequestBuilder bulk = client.prepareBulk();

        IndexRequest ir = new IndexRequest(index, type, id);
        ir.source(JsonUtil.fromJson(vo), XContentType.JSON);
        UpdateRequest ur = new UpdateRequest(index, type, id).upsert(ir);
        Script script = new Script(ScriptType.INLINE, "painless", "ctx.op = 'none'", Maps.newHashMap());
        ur.script(script);
        bulk.add(ur);

        BulkResponse resp = bulk.get();
        System.out.println("Has failure: " + resp.hasFailures());
    }
}
