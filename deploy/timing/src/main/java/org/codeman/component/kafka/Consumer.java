package org.codeman.component.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/10/03
 */
@Component
@Slf4j
public class Consumer {

    @KafkaListener(topics = "clock-topic", groupId = "kafka-group")
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("listener get message: " + record.value());
        ack.acknowledge();
    }
}

