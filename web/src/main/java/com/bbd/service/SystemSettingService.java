package com.bbd.service;

import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnSetting;
import com.bbd.service.param.WarnNotifierParam;
import com.bbd.service.param.WarnSettingVo;

import java.util.List;
import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: SystemSettingService.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public interface SystemSettingService {

    /**
     * 修改舆情预警热度阈值
     * @param first  1级舆情预警热度阈值下限
     * @param second 2级舆情预警热度阈值下限
     * @param third  3级舆情预警热度阈值下限
     * @return
     */
    Integer modifyHeat(Long eventId, Integer type, Integer first, Integer second, Integer third);

    /**
     * 添加或修改预警通知人
     * @param list
     * @return
     */
    Integer operateNotifier(List<WarnNotifierParam> list);

    /**
     * 删除预警通知人
     * @param id 预警通知人id
     * @return
     */
    Integer delNotifier(Long id);

    /**
     * 添加舆情关键词
     * @param keyWords 关键词字符串
     * @return
     */
    Integer addKeyWords(String keyWords);

    /**
     * 查询关键词库
     * @return
     */
    Map<String, List<MonitorKeywords>> getKeywords();

    /**
     * 删除舆情关键词
     * @param id
     * @return
     */
    Integer delKeyWords(Long id);

    /**
     * 根据预警舆情热度，获取各个级别舆情条数
     * @param type
     * @param first
     * @param second
     * @param third
     * @return
     */
    Map<String, Object> getOpinionNumGroupHeat(Integer type, Integer first, Integer second, Integer third);

    /**
     * 获取预警配置列表
     * @param type 预警类型（1. 事件新增观点预警；2.事件总体热度预警；3.舆情预警。）
     * @return
     */
    List<WarnSettingVo> getWarnSettingList(Integer type, Long eventId);

    /**
     * 返回-1表示该舆情没有达到预警级别
     * @param hot
     * @return
     */
    Integer judgeOpinionSettingClass(Integer hot, List<WarnSetting> setting);

    /**
     * 查询预警配置
     * @param type
     * @return
     */
    List<WarnSetting> queryWarnSetting(Integer type);

    /**
     * 获取预警等级
     * @return
     */
    Map<Integer, Integer> getWarnClass();
}
    
    