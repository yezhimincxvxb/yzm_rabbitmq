package com.yzm.ack.consumer;

import com.rabbitmq.client.Channel;
import com.yzm.ack.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息监听
 */
@Component
public class ReceiverAck {

    private final AtomicInteger count1 = new AtomicInteger(1);
    private final AtomicInteger count2 = new AtomicInteger(1);

    @RabbitListener(queues = RabbitConfig.ACK_QUEUE)
    public void receive1(Message message, Channel channel,
                         @Headers Map<String, Object> map,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException, InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@1号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@1号 ] 处理消息数：" + count1.getAndIncrement());
        System.out.println("获取消息ID的三种方式：" + message.getMessageProperties().getDeliveryTag() + " | " + map.get(AmqpHeaders.DELIVERY_TAG) + " | " + deliveryTag);

        // 确认消息
        // 第一个参数，交付标签，相当于消息ID 64位的长整数(从1开始递增)
        // 第二个参数，false表示仅确认提供的交付标签；true表示批量确认所有消息(消息ID小于自身的ID)，包括提供的交付标签
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = RabbitConfig.ACK_QUEUE)
    public void receive2(Message message, Channel channel) throws IOException, InterruptedException {
        Thread.sleep(1000);
        System.out.println(" [ 消费者@2号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@2号 ] 消息被我拒绝了：" + count2.getAndIncrement());

        // 拒绝消息方式一
        // 第一个参数，交付标签
        // 第二个参数，false表示仅拒绝提供的交付标签；true表示批量拒绝所有消息，包括提供的交付标签
        // 第三个参数，false表示直接丢弃消息，true表示重新排队
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);

        // 拒绝消息方式二
        // 第一个参数，交付标签
        // 第二个参数，false表示直接丢弃消息，true表示重新排队
        // 跟basicNack的区别就是始终只拒绝提供的交付标签
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
    }
}
