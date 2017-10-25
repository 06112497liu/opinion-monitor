package com.bbd.dao;

import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.MonitorKeywordsExample;
import com.mybatis.domain.PageBounds;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorKeywordsExtDao {

    /**
     * 批量出入预警舆情关键词
     *
     * @param keywords 关键词集合
     * @return
     */
    int batchInsertKeywords(List<String> keywords);

}