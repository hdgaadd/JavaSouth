package org.codeman;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * created on 2023/01/08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Client {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 批量写入缓存减少Redis交互次数
     */
    @Test
    public void testPipeline() {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("1", "a");
        keyMap.put("2", "b");
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                keyMap.forEach((key, value) -> {
                    connection.set(serializer.serialize(key), serializer.serialize(value), Expiration.seconds(1), RedisStringCommands.SetOption.UPSERT);
                });
                return null;
            }
        },serializer);

        for (String key : keyMap.keySet()) {
            log.info("the map value is {}", redisTemplate.opsForValue().get(key));
        }
    }

    @Test
    public void setBigKey() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1024 * 1024 * 1; i++) {
            builder.append("a");
        }

        redisTemplate.opsForValue().set("key", builder.toString());
        String val = redisTemplate.opsForValue().get("key");
        System.out.println(val.getBytes().length / (1024 * 1024) + "MB");
    }

    @Test
    public void redisQueue() {
        redisTemplate.delete("queue");
        for (int i = 0; i < 6; i++) {
            redisTemplate.opsForList().leftPush("queue", i + "");
        }

        for (int i = 0; i < 6; i++) {
            log.debug("the queue is {}", redisTemplate.opsForList().rightPop("queue"));
        }
    }


    /**
     * debug:
     * 2023-01-15 17:43:54.879  INFO 2092 --- [           main] org.codeman.Client                       : info
     * 2023-01-15 17:43:54.879  WARN 2092 --- [           main] org.codeman.Client                       : warn
     * 2023-01-15 17:43:54.879 DEBUG 2092 --- [           main] org.codeman.Client                       : debug
     * 2023-01-15 17:43:54.879 ERROR 2092 --- [           main] org.codeman.Client                       : error
     *
     * info:(默认模式)
     * 2023-01-15 17:43:29.385  INFO 1884 --- [           main] org.codeman.Client                       : info
     * 2023-01-15 17:43:29.385  WARN 1884 --- [           main] org.codeman.Client                       : warn
     * 2023-01-15 17:43:29.385 ERROR 1884 --- [           main] org.codeman.Client                       : error
     *
     * warn:
     * 2023-01-15 17:44:12.782  WARN 1420 --- [           main] org.codeman.Client                       : warn
     * 2023-01-15 17:44:12.782 ERROR 1420 --- [           main] org.codeman.Client                       : error
     *
     * error:
     * 2023-01-15 17:44:31.249 ERROR 11476 --- [           main] org.codeman.Client                       : error
     */
    @Test
    public void testLog() {
        log.info("info");
        log.warn("warn");
        log.debug("debug");
        log.error("error");
    }
}
