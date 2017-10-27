package com.bbd.dao;

import com.bbd.domain.SimiliarNews;
import com.bbd.domain.SimiliarNewsExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SimiliarNewsDao {
    int deleteByPrimaryKey(Long id);

    int insert(SimiliarNews record);

    int insertSelective(SimiliarNews record);

    List<SimiliarNews> selectByExampleWithBLOBs(SimiliarNewsExample example);

    List<SimiliarNews> selectByExampleWithPageBounds(SimiliarNewsExample example, PageBounds pageBounds);

    List<SimiliarNews> selectByExample(SimiliarNewsExample example);

    SimiliarNews selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SimiliarNews record, @Param("example") SimiliarNewsExample example);

    int updateByExampleWithBLOBs(@Param("record") SimiliarNews record, @Param("example") SimiliarNewsExample example);

    int updateByExample(@Param("record") SimiliarNews record, @Param("example") SimiliarNewsExample example);

    int updateByPrimaryKeySelective(SimiliarNews record);

    int updateByPrimaryKeyWithBLOBs(SimiliarNews record);

    int updateByPrimaryKey(SimiliarNews record);
}