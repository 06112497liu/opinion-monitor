/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.TaskDao;
import com.bbd.domain.TaskEntity;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务服务
 * @author tjwang
 * @version $Id: TaskService.java, v 0.1 2017/10/26 0026 15:17 tjwang Exp $
 */
@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    public TaskEntity getById(Long id) {
        Preconditions.checkNotNull(id);
        return taskDao.selectByPrimaryKey(id);
    }

    public void updateTask(TaskEntity entity) {
        taskDao.updateByPrimaryKey(entity);
    }
}
