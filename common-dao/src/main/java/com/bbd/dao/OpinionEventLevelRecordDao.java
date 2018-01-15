package com.bbd.dao;

import com.bbd.domain.OpinionEventLevelRecord;
import com.bbd.domain.OpinionEventLevelRecordExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventLevelRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(OpinionEventLevelRecord record);

    int insertSelective(OpinionEventLevelRecord record);

    List<OpinionEventLevelRecord> selectByExampleWithPageBounds(OpinionEventLevelRecordExample example, PageBounds pageBounds);

    List<OpinionEventLevelRecord> selectByExample(OpinionEventLevelRecordExample example);

    OpinionEventLevelRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpinionEventLevelRecord record, @Param("example") OpinionEventLevelRecordExample example);

    int updateByExample(@Param("record") OpinionEventLevelRecord record, @Param("example") OpinionEventLevelRecordExample example);

    int updateByPrimaryKeySelective(OpinionEventLevelRecord record);

    int updateByPrimaryKey(OpinionEventLevelRecord record);
}