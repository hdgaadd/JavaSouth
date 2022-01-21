package com.codeman.service;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class TestImpl { //不用创建Service的接口类，也是直接使用Impl类

    @Resource
    private RedisService redisSerivice;

    @Value("${redis.key.prefix.autoCode}")
    private String AUTO_CODE_PREFIX;
    @Value("${redis.key.expire.autoCode}")
    private int AUTO_CODE_EXPIRE_SOUNDS;
    private String phone;


    public String generateAutoCode(String phone) {
        this.phone = phone;

        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(rd.nextInt(10));
        }
        String autoCode = sb.toString();

        redisSerivice.setKey(AUTO_CODE_PREFIX + phone, autoCode);
        redisSerivice.expire(AUTO_CODE_PREFIX + phone, AUTO_CODE_EXPIRE_SOUNDS);

        return autoCode;
    }

    public String verifyAutoCode(String autoCode) {
        if (StringUtil.isNullOrEmpty(autoCode)) return "please input autoCode";

        if (autoCode.equals(redisSerivice.getKey(AUTO_CODE_PREFIX + phone))) {
            return "autoCode is right";
        } else {
            return "autoCode is not right";
        }
    }
}
