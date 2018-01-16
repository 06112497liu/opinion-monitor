package com.bbd.service.impl;

import com.bbd.dao.OpinionPopDao;
import com.bbd.domain.OpinionPop;
import com.bbd.domain.OpinionPopExample;
import com.bbd.domain.PopMsg;
import com.bbd.report.util.TextUtil;
import com.bbd.service.EsQueryService;
import com.bbd.service.OpinionPopService;
import com.bbd.service.OpinionService;
import com.bbd.util.UserContext;
import com.bbd.vo.UserInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Value("#{propertiesConfig['pop.msg.opinion']}")
    private String opinionPopMsg;

    /**
     * 舆情系统弹窗字符串
     * @param userId
     * @param type
     */
    @Override
    public PopMsg opinionPopupWindowsMsg(Long userId, Integer type) {
        DateTime dateTime = DateTime.now();
        Date now = dateTime.toDate();
        // 记录弹窗时间
        OpinionPop opinionPop = recordPopupTime(userId, type, now);
        Date popupTime = opinionPop.getGmtPopLatest();
        // 查询预警预警统计信息
        DateTime queryTime = null;
        if (DateUtils.isSameInstant(now, popupTime)) queryTime = dateTime.plusMinutes(-10000000);
        else queryTime = dateTime;
        PopMsg result = buildPopMsg(queryTime);
        return result;
    }

    private PopMsg buildPopMsg(DateTime queryTime) {
        Map<Integer, Integer> sta;
        Integer maxHot;
        PopMsg popMsg = new PopMsg();
        sta = esQueryService.queryAddWarnCount(queryTime);
        maxHot = esQueryService.queryMaxHot(queryTime, 1);
        if (maxHot == null) maxHot = esQueryService.queryMaxHot(queryTime, 2);
        if (maxHot == null) maxHot = esQueryService.queryMaxHot(queryTime, 3);
        if (maxHot == null) return null;
        // 用户信息
        List<Object> params = Lists.newArrayList();
        UserInfo user = UserContext.getUser();
        String name = user.getAccountName();
        String username = user.getUsername();
        String str1 = name + "-" + username;
        params.add(str1);

        // 预警统计信息
        params.add(sta.get(1));
        params.add(sta.get(2));
        params.add(sta.get(3));
        params.add(maxHot);

        String msg = TextUtil.replaceParams(opinionPopMsg, params);
        popMsg.setMsg(msg);
        String address = opinionService.getAddress();
        popMsg.setUrl(address + "/warning");
        return popMsg;
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
    
    