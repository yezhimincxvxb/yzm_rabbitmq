package com.yzm.prefetch.provider;

import com.yzm.prefetch.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息发布
 */
@RestController
public class Sender {

    @Resource
    private AmqpTemplate template;

    @GetMapping("/hello/{id}")
    public void hello(@PathVariable("id") int id) {
        for (int i = 1; i <= 10; i++) {
            String msg = "Hello World ..." + i;
            System.out.println(" [ 生产者 ] Sent ==> '" + msg + "'");
            String queueName = id == 1 ? RabbitConfig.HELLO_QUEUE : "HELLO_QUEUE" + id;
            template.convertAndSend(queueName, msg);
        }
    }
}
