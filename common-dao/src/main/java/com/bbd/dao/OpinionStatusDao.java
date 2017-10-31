package com.bbd.dao;

import com.bbd.domain.OpinionStatus;
import com.bbd.domain.OpinionStatusExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionStatusDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionStatus record);

    int insertSelective(OpinionStatus record);

    List<OpinionStatus> selectByExampleWithPageBounds(OpinionStatusExample example, PageBounds pageBounds);

    List<OpinionStatus> selectByExample(OpinionStatusExample example);

    OpinionStatus selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionStatus record, @Param("example") OpinionStatusExample example);

    int updateByExample(@Param("record") OpinionStatus record, @Param("example") OpinionStatusExample example);

    int updateByPrimaryKeySelective(OpinionStatus record);

    int updateByPrimaryKey(OpinionStatus record);
}