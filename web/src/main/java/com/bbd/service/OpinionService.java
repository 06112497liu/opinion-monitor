package com.bbd.service;

import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionVO;
import com.bbd.service.vo.WarnOpinionTopTenVO;
import com.mybatis.domain.PageList;

import java.util.List;

/**
 * 舆情接口服务
 * @author Liuweibo
 * @version Id: OpinionService.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public interface OpinionService {

    /**
     * 预警舆情top10
     * @return
     */
    List<WarnOpinionTopTenVO> getWarnOpinionTopTen();

    /**
     * 预警舆情列表
     * @param timeSpan
     * @param emotion
     * @param sourceType
     * @return
     */
    PageList<OpinionVO> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType);

    /**
     * 舆情列表媒体类型分布
     * @param timeSpan
     * @param emotion
     * @returnkeywordnde
     */
    List<KeyValueVO> getWarnOpinionMediaTrend(Integer timeSpan, Integer emotion, Integer rank);

    /**
     * 热点舆情top100
     * @param keyword
     * @return
     */
    PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, Integer sourceType);
}
