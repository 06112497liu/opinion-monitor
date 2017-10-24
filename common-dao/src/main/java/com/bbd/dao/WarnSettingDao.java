package com.bbd.dao;

import com.bbd.domain.WarnSetting;
import com.bbd.domain.WarnSettingExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WarnSettingDao {
    int deleteByPrimaryKey(Long id);

    int insert(WarnSetting record);

    int insertSelective(WarnSetting record);

    List<WarnSetting> selectByExampleWithPageBounds(WarnSettingExample example, PageBounds pageBounds);

    List<WarnSetting> selectByExample(WarnSettingExample example);

    WarnSetting selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WarnSetting record, @Param("example") WarnSettingExample example);

    int updateByExample(@Param("record") WarnSetting record, @Param("example") WarnSettingExample example);

    int updateByPrimaryKeySelective(WarnSetting record);

    int updateByPrimaryKey(WarnSetting record);
}