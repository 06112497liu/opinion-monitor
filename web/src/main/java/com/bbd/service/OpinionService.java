package com.bbd.service;

import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.service.vo.*;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    Map<String, Object> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb);

    /**
     * 推荐热点舆情top100
     * @param timeSpan
     * @param emotion
     * @param pb
     * @return
     */
    PageList<OpinionVO> getHotOpinionListTop100(Integer timeSpan, Integer emotion, PageBounds pb);

    /**
     * 热点舆情模糊查询
     * @param keyword
     * @return
     */
    PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, PageBounds pb);

    /**
     * 历史预警舆情
     * @param startTime
     * @param endTime
     * @param emotion
     * @param sourceType
     * @return
     */
    Map<String, Object> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer sourceType, PageBounds pb);

    /**
     * 舆情详情
     * @param uuid
     * @return
     */
    OpinionExtVO getOpinionDetail(String uuid);

    /**
     * 历史关键词搜索查询
     * @param keyword
     * @return
     */
    List<String> getHistoryWordSearch(String keyword);

    /**
     * 舆情相同文章数信息
     * @param uuid
     * @param pb
     * @return
     */
    List<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pb);

    /**
     * 获取舆情热度走势
     * @param uuid
     * @param timeSpan
     * @return
     */
    List<OpinionHotEsVO> getOpinionHotTrend(String uuid, Integer timeSpan);
}
