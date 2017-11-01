package com.bbd.dao;

import com.bbd.domain.OpinionIncreaseStatistic;
import com.bbd.domain.OpinionIncreaseStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionIncreaseStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionIncreaseStatistic record);

    int insertSelective(OpinionIncreaseStatistic record);

    List<OpinionIncreaseStatistic> selectByExampleWithPageBounds(OpinionIncreaseStatisticExample example, PageBounds pageBounds);

    List<OpinionIncreaseStatistic> selectByExample(OpinionIncreaseStatisticExample example);

    OpinionIncreaseStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionIncreaseStatistic record, @Param("example") OpinionIncreaseStatisticExample example);

    int updateByExample(@Param("record") OpinionIncreaseStatistic record, @Param("example") OpinionIncreaseStatisticExample example);

    int updateByPrimaryKeySelective(OpinionIncreaseStatistic record);

    int updateByPrimaryKey(OpinionIncreaseStatistic record);
}