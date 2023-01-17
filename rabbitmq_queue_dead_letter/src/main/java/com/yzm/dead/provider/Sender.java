package com.yzm.dead.provider;

import com.yzm.dead.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/dl")
    public void dl() {
        String message = "Hello World!";
        log.info(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY, message);
    }

    @GetMapping("/dl2/{expiration}")
    public void dl2(@PathVariable("expiration") String expiration) {
        String s = "Hello World!";
        log.info(" [ 生产者 ] Sent ==> '" + s + "'");
        //设置过期时间
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(expiration);
        Message message = new Message(s.getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY2, message);
    }

    @GetMapping("/dl3")
    public void dl3() {
        // 发送10条消息给队列NORMAL_QUEUE3，队列最大存储6条消息，多余的转发给了死信队列
        for (int i = 0; i < 10; i++) {
            String message = "Hello World!..." + i;
            log.info(" [ 生产者 ] Sent ==> '" + message + "'");
            rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY3, message);
        }
    }

}
