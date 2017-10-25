/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tjwang
 * @version $Id: HelloController.java, v 0.1 2017/10/25 0025 14:45 tjwang Exp $
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello opinion job";
    }

}
