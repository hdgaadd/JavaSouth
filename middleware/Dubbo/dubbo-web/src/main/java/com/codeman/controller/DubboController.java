package com.codeman.controller;

import com.codeman.dubbo.DubboInterface;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/01/25
 */
@RestController
@RequestMapping("/dubbo")
public class DubboController {
    @DubboReference(version = "${dubbo.application.version}", check = false)
    DubboInterface dubboInterface;

    @GetMapping("/test")
    public String test() {
        return dubboInterface.communicate();
    }
}
