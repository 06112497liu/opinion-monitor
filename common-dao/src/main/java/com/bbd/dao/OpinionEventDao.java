package com.bbd.dao;

import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEvent record);

    int insertSelective(OpinionEvent record);

    List<OpinionEvent> selectByExampleWithBLOBs(OpinionEventExample example);

    List<OpinionEvent> selectByExampleWithPageBounds(OpinionEventExample example, PageBounds pageBounds);

    List<OpinionEvent> selectByExample(OpinionEventExample example);

    OpinionEvent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEvent record, @Param("example") OpinionEventExample example);

    int updateByExampleWithBLOBs(@Param("record") OpinionEvent record, @Param("example") OpinionEventExample example);

    int updateByExample(@Param("record") OpinionEvent record, @Param("example") OpinionEventExample example);

    int updateByPrimaryKeySelective(OpinionEvent record);

    int updateByPrimaryKeyWithBLOBs(OpinionEvent record);

    int updateByPrimaryKey(OpinionEvent record);
}