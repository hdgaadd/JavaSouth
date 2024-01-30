package org.codeman;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hdgaadd
 * created on 2022/10/14
 */
@Service
@Slf4j
public class InsideService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String CURRENT_LIMIT_KEY = "CURRENT_LIMIT_KEY";

    private static final int QPS = 1;

    public String pushMsg(String msg) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(CURRENT_LIMIT_KEY);
        // 针对推送接口最高QPS, 设置速率, 1秒中产生QPS个令牌
        rateLimiter.trySetRate(RateType.OVERALL, QPS, 1, RateIntervalUnit.SECONDS);
        // 获取令牌
        boolean isGet = rateLimiter.tryAcquire(1);

        if (isGet) {
            log.info("push message successful on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": " + msg);
        } else {
            kafkaTemplate.send("current-limit-topic", msg);
            log.info("current limiting");
        }
        return "successful!";
    }
}
