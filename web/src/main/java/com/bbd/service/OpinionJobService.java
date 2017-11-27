package com.bbd.service;

import com.bbd.annotation.TimeUsed;
import com.bbd.bean.Reptile;
import com.bbd.dao.MonitorKeywordsDao;
import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.MonitorKeywordsExample;
import com.bbd.util.HttpUtil;
import com.bbd.util.RemoteConfigUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.scripts.JO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: OpinionJobService.java, v0.1 2017/11/17 Liuweibo Exp $$
 */
@Service
public class OpinionJobService {

    @Autowired
    private MonitorKeywordsDao keywordsDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 定时推送舆情关键词到爬虫种子接口
     * 每天凌晨1点向爬虫推送关键词（全量）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void pushKeyWords() throws UnsupportedEncodingException {
        String url = RemoteConfigUtil.get(Reptile.NEWQA_ADD_URL);
        String path = RemoteConfigUtil.get(Reptile.NEWQA_ADD_PATH);
        String full_url = url + path;
        Map<String, String> params = buildRequestParams(); // 参数
        List<String> prov = buildProv(); // 爬虫名称
        for (String s : prov) {
            logger.info("灌入种子：" + params.get("seeds").replace("%0d%0a", ",") + " ———》 爬虫：" + s);
            params.put("prov", s);
            HttpUtil.getHttp(full_url, params);
            params.remove("prov");
        }
    }

    // 获取种子
    private String buildSeeds() {
//        Joiner joiner = Joiner.on("%0d%0a").skipNulls();
        Joiner joiner = Joiner.on("\n").skipNulls();
        MonitorKeywordsExample example = new MonitorKeywordsExample();
        List<MonitorKeywords> list = keywordsDao.selectByExample(example);
        List<String> keywords = list.stream().map(MonitorKeywords::getValue).collect(Collectors.toList());
        String str = joiner.join(keywords);
        return str;
    }

    // 构建请求参数
    Map<String, String> buildRequestParams() {
        Map<String, String> params = Maps.newHashMap();
        params.put("keytype", "keyword");
        params.put("qtype", "_bug");
        String seeds = buildSeeds();
        params.put("seeds", seeds);
        params.put("addtype", "队首");
        String ak = RemoteConfigUtil.get(Reptile.NEWQA_ADD_AK);
        params.put("appkey", ak);
        return params;
    }

    // 构建种子名称
    List<String> buildProv() {
        String prov = RemoteConfigUtil.get(Reptile.NEWQA_ADD_PROV);
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        Iterable<String> it = splitter.split(prov);
        ArrayList<String> list = Lists.newArrayList(it);
        return list;
    }

}
    
    