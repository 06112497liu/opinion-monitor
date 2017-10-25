package com.bbd.dao;

import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionDictionaryDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionDictionary record);

    int insertSelective(OpinionDictionary record);

    List<OpinionDictionary> selectByExampleWithPageBounds(OpinionDictionaryExample example, PageBounds pageBounds);

    List<OpinionDictionary> selectByExample(OpinionDictionaryExample example);

    OpinionDictionary selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionDictionary record, @Param("example") OpinionDictionaryExample example);

    int updateByExample(@Param("record") OpinionDictionary record, @Param("example") OpinionDictionaryExample example);

    int updateByPrimaryKeySelective(OpinionDictionary record);

    int updateByPrimaryKey(OpinionDictionary record);
}