package com.yzm.ttl.consumer;

import com.yzm.ttl.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Receiver {

    // 监听延时队列
    @RabbitListener(queues = RabbitConfig.DELAYED_QUEUE)
    public void delayed(Message message) {
        log.info(" [ 消费者@延时号 ] 接收到消息 ==> '" + new String(message.getBody()));
    }

}
