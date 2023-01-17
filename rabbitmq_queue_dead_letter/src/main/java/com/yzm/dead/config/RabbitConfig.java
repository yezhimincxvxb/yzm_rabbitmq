package com.yzm.dead.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    // 死信队列
    public static final String DEAD_EXCHANGE = "dead.exchange";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String DEAD_KEY = "dead.key";

    @Bean
    public DirectExchange dlExchange() {
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE).build();
    }

    @Bean
    public Queue dlQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    @Bean
    public Binding dlBinding() {
        return BindingBuilder.bind(dlQueue()).to(dlExchange()).with(DEAD_KEY);
    }

    // ----------------------------------------------------------------------------------------------------------

    public static final String NORMAL_EXCHANGE = "normal.exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String NORMAL_KEY = "normal.key";

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(NORMAL_EXCHANGE).build();
    }

    /**
     * 正常队列，添加配置
     * 消息过期由队列统一决定
     */
    @Bean
    public Queue norQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);//声明当前队列绑定的死信交换机
        params.put("x-dead-letter-routing-key", DEAD_KEY);//声明当前队列的死信路由键
        // 添加消息过期时间，10秒内还没处理完，就转发给死信队列
        params.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE).withArguments(params).build();
    }

    @Bean
    public Binding norBinding() {
        return BindingBuilder.bind(norQueue()).to(exchange()).with(NORMAL_KEY);
    }

    // ----------------------------------------------------------------------------------------------------------

    public static final String NORMAL_QUEUE2 = "normal_queue2";
    public static final String NORMAL_KEY2 = "normal.key2";

    /**
     * 正常队列，添加配置
     * 消息过期由生产者发送时决定，更为灵活
     */
    @Bean
    public Queue norQueue2() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);//声明当前队列绑定的死信交换机
        params.put("x-dead-letter-routing-key", DEAD_KEY);//声明当前队列的死信路由键
        return QueueBuilder.durable(NORMAL_QUEUE2).withArguments(params).build();
    }

    @Bean
    public Binding norBinding2() {
        return BindingBuilder.bind(norQueue2()).to(exchange()).with(NORMAL_KEY2);
    }

    // ----------------------------------------------------------------------------------------------------------

    public static final String NORMAL_QUEUE3 = "normal_queue3";
    public static final String NORMAL_KEY3 = "normal.key3";

    /**
     * 正常队列，添加配置
     * 设置队列最大长度
     */
    @Bean
    public Queue norQueue3() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);//声明当前队列绑定的死信交换机
        params.put("x-dead-letter-routing-key", DEAD_KEY);//声明当前队列的死信路由键
        params.put("x-max-length", 6);
        return QueueBuilder.durable(NORMAL_QUEUE3).withArguments(params).build();
    }

    @Bean
    public Binding norBinding3() {
        return BindingBuilder.bind(norQueue3()).to(exchange()).with(NORMAL_KEY3);
    }
}
