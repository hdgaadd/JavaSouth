package org.codeman;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/10/03
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "current-limit-topic", groupId = "kafka-group")
    public void listener(ConsumerRecord<String, String> record) {
        log.info("listener get message & insert DB: " + record.value());
    }
}

