package com.yzm.alternate.consumer;

import com.yzm.alternate.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitListener(queues = RabbitMQConfig.ORDINARY_QUEUE)
    public void ordinary(Message message) {
        System.out.println(" [ 消费者@普通号 ] 接收到消息 ==> '" + new String(message.getBody()));
    }

    @RabbitListener(queues = RabbitMQConfig.ALTERNATE_QUEUE)
    public void alternate(Message message) {
        System.err.println(" [ 消费者@备用号 ] 接收到消息 ==> '" + new String(message.getBody()));
    }

}
