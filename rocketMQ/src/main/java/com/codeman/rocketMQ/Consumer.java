package com.codeman.rocketMQ;

import com.codeman.service.PropertyService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(
        topic = Message.Topic,
        consumerGroup = "groupOne" + Message.Topic
)
public class Consumer implements RocketMQListener<Message> {

    @Resource
    private PropertyService propertyService;

    @Override
    public void onMessage(Message message) {
        System.out.println("-----------------消费者接受到消息-----------------");
        System.out.println("id为："+message.getId()+"    "+"name为："+message.getName()+"    "+"增加的金钱为："+message.getNumber());

        propertyService.addMoney(message.getId(), message.getName(), message.getNumber());
        System.out.println("-----------------增加财富成功-----------------");
    }
}
