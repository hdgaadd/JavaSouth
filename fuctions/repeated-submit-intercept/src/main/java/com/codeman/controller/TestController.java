package com.codeman.controller;

import com.codeman.annotation.BanRepeatSubmit;
import com.codeman.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/03/07
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/banRepeatSubmit")
    @BanRepeatSubmit(lockTime = 6)
    public String testSubmit(User user) {
        try {
            Thread.sleep(3000); // 线程睡眠，模拟业务执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("success");
        return "successful";
    }
}
