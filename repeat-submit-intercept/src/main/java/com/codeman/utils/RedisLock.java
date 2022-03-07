package com.codeman.utils;

/**
 * @author hdgaadd
 * Created on 2022/03/07
 */

/**
 * Redis分布式锁实现，可采用Redisson方案
 */
public class RedisLock {

    /**
     * 加锁操作
     * @param lockKey 锁键
     * @param clientId 客户端唯一标识id
     * @param seconds 锁过期时间 TODO: 与锁的锁定时间有何关联
     * @return
     */
    public boolean tryLock(String lockKey, String clientId, long seconds) {

    }

    /**
     * 释放锁
     * @param lockKey
     * @param clientId
     * @return
     */
    public boolean releaseLock(String lockKey, String clientId) {

    }
}
