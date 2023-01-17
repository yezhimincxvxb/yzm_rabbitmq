package com.yzm.dead.consumer;

import com.rabbitmq.client.Channel;
import com.yzm.dead.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Receiver {

    // 监听死信队列
    @RabbitListener(queues = RabbitConfig.DEAD_QUEUE)
    public void dl(Message message) {
        log.info(" [ 消费者@死信号 ] 接收到消息 ==> '" + new String(message.getBody()));
    }

    // 队列NORMAL_QUEUE3之前设置最大长度时有6条消息，现在消费但拒绝，最终由死信队列消费
    @RabbitListener(queues = RabbitConfig.NORMAL_QUEUE3)
    public void normal(Message message, Channel channel) throws IOException {
        log.info(" [ 消费者@普通号 ] 消息被拒绝 ==> '" + new String(message.getBody()));
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }
}
