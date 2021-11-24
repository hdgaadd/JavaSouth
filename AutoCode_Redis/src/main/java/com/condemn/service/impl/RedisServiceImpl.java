package com.condemn.service.impl;

import com.condemn.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate sr;


    @Override
    public String getKey(String key) {
        return sr.opsForValue().get(key);
    }

    @Override
    public void setKey(String key, String val) {
        sr.opsForValue().set(key, val);
    }

    @Override
    public void expire(String key, int sounds) {

    }
}
