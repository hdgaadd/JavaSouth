package org.codeman;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2023/01/08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Client {

    @Resource
    private RedisTemplate<String, String> template;

    @Test
    public void setBigKey() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1024 * 1024 * 10; i++) {
            builder.append("a");
        }

        template.opsForValue().set("key", builder.toString());
        System.out.println(builder.toString().getBytes().length / (1024 * 1024) + "MB");
    }
}
