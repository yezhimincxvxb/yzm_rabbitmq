package com.yzm.retry.provider;

import com.yzm.retry.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/retry")
    public void retry() {
        String message = "Hello World !";
        System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend(RabbitConfig.RETRY_EXCHANGE, RabbitConfig.RETRY_KEY, message);
    }

}
