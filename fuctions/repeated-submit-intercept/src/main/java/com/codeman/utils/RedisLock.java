package com.codeman.utils;

/**
 * @author hdgaadd
 * created on 2022/03/07
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * Redis分布式锁实现，可采用Redisson方案
 */
@Component
public class RedisLock {

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    /**
     * 释放锁脚本
     */
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁操作
     * @param lockKey 锁键
     * @param clientId 随机值，无什么作用
     * @param seconds 锁过期时间 TODO: 与锁的锁定时间有何关联 ：该值与锁的存活时间一致
     * @return
     */
    public boolean tryLock(String lockKey, String clientId, long seconds) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            String result = jedis.set(lockKey, clientId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        });
    }

    /**
     * 释放锁
     * @param lockKey
     * @param clientId
     * @return
     */
    public boolean releaseLock(String lockKey, String clientId) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(clientId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        });
    }
}
