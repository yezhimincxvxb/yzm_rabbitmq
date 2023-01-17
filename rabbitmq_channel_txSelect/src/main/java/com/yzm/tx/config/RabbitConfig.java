package com.yzm.tx.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean(name = "channel")
    public Channel channel(ConnectionFactory connectionFactory) {
        // true，启动事务；
        return connectionFactory.createConnection().createChannel(true);
    }
}
