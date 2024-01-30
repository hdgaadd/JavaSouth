package org.codeman.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hdgaadd
 * created on 2022/09/24
 */
public class MQConnectionUtil {
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.98.54.212");
        factory.setUsername("hdgaadd");
        factory.setPassword("root");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
