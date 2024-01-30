package org.codeman;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.codeman.util.JSONUtil;
import org.codeman.util.MQConnectionUtil;

/**
 * @author hdgaadd
 * created on 2022/09/24
 */
@Slf4j
public class QueueClient {
    /**
     * 交换机
     */
    private static final String EXCHANGE_NAME = "maxwell";
    /**
     * 创建队列，队列名以rabbitmq_routing_key_template=%db%.%table%配置为标准，故队列名为
     */
    private static final String QUEUE_NAME = "hdgaadd.test";

    public static void main(String[] args) throws Exception {
        Channel channel = MQConnectionUtil.getChannel();
        // 创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 队列与交换机绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 接收消息
        DeliverCallback handler = (consumerTag, message) -> {
            String messageStr = new String(message.getBody());
            log.info(String.format("\n\rRabbitMQ - %s - getMessage: %s \n\rRabbitMQ - %s - SQL: %s", QUEUE_NAME, messageStr, QUEUE_NAME, JSONUtil.JSONToSQL(messageStr)));
        };
        // 消费消息
        channel.basicConsume(QUEUE_NAME, true, handler, consumerTag -> {
        });
    }
}