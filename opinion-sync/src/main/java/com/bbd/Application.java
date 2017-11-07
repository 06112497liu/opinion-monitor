/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author tjwang
 * @version $Id: Application.java, v 0.1 2017/11/7 0007 19:33 tjwang Exp $
 */
@SpringBootApplication
@MapperScan("com.bbd.dao")
public class Application {

    public static void main(String[] args) {

    }

}
