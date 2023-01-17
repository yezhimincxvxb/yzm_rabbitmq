package com.yzm.ack.provider;

import com.yzm.ack.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息发布
 */
@RestController
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/ack")
    public void ack() {
        for (int i = 1; i <= 10; i++) {
            String msg = "Hello World ..." + i;
            System.out.println(" [ 生产者 ] Sent ==> '" + msg + "'");
            rabbitTemplate.convertAndSend(RabbitConfig.ACK_QUEUE, msg);
        }
    }
}
