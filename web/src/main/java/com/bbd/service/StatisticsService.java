package com.bbd.service;

import com.bbd.dao.OpinionExtDao;
import com.bbd.param.ChannelTrend;
import com.bbd.param.NameValueInfo;
import com.bbd.param.OpinionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 * @author Liuweibo
 * @version Id: StatisticsService.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
@Service
public class StatisticsService {

    @Autowired
    private OpinionExtDao opinionExtDao;

    /**
     * 获取预警舆情top10
     * @return
     */
    public List<OpinionVo> getWarnOpinionTopTen() {
        List<OpinionVo> list = opinionExtDao.selectWarnOpinionTopTen();
        return list;
    }

    /**
     * 查询不同时间段舆情增量
     * @return
     */
    public Map<String, Long> getAddOpinionGroupByTime() {
        Map<String, Long> map = opinionExtDao.selectAddOpinionGroupByTime();
        return map;
    }

    /**
     * 查询舆情传播渠道分布
     * @return
     */
    public List<ChannelTrend> getOpinionChannelTrend() {
        List<ChannelTrend> list = opinionExtDao.selectOpinionChannelTrend();
        return list;
    }

    /**
     * 查询舆情事件类型分布
     * @return
     */
    public List<NameValueInfo> getEventTypeTrend() {
        List<NameValueInfo> list = opinionExtDao.selectEventTypeTrend();
        return list;
    }

    /**
     * 查询舆情事件地域分布
     * @return
     */
    public List<NameValueInfo> getEventMapTrend() {
        List<NameValueInfo> list = opinionExtDao.selectEventMapTrend();
        return list;
    }

}
    
    