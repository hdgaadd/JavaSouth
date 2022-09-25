package com.codeman.controller;

import com.codeman.service.AdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * Created on 2022/01/20
 */
@Api(tags = "权限测试")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String getToken(String username) { // hdgaadd
        return adminService.getToken(username);
    }

    // token: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoZGdhYWRkIiwiY3JlYXRlZCI6MTY0MjY5MzQ0MjgzMCwiZXhwIjoxNjQzMjk4MjQyfQ.KeZpW1FtBalD1luCS9ZAk8woPxglrQmgJ9oEXGCrUCqLmDDD2lYusBamqA_MsJDsDIVtQerWRmpXVWNX7aAQyg
    @GetMapping("/authority")
    @PreAuthorize("hasAuthority('admin')")
    public String getMessage() {
        return "登录成功";
    }

    // 测试没有该权限
    @GetMapping("/authority2")
    @PreAuthorize("hasAuthority('admin2')")
    public String getMessage2() {
        return "登录成功";
    }
}
