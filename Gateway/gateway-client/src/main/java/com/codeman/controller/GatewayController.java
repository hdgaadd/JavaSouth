package com.codeman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * Created on 2022/01/23
 */
@RestController
@RequestMapping("gateway")
public class GatewayController { // http://localhost:88/api/gateway/test
    @RequestMapping("/test")
    public String gateway() {
        return "welcome to learning Gateway";
    }
}

