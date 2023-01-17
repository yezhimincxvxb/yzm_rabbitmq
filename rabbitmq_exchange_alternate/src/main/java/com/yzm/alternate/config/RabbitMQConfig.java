package com.yzm.alternate.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 普通交换机
    public static final String ORDINARY_EXCHANGE = "ordinary-exchange";
    public static final String ORDINARY_QUEUE = "ordinary-queue";
    // 备用交换机
    public static final String ALTERNATE_EXCHANGE = "alternate-exchange";
    public static final String ALTERNATE_QUEUE = "alternate-queue";

    @Bean
    public Queue altQueue() {
        return QueueBuilder.durable(ALTERNATE_QUEUE).build();
    }

    // 备用交换器一般都是设置FANOUT模式
    @Bean
    public FanoutExchange altExchange() {
        return ExchangeBuilder.fanoutExchange(ALTERNATE_EXCHANGE).build();
    }

    @Bean
    public Binding altBinding() {
        return BindingBuilder.bind(altQueue()).to(altExchange());
    }

    //-----------------------------------------------------------------------------------------

    @Bean
    public Queue ordQueue() {
        return QueueBuilder.durable(ORDINARY_QUEUE).build();
    }

    // 普通交换机绑定备用交换机
    @Bean
    public TopicExchange ordExchange() {
        return ExchangeBuilder.topicExchange(ORDINARY_EXCHANGE).withArgument("alternate-exchange", ALTERNATE_EXCHANGE).build();
    }

    @Bean
    public Binding ordBinding() {
        return BindingBuilder.bind(ordQueue()).to(ordExchange()).with("#.ordinary.#");
    }

}
