package com.bbd.dao;

import com.bbd.domain.OpinionEventTrendStatistic;
import com.bbd.domain.OpinionEventTrendStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventTrendStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventTrendStatistic record);
    
    int insertBatch(List<OpinionEventTrendStatistic> records);

    int insertSelective(OpinionEventTrendStatistic record);

    List<OpinionEventTrendStatistic> selectByExampleWithBLOBs(OpinionEventTrendStatisticExample example);

    List<OpinionEventTrendStatistic> selectByExampleWithPageBounds(OpinionEventTrendStatisticExample example, PageBounds pageBounds);

    List<OpinionEventTrendStatistic> selectByExample(OpinionEventTrendStatisticExample example);

    OpinionEventTrendStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventTrendStatistic record, @Param("example") OpinionEventTrendStatisticExample example);

    int updateByExampleWithBLOBs(@Param("record") OpinionEventTrendStatistic record, @Param("example") OpinionEventTrendStatisticExample example);

    int updateByExample(@Param("record") OpinionEventTrendStatistic record, @Param("example") OpinionEventTrendStatisticExample example);

    int updateByPrimaryKeySelective(OpinionEventTrendStatistic record);

    int updateByPrimaryKeyWithBLOBs(OpinionEventTrendStatistic record);

    int updateByPrimaryKey(OpinionEventTrendStatistic record);
}