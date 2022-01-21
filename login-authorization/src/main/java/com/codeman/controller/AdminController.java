package com.codeman.controller;


import com.codeman.domain.Admin;
import com.codeman.service.IAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/codeman/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @GetMapping("/register")
    public String register(Admin admin) {
        Admin register = adminService.register(admin);
        if (register != null) {
            return "successful";
        }
        return "fail";
    }
}

