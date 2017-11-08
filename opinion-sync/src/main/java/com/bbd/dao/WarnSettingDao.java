package com.bbd.dao;

import com.bbd.domain.WarnSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface WarnSettingDao {

    @Select("select * from bbd_warn_setting where id=#{id}")
    WarnSetting selectById(@Param("id") Long id);

}