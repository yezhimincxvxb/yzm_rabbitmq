package com.yzm.callback.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * connectionFactory = {CachingConnectionFactory@5627} "CachingConnectionFactory [channelCacheSize=25, host=192.168.192.128, port=5672, active=true rabbitConnectionFactory]"定义rabbit template用于数据的接收和发送
     * connectionFactory：连接工厂
     */
    @Bean("rabbitmq")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //数据转换为json存入消息队列
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        /* 若使用 confirm-callback 或 return-callback，需要配置
         * publisher-confirm-type: correlated
         * publisher-returns: true
         */
        template.setConfirmCallback(confirmCallback());
        template.setReturnsCallback(returnCallback());
        /* 使用return-callback时必须设置mandatory为true，
         * 或者在配置中设置rabbitmq.template.mandatory=true
         */
        //template.setMandatory(true);
        return template;
    }

    // 交换机确认回调
    public static RabbitTemplate.ConfirmCallback confirmCallback() {
        return new RabbitTemplate.ConfirmCallback() {
            /**
             * 消息到达交换机触发回调
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // ack判断消息发送到交换机是否成功
                System.out.println("回调id:" + correlationData.getId());
                if (ack) {
                    // 消息发送成功到达交换机
                    // ...
                    System.out.println("消息成功到达交换机");
                } else {
                    System.err.println("消息到达交换机失败");
                    System.err.println("错误信息：" + cause);
                }
            }
        };
    }

    // 路由队列失败回调
    public static RabbitTemplate.ReturnsCallback returnCallback() {
        return new RabbitTemplate.ReturnsCallback() {
            /**
             * 消息路由失败，回调
             * 消息(带有路由键routingKey)到达交换机，与交换机的所有绑定键进行匹配，匹配不到触发回调
             */
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("交换机：" + returnedMessage.getExchange());
                System.out.println("路由键：" + returnedMessage.getRoutingKey());
                System.out.println("消息主体 : " + returnedMessage.getMessage());
                System.out.println("回复代码 : " + returnedMessage.getReplyCode());
                System.out.println("描述：" + returnedMessage.getReplyText());
            }
        };
    }

}
