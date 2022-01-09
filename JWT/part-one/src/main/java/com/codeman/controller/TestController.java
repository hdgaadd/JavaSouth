package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController // 加上get方法可以不添加@RequestBody
@CrossOrigin
public class TestController {
    @Resource
    private UserService userService;

    @GetMapping("/get")
    public User get() {
        System.out.println(("----- get方法触发 ------"));
        User user = userService.getById(1);
        return user;
    }

    @PostMapping("/postString")
    public void postString(@RequestBody String name) {
        System.out.println(("----- postString方法触发 ------"));
        System.out.println("获取前端数据" + name);
    }

    @PostMapping("/postObject")
    public void postObject(@RequestBody User userTestPost) {
        System.out.println(("----- postObject方法触发 ------"));
        System.out.println("获取前端数据" + userTestPost);
    }
}
