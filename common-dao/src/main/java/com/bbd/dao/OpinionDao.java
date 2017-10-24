package com.bbd.dao;

import com.bbd.domain.Opinion;
import com.bbd.domain.OpinionExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionDao {
    int deleteByPrimaryKey(Long id);

    int insert(Opinion record);

    int insertSelective(Opinion record);

    List<Opinion> selectByExampleWithBLOBs(OpinionExample example);

    List<Opinion> selectByExampleWithPageBounds(OpinionExample example, PageBounds pageBounds);

    List<Opinion> selectByExample(OpinionExample example);

    Opinion selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Opinion record, @Param("example") OpinionExample example);

    int updateByExampleWithBLOBs(@Param("record") Opinion record, @Param("example") OpinionExample example);

    int updateByExample(@Param("record") Opinion record, @Param("example") OpinionExample example);

    int updateByPrimaryKeySelective(Opinion record);

    int updateByPrimaryKeyWithBLOBs(Opinion record);

    int updateByPrimaryKey(Opinion record);
}