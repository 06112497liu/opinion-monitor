package com.bbd.dao;

import com.bbd.domain.KeywordStatistics;
import com.bbd.domain.KeywordStatisticsExample;
import com.mybatis.domain.PageBounds;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeywordStatisticsExtDao {

    /**
     * 批量插入关键词统计数据
     * @param list
     */
    void batchInsert(List<KeywordStatistics> list);

    /**
     * 删除数据
     */
    void batchDel();
}