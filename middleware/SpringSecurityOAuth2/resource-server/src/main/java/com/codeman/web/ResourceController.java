package com.codeman.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author hdgaadd
 * created on 2022/01/11
 */
@RestController
public class ResourceController {
    @PostMapping("/user")
    public String getUser() {
        return "user";
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAnyAuthority('admin')") // 必须是POST的提交方式
    public String getAdmin() {
        return "获取资源服务器数据成功";
    }
}
