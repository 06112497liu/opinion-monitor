package com.bbd.service.impl;

import com.bbd.dao.MonitorKeywordsDao;
import com.bbd.dao.MonitorKeywordsExtDao;
import com.bbd.dao.WarnNotifierDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.*;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.BizErrorCode;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.SystemSettingService;
import com.bbd.service.UserService;
import com.bbd.service.param.WarnSettingVo;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 预警配置服务接口
 * @author Liuweibo
 * @version Id: SystemSettingService.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private WarnSettingDao settingDao;

    @Autowired
    private WarnNotifierDao notifierDao;

    @Autowired
    private MonitorKeywordsDao keywordsDao;

    @Autowired
    private MonitorKeywordsExtDao keywordsExtDao;

    @Autowired
    private UserService userService;

    @Override
    public Integer modifyHeat(Long eventId, Integer type, Integer first, Integer second, Integer third) {

        // step-1：校验热度值是否符合规范
        boolean flag = checkThresholdValue(type, first, second, third);
        if(flag == false) throw new ApplicationException(CommonErrorCode.PARAM_ERROR);

        // step-2：如果是针对事件，校验操作时间配置是否存在
        if(type != 3) {
            WarnSettingExample ex = new WarnSettingExample();
            ex.createCriteria().andEventIdEqualTo(eventId);
            List<WarnSetting> list = settingDao.selectByExample(ex);
            if(list.isEmpty()) throw new ApplicationException(BizErrorCode.OBJECT_NOT_EXIST, "本事件没有预警配置");
        }

        // step-2：热度值配置
        int result;
        if(1 == type) {
            result = settingDao.updateByExampleSelective(buildWarnSetting(100, first), buildWarnSettingExample(2, eventId, type, 1));
        } else if(2 == type) {
            result = settingDao.updateByExampleSelective(buildWarnSetting(100, first), buildWarnSettingExample(2, eventId, type, 1))
            + settingDao.updateByExampleSelective(buildWarnSetting(first-1, second), buildWarnSettingExample(2, eventId, type, 2))
            + settingDao.updateByExampleSelective(buildWarnSetting(second-1, third), buildWarnSettingExample(2, eventId, type, 3));
        } else {
            result = settingDao.updateByExampleSelective(buildWarnSetting(100, first), buildWarnSettingExample(1, eventId, type, 1))
                    + settingDao.updateByExampleSelective(buildWarnSetting(first-1, second), buildWarnSettingExample(1, eventId, type, 2))
                    + settingDao.updateByExampleSelective(buildWarnSetting(second-1, third), buildWarnSettingExample(1, eventId, type, 3));
        }
        return result;
    }

    @Override
    public Integer addNotifier(WarnNotifier notifier) {

        Long settingId = notifier.getSettingId();
        // step-1：校验通知人是否已经达到20人
        WarnNotifierExample example = new WarnNotifierExample();
        example.createCriteria().andSettingIdEqualTo(settingId);
        int size = notifierDao.selectByExample(example).size();
        if(size >= 20) throw new ApplicationException(BizErrorCode.NOTIFIER_OUT_TWENTY);

        // step-2：校验settingId是否存在
        Preconditions.checkNotNull(settingId);
        boolean flag = Objects.nonNull(settingDao.selectByPrimaryKey(settingId));
        if(!flag) throw new ApplicationException(BizErrorCode.NOTIFIER_SETTINGID_NOT_EXIST);

        // step-3：添加预警通知人
        notifier.setCreateBy(userService.getUserId());
        notifier.setGmtCreate(new Date());
        return notifierDao.insertSelective(notifier);
    }

    @Override
    public Integer modifyNotifier(WarnNotifier notifier) {
        // step-1：校验id是否为null
        Long id = notifier.getId();
        Preconditions.checkNotNull(id);

        // step-2：校验操作对象是否存在
        boolean flag = Objects.nonNull(notifierDao.selectByPrimaryKey(id));
        if(flag == false) throw new ApplicationException(BizErrorCode.OBJECT_NOT_EXIST);

        // step-3：执行修改操作
        notifier.setModifiedBy(userService.getUserId());
        notifier.setGmtModified(new Date());
        return notifierDao.updateByPrimaryKeySelective(notifier);
    }

    @Override
    public Integer delNotifier(Long id) {
        boolean flag = Objects.nonNull(notifierDao.selectByPrimaryKey(id));
        if(flag == false) throw new ApplicationException(BizErrorCode.OBJECT_NOT_EXIST);
        return notifierDao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer addKeyWords(String keyWords) {
        // step-1：校验关键词是否为null
        boolean flag = StringUtils.isEmpty(keyWords);
        if(flag) return 0;

        // step-2：处理关键词
        Splitter splitter = Splitter.on(" ").trimResults().omitEmptyStrings();
        Iterable<String> it = splitter.split(keyWords);
        ArrayList<String> list = Lists.newArrayList(it);

        // step-3：执行添加操作
        int rs;
        try {
            rs = keywordsExtDao.batchInsertKeywords(list);
        } catch (Exception e) {
            throw new ApplicationException(BizErrorCode.KEY_WORD_EXIST);
        }
        return rs;
    }

    @Override
    public Map<Integer, List<MonitorKeywords>> getKeywords() {
        List<MonitorKeywords> list = keywordsDao.selectByExample(new MonitorKeywordsExample());
        Map<Integer, List<MonitorKeywords>> map =
                list.stream().collect(Collectors.groupingBy(MonitorKeywords::getState));
        return map;
    }

    @Override
    public Integer delKeyWords(Long id) {
        boolean flag = Objects.nonNull(keywordsDao.selectByPrimaryKey(id));
        if(flag == false) throw new ApplicationException(BizErrorCode.OBJECT_NOT_EXIST);
        return keywordsDao.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> getOpinionNumGroupHeat(Integer type, Integer first, Integer second, Integer third) {
        return null;
    }

    @Override
    public List<WarnSettingVo> getWarnSettingList(Integer type, Long eventId) {
        // step-1：查询warn_setting配置
        WarnSettingExample example = new WarnSettingExample();
        WarnSettingExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type);
        if(type != 3) criteria.andEventIdEqualTo(eventId);
        List<WarnSetting> dbList = settingDao.selectByExample(example);
        List<WarnSettingVo> list = BeanMapperUtil.mapList(dbList, WarnSettingVo.class);

        // step-2：根据指定settingId获取通知人
        list.forEach(w -> {
            Long settingId = w.getId();
            WarnNotifierExample exam = new WarnNotifierExample();
            exam.createCriteria().andSettingIdEqualTo(settingId);
            w.setNotifierList(notifierDao.selectByExample(exam));
        });
        return list;
    }

    @Override
    public Integer judgeOpinionSettingClass(Integer hot) {
        WarnSettingExample example = new WarnSettingExample();
        example.createCriteria().andTypeEqualTo(3);
        List<WarnSetting> setting = settingDao.selectByExample(example);
        for(WarnSetting s : setting) {
            int min = s.getMin(); int max = s.getMax();
            boolean isContain = Range.closed(min, max).contains(hot);
            if(isContain) return s.getLevel();
        }
        return -1;
    }

    @Override
    public Map<Integer, Integer> getWarnClass() {
        WarnSettingExample example = new WarnSettingExample();
        example.createCriteria().andTypeEqualTo(3);
        List<WarnSetting> list = settingDao.selectByExample(example);
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(WarnSetting::getLevel, WarnSetting::getMin));
        return map;
    }

    // 校验阈值是否符合规则
    private boolean checkThresholdValue(Integer type, Integer first, Integer second, Integer third) {
        boolean flag;
        flag = Range.closed(0, 100).containsAll(Ints.asList(first, second, third));
        if(flag == false) return flag;
        if(type != 1) {
            flag = (first > second && second > third);
            if(flag == false) return flag;
        }
        return flag;
    }

    // 构建WarnSetting类
    private WarnSetting buildWarnSetting(Integer max, Integer min) {
        WarnSetting set = new WarnSetting();
        set.setMax(max);
        set.setMin(min);
        set.setGmtModified(new Date());
        set.setModifiedBy(userService.getUserId());
        return set;
    }

    // 构建WarnSettingExample类
    private WarnSettingExample buildWarnSettingExample(Integer targetType, Long eventId, Integer type, Integer level) {
        WarnSettingExample example = new WarnSettingExample();
        WarnSettingExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type).andLevelEqualTo(level);
        if(targetType == 2) criteria.andEventIdEqualTo(eventId);
        return example;
    }

}














    
    