package com.codeman.controller;

import com.codeman.annotation.LogOperator;
import com.codeman.entity.User;
import com.codeman.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
public class TestController {
    @Resource
    private UserService userService;

    @RequestMapping("test")
    @LogOperator(operatorName = "666")
    @ResponseBody
    public User testSelect() {
        System.out.println(("-----selectAll method test------"));
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }
}
