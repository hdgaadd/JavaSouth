package org.codeman.component.redisson;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.repository.Clock;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hdgaadd
 * created on 2022/10/01
 */
@Component
@Slf4j
public class Producer implements ApplicationRunner {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    private final static String TOPIC_NAME = "clock-topic";

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Config config = new Config();
//        config.useClusterServers().setScanInterval(2000)
//                .addNodeAddress("redis://106.14.172.7:7001", "redis://106.14.172.7:7002")
//                .addNodeAddress("redis://106.14.172.7:7003");
//        RedissonClient redissonClient = Redisson.create(config);

        RBlockingQueue<Clock> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");

        while (true) {
            Clock clock = blockingFairQueue.take();
            kafkaTemplate.send(TOPIC_NAME, "key", clock.toString());
            log.info("time out: {} , clock created: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), clock.getTime());
        }
    }
}
