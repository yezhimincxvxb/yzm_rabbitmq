package com.yzm.confirm.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean(name = "channel")
    public Channel channel(ConnectionFactory connectionFactory) {
        // 需要关闭事务；
        return connectionFactory.createConnection().createChannel(false);
    }
}
