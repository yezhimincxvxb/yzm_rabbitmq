package com.yzm.ttl.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    // 延时队列
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_QUEUE = "delayed_queue";
    public static final String DELAYED_KEY = "delayed.key";

    @Bean
    public DirectExchange dyExchange() {
        return ExchangeBuilder.directExchange(DELAYED_EXCHANGE).build();
    }

    @Bean
    public Queue dyQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    @Bean
    public Binding dyBinding() {
        return BindingBuilder.bind(dyQueue()).to(dyExchange()).with(DELAYED_KEY);
    }

    // --------------------------------------------------------------------------------------

    public static final String NORMAL_EXCHANGE = "normal.exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String NORMAL_QUEUE2 = "normal_queue2";
    public static final String NORMAL_KEY = "normal.key";
    public static final String NORMAL_KEY2 = "normal.key2";

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(NORMAL_EXCHANGE).build();
    }

    @Bean
    public Queue norQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DELAYED_EXCHANGE);
        params.put("x-dead-letter-routing-key", DELAYED_KEY);
        // 添加消息过期时间，10秒内还没处理完，就转发给死信队列
        params.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE).withArguments(params).build();
    }

    @Bean
    public Binding norBinding() {
        return BindingBuilder.bind(norQueue()).to(exchange()).with(NORMAL_KEY);
    }

    @Bean
    public Queue norQueue2() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DELAYED_EXCHANGE);
        params.put("x-dead-letter-routing-key", DELAYED_KEY);
        // 添加消息过期时间，30秒内还没处理完，就转发给死信队列
        params.put("x-message-ttl", 30000);
        return QueueBuilder.durable(NORMAL_QUEUE2).withArguments(params).build();
    }

    @Bean
    public Binding norBinding2() {
        return BindingBuilder.bind(norQueue2()).to(exchange()).with(NORMAL_KEY2);
    }

    public static final String NORMAL_QUEUE3 = "normal_queue3";
    public static final String NORMAL_KEY3 = "normal.key3";

    @Bean
    public Queue norQueue3() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DELAYED_EXCHANGE);
        params.put("x-dead-letter-routing-key", DELAYED_KEY);
        return QueueBuilder.durable(NORMAL_QUEUE3).withArguments(params).build();
    }

    @Bean
    public Binding norBinding3() {
        return BindingBuilder.bind(norQueue3()).to(exchange()).with(NORMAL_KEY3);
    }
}
