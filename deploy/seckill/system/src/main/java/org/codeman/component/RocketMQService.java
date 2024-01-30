package org.codeman.component;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2021/12/12
 */
@Service
public class RocketMQService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic, String messageBody) throws Exception {
        Message message = new Message(topic, messageBody.getBytes());
        rocketMQTemplate.getProducer().send(message);
    }

    public void sendMessage(String topic, String messageBody, int delayTimeLevel) throws Exception {
        Message message = new Message(topic, messageBody.getBytes());
        message.setDelayTimeLevel(delayTimeLevel);
        rocketMQTemplate.getProducer().send(message);
    }
}
