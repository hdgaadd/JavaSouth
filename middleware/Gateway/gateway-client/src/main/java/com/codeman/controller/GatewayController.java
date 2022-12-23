package com.codeman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/01/23
 * 
 * access url: http://localhost:88/api/gateway/test
 */
@RestController
@RequestMapping("gateway")
public class GatewayController {
    
    @RequestMapping("/test")
    public String gateway() {
        return "welcome to learning Gateway";
    }
}

