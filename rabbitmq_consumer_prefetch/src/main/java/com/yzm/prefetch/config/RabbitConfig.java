package com.yzm.prefetch.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String HELLO_QUEUE = "HELLO_QUEUE";
    public static final String HELLO_QUEUE2 = "HELLO_QUEUE2";
    public static final String HELLO_QUEUE3 = "HELLO_QUEUE3";
    public static final String PREFETCH_ONE = "PREFETCH_ONE";
    public static final String PREFETCH_TWO = "PREFETCH_TWO";

    @Bean
    public Queue queue() {
        return new Queue(HELLO_QUEUE, true, false, false);
    }

    @Bean
    public Queue queue2() {
        return new Queue(HELLO_QUEUE2, true, false, false);
    }

    @Bean
    public Queue queue3() {
        return new Queue(HELLO_QUEUE3, true, false, false);
    }


    @Bean(name = PREFETCH_ONE)
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchOne(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 手动确认
        // factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置prefetch
        factory.setPrefetchCount(1);
        return factory;
    }

    @Bean(name = PREFETCH_TWO)
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchTwo(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 默认为0，prefetch值决定channel最多可以存储多少条消息
        factory.setPrefetchCount(3);
        return factory;
    }
}
