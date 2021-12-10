package com.codeman.rocketMQ;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void addMoney() {
        Message message = new Message();
        message.setId(1);
        message.setName("codeman");
        message.setNumber(100);

        rocketMQTemplate.syncSend(Message.Topic,message);

    }
}
