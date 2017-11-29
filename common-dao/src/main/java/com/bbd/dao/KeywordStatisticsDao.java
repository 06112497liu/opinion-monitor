package com.bbd.dao;

import com.bbd.domain.KeywordStatistics;
import com.bbd.domain.KeywordStatisticsExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KeywordStatisticsDao {
    int insert(KeywordStatistics record);

    int insertSelective(KeywordStatistics record);

    List<KeywordStatistics> selectByExampleWithPageBounds(KeywordStatisticsExample example, PageBounds pageBounds);

    List<KeywordStatistics> selectByExample(KeywordStatisticsExample example);

    int updateByExampleSelective(@Param("record") KeywordStatistics record, @Param("example") KeywordStatisticsExample example);

    int updateByExample(@Param("record") KeywordStatistics record, @Param("example") KeywordStatisticsExample example);
}