package com.bbd.service;

import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: SystemSettingServiceTest.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
public class SystemSettingServiceTest extends BaseServiceTest{

    @Autowired
    private SystemSettingService settingService;

    @Autowired
    private WarnSettingDao warnSettingDao;

    @Test
    public void testModifyHeat() {
        settingService.modifyHeat(2L, 3, 1, 74, 60, 50);
    }

    @Test
    public void testAddNotifier() {
        WarnNotifier w = new WarnNotifier();
        w.setNotifier("王五");
        w.setSettingId(7L);
        w.setEmail("804912547@qq.com");
        w.setEmailNotify(1);
    }

    @Test
    public void testModifyNotifier() {
        WarnNotifier w = new WarnNotifier();
        w.setNotifier("张三");
        w.setId(1L);
    }

    @Test
    public void testDelNotifier() {
        settingService.delNotifier(1L);
    }

    @Test
    public void testAddKeyWords() {
        String keywords = "天价";
        settingService.addKeyWords(keywords);
    }

    @Test
    public void getKeywords() {
        System.out.println(settingService.getKeywords());
    }

    @Test
    public void testGetWarnSettingList() {
        settingService.getWarnSettingList(3, 8L);
    }

    @Test
    public void testWarnSetting() {
        WarnSettingExample exam = new WarnSettingExample();
        exam.createCriteria().andTypeEqualTo(1);
        List<WarnSetting> ss = warnSettingDao.selectByExample(exam);
        ss.stream().collect(Collectors.toMap(WarnSetting::getEventId, s -> s));
        System.out.println(ss);
    }
}
    
    