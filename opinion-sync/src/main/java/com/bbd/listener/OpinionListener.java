/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.bbd.bean.OpinionEsSyncVO;
import com.bbd.bean.OpinionEventRecordVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.bean.OpinionWarnTime;
import com.bbd.constant.EsConstant;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.bbd.util.EsUtil;
import com.bbd.util.JsonUtil;
import com.bbd.vo.OpinionVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 舆情信息Listener
 * 
 * @author tjwang
 * @version $Id: OpinionListener.java, v 0.1 2017/11/8 0008 11:38 tjwang Exp $
 */
@Component
public class OpinionListener {

    private static final Logger logger = LoggerFactory.getLogger(OpinionListener.class);

    @Autowired
    private EsUtil              esUtil;

    @Autowired
    private WarnSettingDao      warnSettingDao;

    @KafkaListener(topics = "bbd_opinion", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        processRecords(records);
    }

    private void processRecords(List<ConsumerRecord<String, String>> records) {
        if (records.size() == 0) {
            return;
        }
        long start = System.currentTimeMillis();

        List<WarnSetting> settings = getWarnSetting();
        Map<Long, WarnSetting> eventWarnSettings = getEventsWarnSettings();

        List<OpinionVO> vos = Lists.newArrayList();
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            OpinionVO vo = JsonUtil.parseObject(record.value(), OpinionVO.class);
            vos.add(vo);
        }

        List<OpinionVO> warnVos = Lists.newArrayList();
        List<OpinionHotEsVO> hotVos = Lists.newArrayList();
        List<OpinionEsSyncVO> updateVos = Lists.newArrayList();
        List<OpinionEsSyncVO> indexVos = Lists.newArrayList();
        List<OpinionEventRecordVO> oerVos = Lists.newArrayList();
        List<OpinionEventRecordVO> oerWarnVos = Lists.newArrayList();
        List<OpinionEventRecordVO> oerHotVos = Lists.newArrayList();

        Map<String, OpinionEsSyncVO> existMap = getExistsOpinions(vos);
        for (OpinionVO vo : vos) {
            addOpinionEventRecords(vo, oerVos);
            addOpinionEventWarnRecord(vo, oerHotVos, eventWarnSettings);

            String uuid = vo.getUuid();

            OpinionEsSyncVO nvo = new OpinionEsSyncVO();
            BeanUtils.copyProperties(vo, nvo);

            boolean warn = matchWarnLevel(vo.getHot(), settings);
            OpinionEsSyncVO esVo = existMap.get(uuid);
            if (warn) {
                addOpinionEventRecords(vo, oerWarnVos);
                addHotRecord(vo, hotVos);

                setWarnTime(nvo, esVo, settings);

                if (esVo != null) {
                    updateVos.add(nvo);
                } else {
                    indexVos.add(nvo);
                }

                addWarnVos(vo, esVo, warnVos, settings);
            } else {
                if (esVo != null) {
                    updateVos.add(nvo);
                } else {
                    indexVos.add(nvo);
                }
            }
        }

        syncOpinionIndex(updateVos, indexVos);
        saveOpinionHotVos(hotVos);
        sendWarnMessage(warnVos);
        saveOpinionEventRecords(oerVos);
        saveOpinionEvenWarnRecords(oerWarnVos);
        saveOpinionEventHotRecords(oerHotVos);

        long end = System.currentTimeMillis();
        logger.info("Process {} opinions success, time used: {}", records.size(), (end - start));
    }

    /**
     * 保存事件和舆情关联记录
     * @param vo
     * @return
     */
    private void addOpinionEventRecords(OpinionVO vo, List<OpinionEventRecordVO> vos) {

        List<Long> events = vo.getEvents();
        if (events == null || events.size() == 0) {
            return;
        }

        List<OpinionEventRecordVO> result = Lists.newArrayList();

        String opinionId = vo.getUuid();
        DateTime dateTime = new DateTime();
        DateTime trimTime = dateTime.withMinuteOfHour(0);
        trimTime = trimTime.withSecondOfMinute(0);
        trimTime = trimTime.withMillisOfSecond(0);

        for (Long eventId : events) {
            OpinionEventRecordVO ovo = new OpinionEventRecordVO();
            ovo.setEventId(eventId);
            ovo.setOpinionId(opinionId);
            ovo.setMatchTime(dateTime.toDate());
            ovo.setMatchTimeTrim(trimTime.toDate());
            result.add(ovo);
        }

        vos.addAll(result);
    }

    /**
     * 获取舆情预警设置
     *
     * @return
     */
    private List<WarnSetting> getWarnSetting() {
        WarnSettingExample exam = new WarnSettingExample();
        exam.createCriteria().andTypeEqualTo(3);
        return warnSettingDao.selectByExample(exam);
    }

    /**
     * 获取时间预警设置
     * @return
     */
    private Map<Long, WarnSetting> getEventsWarnSettings() {
        WarnSettingExample exam = new WarnSettingExample();
        exam.createCriteria().andTypeEqualTo(1);
        List<WarnSetting> ss = warnSettingDao.selectByExample(exam);
        if (ss.size() == 0) {
            return Maps.newHashMap();
        }
        return ss.stream().collect(Collectors.toMap(WarnSetting::getEventId, s -> s));
    }

    /**
     * 获取ES中已存在的舆情数据
     *
     * @param vos
     * @return
     */
    private Map<String, OpinionEsSyncVO> getExistsOpinions(List<OpinionVO> vos) {
        String index = EsUtil.INDEX;
        String type = EsUtil.TYPE;

        long start = System.currentTimeMillis();

        MultiGetRequestBuilder builder = esUtil.getClient().prepareMultiGet();
        String[] includeFields = { EsConstant.OPINION_UUID, EsConstant.OPINION_HOT_PROP, EsConstant.OPINION_FIRST_WARN_TIME };
        String[] excludeFields = {};
        FetchSourceContext sourceContext = new FetchSourceContext(true, includeFields, excludeFields);
        for (OpinionVO vo : vos) {
            MultiGetRequest.Item item = new MultiGetRequest.Item(index, type, vo.getUuid());
            item.fetchSourceContext(sourceContext);
            builder.add(item);
        }

        Map<String, OpinionEsSyncVO> map = Maps.newHashMap();
        MultiGetResponse multiResp = builder.get();

        for (MultiGetItemResponse itemResp : multiResp) {
            if (itemResp.getResponse().isExists()) {
                String str = itemResp.getResponse().getSourceAsString();
                OpinionEsSyncVO vo = JsonUtil.parseObject(str, OpinionEsSyncVO.class);
                map.put(vo.getUuid(), vo);
            }
        }

        long end = System.currentTimeMillis();
        logger.info("Get exists opinion time used: {}", (end - start));

        return map;
    }

    /**
     * 获取预警级别, 0. 未达到预警；1.一级预警；2.二级预警；3.三级预警
     *
     * @param hot:      热度
     * @param settings: 设置
     * @return
     */
    private Integer getWarnLevel(Integer hot, List<WarnSetting> settings) {
        for (WarnSetting setting : settings) {
            Integer min = setting.getMin();
            Integer max = setting.getMax();
            if (hot >= min && hot <= max) {
                return setting.getLevel();
            }
        }
        return 0;
    }

    /**
     * 判断是否达到预警级别
     *
     * @param hot
     * @param settings
     * @return
     */
    private boolean matchWarnLevel(Integer hot, List<WarnSetting> settings) {
        return getWarnLevel(hot, settings) > 0;
    }

    /**
     * 发送预警消息
     *
     * @param vo
     */
    private void addWarnVos(OpinionVO vo, OpinionEsSyncVO old, List<OpinionVO> warnVos, List<WarnSetting> settings) {
        if (old == null) {
            warnVos.add(vo);
            return;
        }
        Integer newLevel = getWarnLevel(vo.getHot(), settings);
        Integer oldLevel = getWarnLevel(old.getHot(), settings);
        // 新舆情达到预警级别，且与旧舆情级别不同
        if (newLevel > 0 && (newLevel != oldLevel)) {
            warnVos.add(vo);
        }
    }

    /**
     * 新增热度记录
     *
     * @param vo: 新舆情信息
     */
    private void addHotRecord(OpinionVO vo, List<OpinionHotEsVO> hotVos) {
        String uuid = vo.getUuid();
        Integer hot = vo.getHot();
        Date hotTime = new Date();
        OpinionHotEsVO hotVo = new OpinionHotEsVO();
        hotVo.setUuid(uuid);
        hotVo.setHot(hot);
        hotVos.add(hotVo);
        hotVo.setHotTime(hotTime);
    }

    /**
     * 设置预警时间
     * @param vo：新舆情
     * @param old： 旧舆情
     * @param settings： 舆情预警设置
     */
    private void setWarnTime(OpinionEsSyncVO vo, OpinionEsSyncVO old, List<WarnSetting> settings) {
        OpinionWarnTime warnTime = new OpinionWarnTime();

        Integer newHot = vo.getHot();
        Date now = new Date();

        Integer level = getWarnLevel(newHot, settings);
        if (old == null) {
            warnTime.setFirstWarnTime(now);
            switch (level) {
                case 1:
                    warnTime.setFirstWarnTimeOne(now);
                    break;
                case 2:
                    warnTime.setFirstWarnTimeTwo(now);
                    break;
                case 3:
                    warnTime.setFirstWarnTimeThree(now);
                    break;
            }
        } else {
            OpinionWarnTime oldWarnTime = old.getWarnTime();
            if (oldWarnTime != null) {
                BeanUtils.copyProperties(oldWarnTime, warnTime);
            }

            if (warnTime.getFirstWarnTime() == null) {
                warnTime.setFirstWarnTime(now);
            }
            switch (level) {
                case 1:
                    if (warnTime.getFirstWarnTimeOne() == null) {
                        warnTime.setFirstWarnTimeOne(now);
                    }
                    break;
                case 2:
                    if (warnTime.getFirstWarnTimeTwo() == null) {
                        warnTime.setFirstWarnTimeTwo(now);
                    }
                    break;
                case 3:
                    if (warnTime.getFirstWarnTimeThree() == null) {
                        warnTime.setFirstWarnTimeThree(now);
                    }
                    break;
            }
        }

        vo.setWarnTime(warnTime);
    }

    /**
     * 批量保存舆情热度信息
     *
     * @param vos
     */
    private void saveOpinionHotVos(List<OpinionHotEsVO> vos) {
        if (vos.size() == 0) {
            return;
        }
        String index = EsUtil.HOT_INDEX;
        String type = EsUtil.HOT_TYPE;

        BulkRequestBuilder bulkBuilder = esUtil.getClient().prepareBulk();
        for (OpinionHotEsVO vo : vos) {
            IndexRequest ir = new IndexRequest(index, type);
            ir.source(JsonUtil.fromJson(vo), XContentType.JSON);
            bulkBuilder.add(ir);
        }
        try {
            BulkResponse resp = bulkBuilder.execute().get();
            long took = resp.getTookInMillis();
            logger.info("Sync opinion hot data to elasticsearch time used: {} ms", took);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 批量同步舆情信息
     *
     * @param updateVos: 待更新数据
     * @param indexVos:  待新增数据
     */
    private void syncOpinionIndex(List<OpinionEsSyncVO> updateVos, List<OpinionEsSyncVO> indexVos) {
        String index = EsUtil.INDEX;
        String type = EsUtil.TYPE;

        BulkRequestBuilder bulkBuilder = esUtil.getClient().prepareBulk();
        for (OpinionEsSyncVO vo : updateVos) {
            UpdateRequest ur = new UpdateRequest(index, type, vo.getUuid()).doc(JsonUtil.fromJson(vo), XContentType.JSON);
            bulkBuilder.add(ur);
        }

        for (OpinionEsSyncVO vo : indexVos) {
            IndexRequest ir = new IndexRequest(index, type, vo.getUuid()).source(JsonUtil.fromJson(vo), XContentType.JSON);
            bulkBuilder.add(ir);
        }

        try {
            BulkResponse resp = bulkBuilder.execute().get();
            logger.info("Has failure - {}", resp.hasFailures());
            long took = resp.getTookInMillis();
            logger.info("Sync opinion data to elasticsearch time used: {} ms", took);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 保存舆情、事件关联记录
     */
    private void saveOpinionEventRecords(List<OpinionEventRecordVO> vos) {
        doSaveOpinionEventRecords(EsConstant.OPINION_EVENT_RECORD_TYPE, vos);
    }

    /**
     * 保存与事件关联的舆情热度超过舆情预警记录
     */
    private void saveOpinionEvenWarnRecords(List<OpinionEventRecordVO> vos) {
        doSaveOpinionEventRecords(EsConstant.OPINION_EVENT_RECORD_WARN_TYPE, vos);
    }

    /**
     * 保存与事件关联的舆情热度超过事件预警记录
     */
    private void saveOpinionEventHotRecords(List<OpinionEventRecordVO> vos) {
        doSaveOpinionEventRecords(EsConstant.OPINION_EVENT_RECORD_HOT_TYPE, vos);
    }

    /**
     * 保存记录
     * @param type：索引类型
     * @param vos：数据
     */
    private void doSaveOpinionEventRecords(String type, List<OpinionEventRecordVO> vos) {
        if (vos == null || vos.size() == 0) {
            return;
        }
        long start = System.currentTimeMillis();

        TransportClient client = esUtil.getClient();
        BulkRequestBuilder bulk = client.prepareBulk();

        String index = EsConstant.IDX_OPINION_EVENT_RECORD;

        for (OpinionEventRecordVO vo : vos) {
            String id = String.valueOf(vo.hashCode());
            IndexRequest ir = new IndexRequest(index, type, id);
            ir.source(JsonUtil.fromJson(vo), XContentType.JSON);
            UpdateRequest ur = new UpdateRequest(index, type, id).upsert(ir);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx.op = 'none'", Maps.newHashMap());
            ur.script(script);
            bulk.add(ur);
        }
        BulkResponse resp = bulk.get();
        System.out.println("doSaveOpinionEventRecords Has failure: " + resp.hasFailures());

        long end = System.currentTimeMillis();
        logger.info("SaveOpinionEventRecords save {} records, time used : {}", vos.size(), (end - start));
    }

    /**
     * 发送预警消息
     *
     * @param vos: 待发送舆情
     */
    private void sendWarnMessage(List<OpinionVO> vos) {
        if (vos.size() == 0) {
            return;
        }
    }

    /**
     * 添加与事件关联舆情首次超过事件预警设置时间记录
     * @param opinion: 舆情
     * @param oerHotVos: 批量操作记录结果集
     * @param settingMap: 事件预警配置
     */
    private void addOpinionEventWarnRecord(OpinionVO opinion, List<OpinionEventRecordVO> oerHotVos, Map<Long, WarnSetting> settingMap) {
        List<Long> eventsIds = opinion.getEvents();
        if (eventsIds == null || eventsIds.size() == 0) {
            return;
        }
        String opinionId = opinion.getUuid();
        Integer hot = opinion.getHot();

        for (Long eventId : eventsIds) {
            WarnSetting setting = settingMap.get(eventId);
            if (setting == null) {
                continue;
            }
            Integer threashold = setting.getMin();
            threashold = threashold == null ? 0 : threashold;
            if (hot >= threashold) {
                OpinionEventRecordVO vo = buildOpinionEventRecordVO(eventId, opinionId);
                oerHotVos.add(vo);
            }
        }
    }

    private OpinionEventRecordVO buildOpinionEventRecordVO(Long eventId, String opinionId) {
        DateTime dateTime = new DateTime();
        DateTime trimTime = dateTime.withMinuteOfHour(0);
        trimTime = trimTime.withSecondOfMinute(0);
        trimTime = trimTime.withMillisOfSecond(0);

        OpinionEventRecordVO vo = new OpinionEventRecordVO();
        vo.setEventId(eventId);
        vo.setOpinionId(opinionId);
        vo.setMatchTime(dateTime.toDate());
        vo.setMatchTimeTrim(trimTime.toDate());

        return vo;
    }
}
