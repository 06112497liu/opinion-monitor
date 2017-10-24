package com.bbd.dao;

import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnNotifierExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WarnNotifierDao {
    int deleteByPrimaryKey(Long id);

    int insert(WarnNotifier record);

    int insertSelective(WarnNotifier record);

    List<WarnNotifier> selectByExampleWithPageBounds(WarnNotifierExample example, PageBounds pageBounds);

    List<WarnNotifier> selectByExample(WarnNotifierExample example);

    WarnNotifier selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WarnNotifier record, @Param("example") WarnNotifierExample example);

    int updateByExample(@Param("record") WarnNotifier record, @Param("example") WarnNotifierExample example);

    int updateByPrimaryKeySelective(WarnNotifier record);

    int updateByPrimaryKey(WarnNotifier record);
}