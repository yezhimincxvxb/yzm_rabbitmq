package com.yzm.ack.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ACK_QUEUE = "ACK_QUEUE";

    @Bean
    public Queue ackQueue() {
        return new Queue(ACK_QUEUE, true, false, false);
    }
}
