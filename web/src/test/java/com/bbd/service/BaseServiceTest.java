/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试dao层的基础类
 *
 * @author tjwang
 * @version $Id: BaseDaoTest.java, v 0.1 2017/1/25 0025 17:29 tjwang Exp $
 */
@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseServiceTest extends Assert {

    protected Logger logger = LoggerFactory.getLogger(getClass());

}
