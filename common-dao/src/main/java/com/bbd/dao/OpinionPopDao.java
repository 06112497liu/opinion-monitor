package com.bbd.dao;

import com.bbd.domain.OpinionPop;
import com.bbd.domain.OpinionPopExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionPopDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionPop record);

    int insertSelective(OpinionPop record);

    List<OpinionPop> selectByExampleWithPageBounds(OpinionPopExample example, PageBounds pageBounds);

    List<OpinionPop> selectByExample(OpinionPopExample example);

    OpinionPop selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionPop record, @Param("example") OpinionPopExample example);

    int updateByExample(@Param("record") OpinionPop record, @Param("example") OpinionPopExample example);

    int updateByPrimaryKeySelective(OpinionPop record);

    int updateByPrimaryKey(OpinionPop record);
}