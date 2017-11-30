package com.bbd.dao;

import com.bbd.domain.SearchHistory;
import com.bbd.domain.SearchHistoryExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(SearchHistory record);

    int insertSelective(SearchHistory record);

    List<SearchHistory> selectByExampleWithPageBounds(SearchHistoryExample example, PageBounds pageBounds);

    List<SearchHistory> selectByExample(SearchHistoryExample example);

    SearchHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SearchHistory record, @Param("example") SearchHistoryExample example);

    int updateByExample(@Param("record") SearchHistory record, @Param("example") SearchHistoryExample example);

    int updateByPrimaryKeySelective(SearchHistory record);

    int updateByPrimaryKey(SearchHistory record);
}