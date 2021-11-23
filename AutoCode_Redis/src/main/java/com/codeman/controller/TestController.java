package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.RedisService;
import com.codeman.service.UserService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

@Api(tags = "swagger-ui测试")
@RestController
public class TestController {

    @Resource
    private UserService userService;
    @Resource
    private RedisService redisSerivice;

    @Value("${redis.key.prefix.autoCode}")
    private String AUTO_CODE_PREFIX;
    @Value("${redis.key.expire.autoCode}")
    private int AUTO_CODE_EXPIRE_SOUNDS;

    private String phone;


    @GetMapping("/")
    @ApiOperation("测试url")
    public User testSelect() {
        System.out.println(("----- selectAll method test ------"));
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }

    @PostMapping("/getAutoCode")
    @ApiOperation("获取验证码")
    public String getAutoCode(@RequestBody String phone) {
        this.phone = phone;

        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(rd.nextInt(10));
        }
        String autoCode = sb.toString();

        redisSerivice.setKey(AUTO_CODE_PREFIX + phone, autoCode);
        redisSerivice.expire(AUTO_CODE_PREFIX + phone, AUTO_CODE_EXPIRE_SOUNDS);
        return autoCode; //如果不加RestController，表示的是请求转发到一个页面里
    }

    @PostMapping("/verifyAutoCode")
    @ApiOperation("验证验证码")
    public String verifyAutoCode(@RequestParam String autoCode) {
        if (StringUtil.isNullOrEmpty(autoCode)) return "please input autoCode";

        if (autoCode.equals(redisSerivice.getKey(AUTO_CODE_PREFIX + phone))) {
            return "autoCode is right";
        } else {
            return "autoCode is not right";
        }
    }
}
