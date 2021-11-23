package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags="swagger-ui测试")
@RestController
public class TestController {

    @Resource
    private UserService userService;

    @GetMapping("/")
    @ApiOperation("测试url")
    public User testSelect() {
        System.out.println(("----- selectAll method test ------"));
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }
}
