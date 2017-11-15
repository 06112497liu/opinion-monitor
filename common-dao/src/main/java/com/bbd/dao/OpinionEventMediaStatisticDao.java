package com.bbd.dao;

import com.bbd.domain.OpinionEventMediaStatistic;
import com.bbd.domain.OpinionEventMediaStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventMediaStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventMediaStatistic record);
    
    int insertBatch(List<OpinionEventMediaStatistic> records);

    int insertSelective(OpinionEventMediaStatistic record);

    List<OpinionEventMediaStatistic> selectByExampleWithPageBounds(OpinionEventMediaStatisticExample example, PageBounds pageBounds);

    List<OpinionEventMediaStatistic> selectByExample(OpinionEventMediaStatisticExample example);

    OpinionEventMediaStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventMediaStatistic record, @Param("example") OpinionEventMediaStatisticExample example);

    int updateByExample(@Param("record") OpinionEventMediaStatistic record, @Param("example") OpinionEventMediaStatisticExample example);

    int updateByPrimaryKeySelective(OpinionEventMediaStatistic record);

    int updateByPrimaryKey(OpinionEventMediaStatistic record);
}