package com.bbd.service;

import com.bbd.dao.OpinionExtDao;
import com.bbd.domain.Opinion;
import com.bbd.param.OpinionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
    
    