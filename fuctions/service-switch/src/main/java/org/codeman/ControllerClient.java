package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2022/09/25
 */
@RestController
public class ControllerClient {
    @Resource
    private RedisService redisService;

    private int requestIndex = 0;

    @GetMapping("/getKey")
    public String getKey() {
        return redisService.getByRedisLock(0) + requestIndex++;
    }

    @GetMapping("/deleteKey")
    public String deleteKey() {
        return redisService.deleteKey();
    }

}
