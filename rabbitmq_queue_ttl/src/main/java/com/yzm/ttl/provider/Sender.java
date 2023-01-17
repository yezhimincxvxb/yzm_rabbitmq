package com.yzm.ttl.provider;

import com.yzm.ttl.config.RabbitConfig;
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

    @GetMapping("/delayed")
    public void delayed() {
        String message = "Hello World!";
        log.info(" [ 生产者 ] Sent ==> '" + message + "'");
        rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY, message);
        rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY2, message);
    }

    // 先发送30s，再发送2s的
    @GetMapping("/delayed2/{expiration}")
    public void delayed2(@PathVariable("expiration") long expiration) {
        String s = "Hello World!";
        log.info(" [ 生产者 ] Sent ==> '" + s + "'");
        //设置过期时间
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(String.valueOf(expiration * 1000));
        Message message = new Message(s.getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(RabbitConfig.NORMAL_EXCHANGE, RabbitConfig.NORMAL_KEY3, message);
    }

}
