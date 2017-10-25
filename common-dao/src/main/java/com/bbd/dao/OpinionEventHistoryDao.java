package com.bbd.dao;

import com.bbd.domain.OpinionEventHistory;
import com.bbd.domain.OpinionEventHistoryExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpinionEventHistoryDao {
    int insert(OpinionEventHistory record);

    int insertSelective(OpinionEventHistory record);

    List<OpinionEventHistory> selectByExampleWithBLOBs(OpinionEventHistoryExample example);

    List<OpinionEventHistory> selectByExampleWithPageBounds(OpinionEventHistoryExample example, PageBounds pageBounds);

    List<OpinionEventHistory> selectByExample(OpinionEventHistoryExample example);

    int updateByExampleSelective(@Param("record") OpinionEventHistory record, @Param("example") OpinionEventHistoryExample example);

    int updateByExampleWithBLOBs(@Param("record") OpinionEventHistory record, @Param("example") OpinionEventHistoryExample example);

    int updateByExample(@Param("record") OpinionEventHistory record, @Param("example") OpinionEventHistoryExample example);
}