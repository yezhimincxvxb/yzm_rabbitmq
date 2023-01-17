package com.yzm.exchange.provider;

import com.yzm.exchange.config.RabbitMQConfig;
import com.yzm.exchange.consumer.Receiver;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/fanout")
    public void fanout() {
        Receiver.count1.set(1);Receiver.count2.set(1);Receiver.count3.set(1);
        for (int i = 1; i <= 10; i++) {
            String message = "Hello World ..." + i;
            rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", message);
            System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        }
    }

    @GetMapping("/direct")
    public void direct() {
        Receiver.count1.set(1);Receiver.count2.set(1);Receiver.count3.set(1);
        String[] routes = {RabbitMQConfig.DIRECT_C, RabbitMQConfig.DIRECT_D, RabbitMQConfig.DIRECT_D2};
        for (int i = 0; i < 10; i++) {
            String message = "Hello World ..." + i;
            System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
            String routingKey = routes[i % routes.length];
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, routingKey, message);
        }
    }

    @GetMapping("/topic")
    public void topic() {
        Receiver.count1.set(1);Receiver.count2.set(1);Receiver.count3.set(1);
        for (int i = 0; i < 10; i++) {
            String message = "Hello World ..." + i;
            System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
            if (i % 3 == 0) {
                // topic.yzm.key，可以匹配 topic.yzm.* 和 topic.#
                rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.yzm.key", message);
            } else if (i % 3 == 1) {
                // topic.yzm.yzm，可以匹配 topic.yzm.* 、 topic.# 和 topic.*.yzm
                rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.yzm.yzm", message);
            } else {
                // topic.key.yzm，可以匹配 topic.# 和 topic.*.yzm
                rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.key.yzm", message);
            }
        }
    }

    @GetMapping("/headers")
    public void headers() {
        String s = "Hello World";
        System.out.println(" [ 生产者 ] Sent ==> '" + s + "'");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("key1", "value1");
        messageProperties.setHeader("name", "yzm");
        Message message = new Message(s.getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(RabbitMQConfig.HEADER_EXCHANGE, "", message);
    }

    @GetMapping("/headers2")
    public void headers2() {
        String s = "Hello World";
        System.out.println(" [ 生产者 ] Sent ==> '" + s + "'");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("key3", "value3");
        messageProperties.setHeader("name", "yzm");
        Message message = new Message(s.getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(RabbitMQConfig.HEADER_EXCHANGE, "", message);
    }

}
