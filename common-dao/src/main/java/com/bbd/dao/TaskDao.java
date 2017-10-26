package com.bbd.dao;

import com.bbd.domain.TaskEntity;
import com.bbd.domain.TaskEntityExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskDao {
    int deleteByPrimaryKey(Long id);

    int insert(TaskEntity record);

    int insertSelective(TaskEntity record);

    List<TaskEntity> selectByExampleWithPageBounds(TaskEntityExample example, PageBounds pageBounds);

    List<TaskEntity> selectByExample(TaskEntityExample example);

    TaskEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TaskEntity record, @Param("example") TaskEntityExample example);

    int updateByExample(@Param("record") TaskEntity record, @Param("example") TaskEntityExample example);

    int updateByPrimaryKeySelective(TaskEntity record);

    int updateByPrimaryKey(TaskEntity record);
}