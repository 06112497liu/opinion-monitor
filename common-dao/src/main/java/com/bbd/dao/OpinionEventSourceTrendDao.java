package com.bbd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbd.domain.Graph;
import com.bbd.domain.OpinionEventSourceTrend;
import com.bbd.domain.OpinionEventSourceTrendExample;
import com.mybatis.domain.PageBounds;

public interface OpinionEventSourceTrendDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventSourceTrend record);

    int insertSelective(OpinionEventSourceTrend record);

    List<OpinionEventSourceTrend> selectByExampleWithPageBounds(OpinionEventSourceTrendExample example, PageBounds pageBounds);

    List<OpinionEventSourceTrend> selectByExample(OpinionEventSourceTrendExample example);

    OpinionEventSourceTrend selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventSourceTrend record, @Param("example") OpinionEventSourceTrendExample example);

    int updateByExample(@Param("record") OpinionEventSourceTrend record, @Param("example") OpinionEventSourceTrendExample example);

    int updateByPrimaryKeySelective(OpinionEventSourceTrend record);

    int updateByPrimaryKey(OpinionEventSourceTrend record);
    
    List<Graph> selectBySourceAndCycle(@Param("id")Long id, @Param("sourceType")String sourceType, @Param("isInfo")String isInfo, @Param("days")Integer days);
}