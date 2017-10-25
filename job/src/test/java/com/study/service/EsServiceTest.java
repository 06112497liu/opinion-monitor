/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.study.service;

import com.bbd.service.EsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tjwang
 * @version $Id: EsServiceTest.java, v 0.1 2017/10/25 0025 18:00 tjwang Exp $
 */
public class EsServiceTest extends BaseServiceTest {

    @Autowired
    private EsService esService;

    @Test
    public void testCreateIndex() {
        esService.createIndex();
    }

}
