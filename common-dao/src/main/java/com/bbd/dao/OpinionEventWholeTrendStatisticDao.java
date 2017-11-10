package com.bbd.dao;

import com.bbd.domain.OpinionEventWholeTrendStatistic;
import com.bbd.domain.OpinionEventWholeTrendStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventWholeTrendStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventWholeTrendStatistic record);
    
    int insertBatch(List<OpinionEventWholeTrendStatistic> records);

    int insertSelective(OpinionEventWholeTrendStatistic record);

    List<OpinionEventWholeTrendStatistic> selectByExampleWithPageBounds(OpinionEventWholeTrendStatisticExample example, PageBounds pageBounds);

    List<OpinionEventWholeTrendStatistic> selectByExample(OpinionEventWholeTrendStatisticExample example);

    OpinionEventWholeTrendStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventWholeTrendStatistic record, @Param("example") OpinionEventWholeTrendStatisticExample example);

    int updateByExample(@Param("record") OpinionEventWholeTrendStatistic record, @Param("example") OpinionEventWholeTrendStatisticExample example);

    int updateByPrimaryKeySelective(OpinionEventWholeTrendStatistic record);

    int updateByPrimaryKey(OpinionEventWholeTrendStatistic record);
}