/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 *
 * @author tjwang
 * @version $Id: ServletInitializer.java, v 0.1 2017/10/25 0025 15:01 tjwang Exp $
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringbootApplication.class);
    }
}
