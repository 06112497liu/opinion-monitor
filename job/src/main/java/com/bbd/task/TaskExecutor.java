/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.task;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: TaskExecutor.java, v 0.1 2017/10/26 0026 14:42 tjwang Exp $
 */
public interface TaskExecutor<T, V> {

    /**
     * 是否正在运行
     * @return
     */
    boolean isRunning();

    /**
     * 获取当前数据节点
     * @param <T>
     * @return
     */
    <T> T getCurrentItem();

    /**
     * 设置当前节点
     * @param datas
     */
    void setCurrentItem(List<V> datas);

    /**
     * 获取数据
     * @param item
     * @return
     */
    List<V> getData(T item);

    /**
     * 执行工作
     * @param datas
     */
    void doWork(List<V> datas);

    /**
     * 开始任务
     */
    void start();

    /**
     * 结束任务
     */
    void end();

    /**
     * 暂停任务
     */
    void stop();

}
