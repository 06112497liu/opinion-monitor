package com.bbd.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * @author Liuweibo
 * @version Id: OpinionJobServiceTest.java, v0.1 2017/11/23 Liuweibo Exp $$
 */
public class OpinionJobServiceTest extends BaseServiceTest {

    @Autowired
    private OpinionJobService jobService;

    @Test
    public void testPushKeyWords() throws UnsupportedEncodingException {

        jobService.pushKeyWords();

    }

}
    
    