/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.task;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: AbstractTaskExecutor.java, v 0.1 2017/10/26 0026 14:50 tjwang Exp $
 */
public abstract class AbstractTaskExecutor<T, V> implements TaskExecutor<T, V> {

    private volatile boolean running;

    /**
     * 执行任务
     */
    public void execute() {
        new Thread(() -> {
            start();
            while (true) {
                T currentItem = getCurrentItem();
                List<V> data = getData(currentItem);
                if (data == null || data.size() == 0) {
                    break;
                }
                doWork(data);
                setCurrentItem(data);
            }
            end();
        }).start();
    }

}
