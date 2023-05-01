package org.codeman.controller;


import org.codeman.service.IMybatisPlusService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mybatis-plus")
public class MybatisPlusController {
    @Resource
    private IMybatisPlusService iMybatisPlusService;

    @RequestMapping("/lambdaQuery")
    public void lambdaQuery() {
        iMybatisPlusService.queryTest();
    }

    @RequestMapping("/insertNull")
    public void insertNull() {
        iMybatisPlusService.insertNull();
    }

}


