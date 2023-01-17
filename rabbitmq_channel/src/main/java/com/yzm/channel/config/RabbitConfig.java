package com.yzm.channel.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {

    public static final String HELLO_QUEUE = "HELLO_QUEUE";

    // 获取MQ连接
    public static Connection getConnection() {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.192.128");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("1234");
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
