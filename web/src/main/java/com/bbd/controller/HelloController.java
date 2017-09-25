/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller;

import com.bbd.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tjwang
 * @version $Id: HelloController.java, v 0.1 2017/9/25 0025 13:47 tjwang Exp $
 */
@RestController
@RequestMapping("/yc/hello")
public class HelloController extends AbstractController {

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public RestResult sayHello() {
        return RestResult.ok("Hello Abnormal");
    }

}
