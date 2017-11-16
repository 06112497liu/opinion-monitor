package com.bbd.dao;

import com.bbd.domain.WarnNotifier;
import com.bbd.domain.WarnNotifierExample;
import com.mybatis.domain.PageBounds;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarnNotifierExtDao {

    /**
     * 删除指定配置id下的预警通知人
     * @param settingId
     * @return
     */
    int delNotifierBySettingId(@Param(value = "settingId") Long settingId);

    /**
     * 批量添加预警通知人
     * @param list
     * @return
     */
    int batchInsertNotifier(List<WarnNotifier> list);

}