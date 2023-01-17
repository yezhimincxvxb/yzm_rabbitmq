package com.yzm.alternate.provider;

import com.yzm.alternate.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/alternate")
    public void alternate() {
        String[] routs = {"ordinary.a", "alternate.b", "other.c"};
        for (int i = 0; i < 10; i++) {
            String routing = routs[i % 3];
            String message = routing + " ==> " + i;
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDINARY_EXCHANGE, routing, message);
            System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        }
    }

}
