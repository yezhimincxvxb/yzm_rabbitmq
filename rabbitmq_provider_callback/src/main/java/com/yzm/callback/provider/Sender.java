package com.yzm.callback.provider;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class Sender {

    @Resource(name = "rabbitmq")
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/callback")
    public void callback() {
        // 全局唯一
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        String message = "Hello world!";
        System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend("callback_exchange", "callback.a.yzm", message, correlationData);
    }

    @GetMapping("/callback2")
    public void callback2() {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        String message = "Hello world!";
        System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend("不存在的交换机", "callback.a.yzm", message, correlationData);
    }

    @GetMapping("/callback3")
    public void callback3() {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        String message = "Hello world!";
        System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend("callback_exchange", "不存在的路由键", message, correlationData);
    }

}
