package com.bbd.dao;

import com.bbd.domain.OpinionEventLevelChange;
import com.bbd.domain.OpinionEventLevelChangeExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventLevelChangeDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventLevelChange record);

    int insertSelective(OpinionEventLevelChange record);

    List<OpinionEventLevelChange> selectByExampleWithPageBounds(OpinionEventLevelChangeExample example, PageBounds pageBounds);

    List<OpinionEventLevelChange> selectByExample(OpinionEventLevelChangeExample example);

    OpinionEventLevelChange selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventLevelChange record, @Param("example") OpinionEventLevelChangeExample example);

    int updateByExample(@Param("record") OpinionEventLevelChange record, @Param("example") OpinionEventLevelChangeExample example);

    int updateByPrimaryKeySelective(OpinionEventLevelChange record);

    int updateByPrimaryKey(OpinionEventLevelChange record);
}