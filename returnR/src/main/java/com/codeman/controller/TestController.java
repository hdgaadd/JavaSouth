package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.UserService;
import com.codeman.service.impl.UserServiceImpl;
import com.codeman.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
public class TestController {

    @Resource
    private UserService userService;

    @RequestMapping("/")
    @ResponseBody
    public User testSelect() {
        System.out.println(("----- selectAll method test ------"));
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }

    @RequestMapping("/test")
    @ResponseBody
    public R test() {
        return R.ok(userService.getById(1));
    }
}
