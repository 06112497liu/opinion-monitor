package com.bbd.dao;

import com.bbd.domain.OpinionWarnCountStatistic;
import com.bbd.domain.OpinionWarnCountStatisticExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionWarnCountStatisticDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionWarnCountStatistic record);

    int insertSelective(OpinionWarnCountStatistic record);

    List<OpinionWarnCountStatistic> selectByExampleWithPageBounds(OpinionWarnCountStatisticExample example, PageBounds pageBounds);

    List<OpinionWarnCountStatistic> selectByExample(OpinionWarnCountStatisticExample example);

    OpinionWarnCountStatistic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionWarnCountStatistic record, @Param("example") OpinionWarnCountStatisticExample example);

    int updateByExample(@Param("record") OpinionWarnCountStatistic record, @Param("example") OpinionWarnCountStatisticExample example);

    int updateByPrimaryKeySelective(OpinionWarnCountStatistic record);

    int updateByPrimaryKey(OpinionWarnCountStatistic record);
}