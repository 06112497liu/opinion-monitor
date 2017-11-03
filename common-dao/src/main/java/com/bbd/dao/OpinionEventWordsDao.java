package com.bbd.dao;

import com.bbd.domain.OpinionEventWords;
import com.bbd.domain.OpinionEventWordsExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventWordsDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventWords record);

    int insertSelective(OpinionEventWords record);

    List<OpinionEventWords> selectByExampleWithBLOBs(OpinionEventWordsExample example);

    List<OpinionEventWords> selectByExampleWithPageBounds(OpinionEventWordsExample example, PageBounds pageBounds);

    List<OpinionEventWords> selectByExample(OpinionEventWordsExample example);

    OpinionEventWords selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventWords record, @Param("example") OpinionEventWordsExample example);

    int updateByExampleWithBLOBs(@Param("record") OpinionEventWords record, @Param("example") OpinionEventWordsExample example);

    int updateByExample(@Param("record") OpinionEventWords record, @Param("example") OpinionEventWordsExample example);

    int updateByPrimaryKeySelective(OpinionEventWords record);

    int updateByPrimaryKeyWithBLOBs(OpinionEventWords record);

    int updateByPrimaryKey(OpinionEventWords record);
}