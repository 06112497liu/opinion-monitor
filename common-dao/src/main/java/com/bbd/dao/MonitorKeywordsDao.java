package com.bbd.dao;

import com.bbd.domain.MonitorKeywords;
import com.bbd.domain.MonitorKeywordsExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MonitorKeywordsDao {
    int deleteByPrimaryKey(Long id);

    int insert(MonitorKeywords record);

    int insertSelective(MonitorKeywords record);

    List<MonitorKeywords> selectByExampleWithPageBounds(MonitorKeywordsExample example, PageBounds pageBounds);

    List<MonitorKeywords> selectByExample(MonitorKeywordsExample example);

    MonitorKeywords selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MonitorKeywords record, @Param("example") MonitorKeywordsExample example);

    int updateByExample(@Param("record") MonitorKeywords record, @Param("example") MonitorKeywordsExample example);

    int updateByPrimaryKeySelective(MonitorKeywords record);

    int updateByPrimaryKey(MonitorKeywords record);
}