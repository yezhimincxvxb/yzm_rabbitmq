package com.yzm.exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_A = "fanout_a";
    public static final String QUEUE_B = "fanout_b";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE).build();
    }

    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    //--------------------------------------------------------------------------------------------------------------

    //队列
    public static final String QUEUE_C = "direct_c";
    public static final String QUEUE_D = "direct_d";
    public static final String DIRECT_EXCHANGE = "direct.exchange";

    //路由键
    public static final String DIRECT_C = "direct.yzm";
    public static final String DIRECT_D = "direct.admin";
    public static final String DIRECT_D2 = "direct.root";


    @Bean
    public Queue queueC() {
        return new Queue(QUEUE_C);
    }

    @Bean
    public Queue queueD() {
        return new Queue(QUEUE_D);
    }

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).build();
    }

    @Bean
    public Binding bindingC() {
        return BindingBuilder.bind(queueC()).to(directExchange()).with(DIRECT_C);
    }

    // 队列D有两个路由键
    @Bean
    public Binding bindingD() {
        return BindingBuilder.bind(queueD()).to(directExchange()).with(DIRECT_D);
    }

    @Bean
    public Binding bindingD2() {
        return BindingBuilder.bind(queueD()).to(directExchange()).with(DIRECT_D2);
    }

    //----------------------------------------------------------------------------------------------------------
    //队列
    public static final String QUEUE_E = "topic_e";
    public static final String QUEUE_F = "topic_f";
    public static final String QUEUE_G = "topic_g";
    public static final String TOPIC_EXCHANGE = "topic.exchange";

    //两种特殊字符*与#，用于做模糊匹配，其中*用于匹配一个单词，#用于匹配多个单词（可以是零个）
    public static final String TOPIC_E = "topic.yzm.*";
    public static final String TOPIC_F = "topic.#";
    public static final String TOPIC_G = "topic.*.yzm";

    @Bean
    public Queue queueE() {
        return new Queue(QUEUE_E);
    }

    @Bean
    public Queue queueF() {
        return new Queue(QUEUE_F);
    }

    @Bean
    public Queue queueG() {
        return new Queue(QUEUE_G);
    }

    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE).build();
    }

    @Bean
    public Binding bindingE() {
        return BindingBuilder.bind(queueE()).to(topicExchange()).with(TOPIC_E);
    }

    @Bean
    public Binding bindingF() {
        return BindingBuilder.bind(queueF()).to(topicExchange()).with(TOPIC_F);
    }

    @Bean
    public Binding bindingG() {
        return BindingBuilder.bind(queueG()).to(topicExchange()).with(TOPIC_G);
    }

    //--------------------------------------------------------------------------------------------
    public static final String QUEUE_X = "headers_x";
    public static final String QUEUE_Y = "headers_y";
    public static final String HEADER_EXCHANGE = "headers.exchange";

    @Bean
    public Queue queueX() {
        return new Queue(QUEUE_X);
    }

    @Bean
    public Queue queueY() {
        return new Queue(QUEUE_Y);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return ExchangeBuilder.headersExchange(HEADER_EXCHANGE).build();
    }

    @Bean
    public Binding bindingX() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("name", "yzm");
        // whereAll：表示完全匹配
        return BindingBuilder.bind(queueX()).to(headersExchange()).whereAll(map).match();
    }

    @Bean
    public Binding bindingY() {
        Map<String, Object> map = new HashMap<>();
        map.put("key2", "value2");
        map.put("name", "yzm");
        // whereAny：表示只要有一对键值对能匹配就可以
        return BindingBuilder.bind(queueY()).to(headersExchange()).whereAny(map).match();
    }

}
