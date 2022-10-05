package org.codeman;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author hdgaadd
 * Created on 2022/09/25
 */
@Slf4j
@Service
public class RedisService {

    @Resource
    private RedissonClient redissonClient;

    private static final Jedis jedisClient = new Jedis("localhost", 6379);

    private static final String KEY = "key";

    private static final String KEY_MUTEX = "key_mutex";

    /**
     * 缓存时间
     */
    private static final int REWARD_LIST_EXPIRE_SECOND = 3;
    /**
     * 循环次数
     */
    private static final int REWARD_LIST_LOCK_LOOP_NUM = 5;

    public String getByRedisLock(int num) {
        String value = jedisClient.get(KEY);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        RLock lock = redissonClient.getLock(KEY_MUTEX);
        try {
            if (lock.tryLock()) {
                log.info("tryLock");
                String curValue = jedisClient.get(KEY);
                if (StringUtils.isBlank(curValue)) {
                    curValue = getValueByMySQL();

                    curValue = StringUtils.isNotBlank(curValue) ? curValue : "";
                    // setex(): key存在时覆盖 setnx(): key存在不做动作
                    jedisClient.setex(KEY, REWARD_LIST_EXPIRE_SECOND, curValue);
                }
                return curValue;
            } else {
                // 循环获取锁
                num = num + 1;
                if (num > REWARD_LIST_LOCK_LOOP_NUM) {
                    String curValue = jedisClient.get(KEY);
                    if (StringUtils.isNotBlank(curValue)) {
                        return curValue;
                    } else {
                        return "";
                    }
                }
                getByRedisLock(num);
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (lock.isLocked()) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        return null;
    }

    /**
     * 6s后返回结果，模仿MySQL查询操作
     */
    public String getValueByMySQL() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        return "this is service-switch";
    }

    public String deleteKey() {
        return jedisClient.flushDB();
    }
}
