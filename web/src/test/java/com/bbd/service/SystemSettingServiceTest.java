package com.bbd.service;

import com.bbd.domain.WarnNotifier;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Liuweibo
 * @version Id: SystemSettingServiceTest.java, v0.1 2017/10/25 Liuweibo Exp $$
 */
public class SystemSettingServiceTest extends BaseServiceTest{

    @Autowired
    private SystemSettingService settingService;

    @Test
    public void testModifyHeat() {
        settingService.modifyHeat(3, 74, 60, 50);
    }

    @Test
    public void testAddNotifier() {
        WarnNotifier w = new WarnNotifier();
        w.setNotifier("王五");
        w.setSettingId(7L);
        w.setEmail("804912547@qq.com");
        w.setEmailNotify(1);
        settingService.addNotifier(w);
    }

    @Test
    public void testModifyNotifier() {
        WarnNotifier w = new WarnNotifier();
        w.setNotifier("张三");
        w.setId(1L);
        settingService.modifyNotifier(w);
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
        settingService.getWarnSettingList(3);
    }
}
    
    