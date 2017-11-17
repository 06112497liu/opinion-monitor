package com.bbd.service;

import com.bbd.annotation.TimeUsed;
import com.bbd.dao.MonitorKeywordsDao;
import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.MonitorKeywordsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: OpinionJobService.java, v0.1 2017/11/17 Liuweibo Exp $$
 */
@Service
public class OpinionJobService {

    @Autowired
    private MonitorKeywordsDao keywordsDao;

    /**
     * 定时推送舆情关键词到爬虫种子接口
     * 每天凌晨1点向爬虫推送关键词（全量）
     */
    @TimeUsed
    @Scheduled(cron = "* * 1 * * ?")
    public void pushKeyWords() {
        MonitorKeywordsExample example = new MonitorKeywordsExample();
        List<MonitorKeywords> list = keywordsDao.selectByExample(example);
        List<String> keywords = list.stream().map(MonitorKeywords::getValue).collect(Collectors.toList());
        System.out.println(keywords);
    }

}
    
    