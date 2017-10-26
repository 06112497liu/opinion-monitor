/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.study.task;

import com.bbd.task.OpinionEsDataSyncTaskExecutor;
import com.study.service.BaseServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tjwang
 * @version $Id: OpinionEsDataSyncTaskExecutorTest.java, v 0.1 2017/10/26 0026 15:51 tjwang Exp $
 */
public class OpinionEsDataSyncTaskExecutorTest extends BaseServiceTest {

    @Autowired
    private OpinionEsDataSyncTaskExecutor executor;

    @Test
    public void testExecute() {
        executor.execute();
        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
