package com.bbd.service.impl;

import com.bbd.dao.OpinionPopDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.*;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionPopService;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: OpinionPopServiceImpl.java, v0.1 2018/1/16 Liuweibo Exp $$
 */
@Service
public class OpinionPopServiceImpl implements OpinionPopService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private OpinionPopDao popDao;

    @Autowired
    private OpinionServiceImpl opinionService;

    @Autowired
    private WarnSettingDao settingDao;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取需要弹窗的舆情等级
     * @return
     */
    private List<Integer> needPopLevel() {
        WarnSettingExample example = new WarnSettingExample();
        example.createCriteria().andTypeEqualTo(3).andPopupEqualTo(1);
        List<WarnSetting> list = settingDao.selectByExample(example);
        List<Integer> need = list.stream().map(WarnSetting::getLevel).collect(Collectors.toList());
        return need;
    }

    /**
     * 舆情系统弹窗字符串
     * @param userId
     * @param type
     */
    @Override
    public PopOpinionMsg opinionPopupWindowsMsg(Long userId, Integer type) {
        DateTime dateTime = DateTime.now();
        Date now = dateTime.toDate();
        // 记录弹窗时间
        OpinionPop opinionPop = recordPopupTime(userId, type, now);
        Date popupTime = opinionPop.getGmtPopLatest();
        // 查询预警预警统计信息
        DateTime queryTime;
        if (DateUtils.isSameInstant(now, popupTime)) queryTime = dateTime.plusYears(-90);
        else queryTime = dateTime.plusYears(-90);
        PopOpinionMsg result = buildPopMsg(queryTime);
        return result;
    }

    private PopOpinionMsg buildPopMsg(DateTime queryTime) {
        Map<Integer, Integer> sta;
        Integer maxHot;
        sta = esQueryService.queryAddWarnCount(queryTime);
        List<Integer> needs = needPopLevel();

        maxHot = getMaxHot(needs, queryTime);
        if (maxHot == -1) return null;

        // 热度值
        PopOpinionMsg popOpinionMsg = new PopOpinionMsg();
        popOpinionMsg.setHot(maxHot);
        // 用户信息
        String username = buildUserStr();
        popOpinionMsg.setUsername(username);
        // 预警统计信息
        buildLevelStaStr(needs, sta, popOpinionMsg);
        // 链接
        popOpinionMsg.setLink("warning");
        return popOpinionMsg;
    }

    // 拼凑统计字符串
    private void buildLevelStaStr(List<Integer> needs, Map<Integer, Integer> sta, PopOpinionMsg popOpinionMsg) {
        List<String> staStr = Lists.newArrayList();
        Map<Integer, String> mapping = popOpinionMsg.getMapping();
        for (Integer level : needs) {
            Integer num = sta.get(level);
            String fieldName = mapping.get(level);
            try {
                Field field = popOpinionMsg.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(popOpinionMsg, num);
            } catch (NoSuchFieldException e) {
                logger.error(e.getMessage());
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
    }



    /**
     * 构建用户信息（用户名-账号）
     * @return
     */
    private String buildUserStr() {
        UserInfo user = UserContext.getUser();
        String name = user.getAccountName();
        String username = user.getUsername();
        String str1 = name + "-" + username;
        return str1;
    }

    /**
     * 获取热度最大的值
     * @return
     */
    private Integer getMaxHot(List<Integer> needs, DateTime queryTime) {
        List<Integer> hots = Lists.newArrayList();
        for (Integer level : needs) {
            Integer hot = esQueryService.queryMaxHot(queryTime, level);
            hots.add(hot);
        }
        Optional<Integer> max = hots.stream().max(Integer::compare);
        if (max.isPresent()) return max.get();
        else return -1;
    }

    /**
     * 记录弹窗时间
     * @param userId
     * @param type
     * @param now
     */
    private OpinionPop recordPopupTime(Long userId, Integer type, Date now) {
        OpinionPopExample example = new OpinionPopExample();
        example.createCriteria().andUserIdEqualTo(userId).andTypeEqualTo((byte) type.intValue());
        List<OpinionPop> list = popDao.selectByExample(example);

        OpinionPop record = new OpinionPop();
        record.setUserId(userId);
        record.setType((byte) type.intValue());
        record.setGmtModified(now);
        record.setGmtPopLatest(now);
        if (CollectionUtils.isEmpty(list)) {
            record.setGmtCreate(now);
            popDao.insert(record);
        } else {
            OpinionPop opinionPop = list.get(0);
            Date lastTime = opinionPop.getGmtPopLatest();
            record.setId(opinionPop.getId());
            popDao.updateByPrimaryKeySelective(record);
            record.setGmtPopLatest(lastTime);
        }
        return record;
    }

}
    
    