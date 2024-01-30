package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2022/09/25
 *
 * design: Redis缓存 + Redis分布式锁(高并发) & 短的键缓存时间(低延迟)
 */
@RestController
public class OpenController {
    @Resource
    private RedisService redisService;

    private int requestIndex = 0;

    @GetMapping("/getKey")
    public String getKey() {
        return redisService.getByRedisLock(0) + "-" + requestIndex++;
    }
}
