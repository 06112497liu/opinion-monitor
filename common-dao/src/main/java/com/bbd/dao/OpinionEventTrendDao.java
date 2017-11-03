package com.bbd.dao;

import com.bbd.domain.OpinionEventTrend;
import com.bbd.domain.OpinionEventTrendExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventTrendDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventTrend record);

    int insertSelective(OpinionEventTrend record);

    List<OpinionEventTrend> selectByExampleWithPageBounds(OpinionEventTrendExample example, PageBounds pageBounds);

    List<OpinionEventTrend> selectByExample(OpinionEventTrendExample example);

    OpinionEventTrend selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventTrend record, @Param("example") OpinionEventTrendExample example);

    int updateByExample(@Param("record") OpinionEventTrend record, @Param("example") OpinionEventTrendExample example);

    int updateByPrimaryKeySelective(OpinionEventTrend record);

    int updateByPrimaryKey(OpinionEventTrend record);
}