/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.listener;

import com.bbd.bean.OpinionEsSyncVO;
import com.bbd.bean.OpinionHotEsVO;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
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

/**
 * @author tjwang
 * @version $Id: OpinionListener.java, v 0.1 2017/11/8 0008 11:38 tjwang Exp $
 */
@Component
public class OpinionListener {

    private static final Logger logger = LoggerFactory.getLogger(OpinionListener.class);

    @Autowired
    private WarnSettingDao      warnSettingDao;

    @KafkaListener(topics = "bbd_opinion", containerFactory = "kafkaListenerContainerFactory")
    public void Listen(List<ConsumerRecord<String, String>> records) {
        if (records.size() == 0) {
            return;
        }
        long start = System.currentTimeMillis();

        Date now = new Date();
        List<WarnSetting> settings = getWarnSetting();

        List<OpinionVO> vos = Lists.newArrayList();
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            OpinionVO vo = JsonUtil.parseObject(record.value(), OpinionVO.class);
            vos.add(vo);
        }

        //List<OpinionVO> warnVos = Lists.newArrayList();
        List<OpinionHotEsVO> hotVos = Lists.newArrayList();
        List<OpinionEsSyncVO> updateVos = Lists.newArrayList();
        List<OpinionEsSyncVO> indexVos = Lists.newArrayList();

        Map<String, OpinionEsSyncVO> existMap = getExistsOpinions(vos);
        for (OpinionVO vo : vos) {
            String uuid = vo.getUuid();

            OpinionEsSyncVO nvo = new OpinionEsSyncVO();
            BeanUtils.copyProperties(vo, nvo);

            boolean warn = matchWarnLevel(vo.getHot(), settings);
            if (warn) {
                //addWarnVos(vo, warnVos);
                addHotRecord(vo, hotVos);

                if (existMap.keySet().contains(uuid)) {
                    OpinionEsSyncVO esVo = existMap.get(vo.getUuid());
                    if (!hasMatchHotLevel(esVo)) {
                        nvo.setFirstWarnTime(now);
                    }
                    updateVos.add(nvo);
                } else {
                    nvo.setFirstWarnTime(now);
                    indexVos.add(nvo);
                }
            } else {
                if (existMap.keySet().contains(uuid)) {
                    updateVos.add(nvo);
                } else {
                    indexVos.add(nvo);
                }
            }
        }

        syncOpinionIndex(updateVos, indexVos);
        saveOpinionHotVos(hotVos);

        long end = System.currentTimeMillis();
        logger.info("Process {} opinions success, time used: {}", records.size(), (end - start));
    }

    /**
     * 获取舆情预警设置
     * @return
     */
    private List<WarnSetting> getWarnSetting() {
        WarnSettingExample exam = new WarnSettingExample();
        exam.createCriteria().andTypeEqualTo(3);
        return warnSettingDao.selectByExample(exam);
    }

    /**
     * 获取ES中已存在的舆情数据
     * @param vos
     * @return
     */
    private Map<String, OpinionEsSyncVO> getExistsOpinions(List<OpinionVO> vos) {
        String index = EsUtil.INDEX;
        String type = EsUtil.TYPE;

        long start = System.currentTimeMillis();

        MultiGetRequestBuilder builder = EsUtil.getClient().prepareMultiGet();
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
     * @param hot: 热度
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
     * @param hot
     * @param settings
     * @return
     */
    private boolean matchWarnLevel(Integer hot, List<WarnSetting> settings) {
        return getWarnLevel(hot, settings) > 0;
    }

    /**
     * 发送预警消息
     * @param vo
     */
    private void addWarnVos(OpinionVO vo, List<OpinionVO> warnVos) {
        warnVos.add(vo);
    }

    /**
     * 舆情是否到达过预警级别
     * @param vo
     * @return
     */
    private boolean hasMatchHotLevel(OpinionEsSyncVO vo) {
        return vo.getFirstWarnTime() != null;
    }

    /**
     * 新增热度记录
     * @param vo: 新舆情信息
     */
    private void addHotRecord(OpinionVO vo, List<OpinionHotEsVO> hotVos) {
        String uuid = vo.getUuid();
        Integer hot = vo.getHot();
        OpinionHotEsVO hotVo = new OpinionHotEsVO();
        hotVo.setUuid(uuid);
        hotVo.setHot(hot);
        hotVos.add(hotVo);
    }

    /**
     * 批量保存舆情热度信息
     * @param vos
     */
    private void saveOpinionHotVos(List<OpinionHotEsVO> vos) {
        if (vos.size() == 0) {
            return;
        }
        String index = EsUtil.HOT_INDEX;
        String type = EsUtil.HOT_TYPE;

        BulkRequestBuilder bulkBuilder = EsUtil.getClient().prepareBulk();
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
     * @param updateVos: 待更新数据
     * @param indexVos: 待新增数据
     */
    private void syncOpinionIndex(List<OpinionEsSyncVO> updateVos, List<OpinionEsSyncVO> indexVos) {
        String index = EsUtil.INDEX;
        String type = EsUtil.TYPE;

        BulkRequestBuilder bulkBuilder = EsUtil.getClient().prepareBulk();
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
     * 发送预警消息
     * @param vos: 待发送舆情
     */
    private void sendWarnMessage(List<OpinionVO> vos) {

    }
}
