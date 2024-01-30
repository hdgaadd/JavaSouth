package org.codeman;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hdgaadd
 * created on 2022/11/05
 */
@Service
@Slf4j
public class InsideService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private BloomFilterHelper<String> bloomFilterHelper;

    private static final Cache<String, Boolean> CACHE_MAP = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(10000)
            .build();

    private static final String CONFIG_KEY = "config_key";

    public void setConfigSwitch(String configVal) {
        int[] bitArr = bloomFilterHelper.murmurHashOffset(configVal);
        for (int bit : bitArr) {
            redisTemplate.opsForValue().setBit(CONFIG_KEY, bit, true);
        }
    }

    public Boolean getConfigSwitch(String configVal) {
        Boolean result = null;
        if ((result = CACHE_MAP.getIfPresent(configVal)) != null) {
            log.info("get info from cache!");
            return result;
        }

        result = Boolean.TRUE;
        int[] bitArr = bloomFilterHelper.murmurHashOffset(configVal);
        for (int bit : bitArr) {
            // 每次查询使用for，需要用到Redis多个连接
            Boolean isBitExist = redisTemplate.opsForValue().getBit(CONFIG_KEY, bit);
            if (!isBitExist) {
                result = false;
                break;
            }
        }

        CACHE_MAP.put(configVal, result);
        return result;
    }
}
