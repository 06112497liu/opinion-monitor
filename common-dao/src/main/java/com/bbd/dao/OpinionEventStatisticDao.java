package com.bbd.dao;

import com.bbd.domain.OpinionEventStatistic;
import com.bbd.domain.OpinionEventStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventStatistic record);

    int insertSelective(OpinionEventStatistic record);

    List<OpinionEventStatistic> selectByExampleWithBLOBs(OpinionEventStatisticExample example);

    List<OpinionEventStatistic> selectByExampleWithPageBounds(OpinionEventStatisticExample example, PageBounds pageBounds);

    List<OpinionEventStatistic> selectByExample(OpinionEventStatisticExample example);

    OpinionEventStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventStatistic record, @Param("example") OpinionEventStatisticExample example);

    int updateByExampleWithBLOBs(@Param("record") OpinionEventStatistic record, @Param("example") OpinionEventStatisticExample example);

    int updateByExample(@Param("record") OpinionEventStatistic record, @Param("example") OpinionEventStatisticExample example);

    int updateByPrimaryKeySelective(OpinionEventStatistic record);

    int updateByPrimaryKeyWithBLOBs(OpinionEventStatistic record);

    int updateByPrimaryKey(OpinionEventStatistic record);
}