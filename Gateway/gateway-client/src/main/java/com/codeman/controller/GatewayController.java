package com.codeman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * Created on 2022/01/23
 */
@RestController
@RequestMapping("lee2")
public class GatewayController { // http://localhost:88/api/lee2/gateway
    @RequestMapping("/gateway")
    public String gateway() {
        return "welcome to learning gateway";
    }
}

