package com.yzm.priority.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PRI_QUEUE = "PRI_QUEUE";

    @Bean(name = "channel")
    public Channel channel(ConnectionFactory connectionFactory) {
        return connectionFactory.createConnection().createChannel(false);
    }
}
