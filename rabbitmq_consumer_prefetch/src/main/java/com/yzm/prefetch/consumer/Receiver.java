package com.yzm.prefetch.consumer;

import com.rabbitmq.client.Channel;
import com.yzm.prefetch.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息监听
 */
@Component
public class Receiver {

    private final AtomicInteger count1 = new AtomicInteger(1);
    private final AtomicInteger count2 = new AtomicInteger(1);

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE)
    public void receive1(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@1号 ] Received ==> '" + message + "'");
        System.out.println(" [ 消费者@1号 ] 处理消息数：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE)
    public void receive2(Message message) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(" [ 消费者@2号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@2号 ] 处理消息数：" + count2.getAndIncrement());
    }

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE2, containerFactory = RabbitConfig.PREFETCH_ONE)
    public void receive3(Message message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@3号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@3号 ] 处理消息数：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE2, containerFactory = RabbitConfig.PREFETCH_ONE)
    public void receive4(Message message) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(" [ 消费者@4号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@4号 ] 处理消息数：" + count2.getAndIncrement());
    }

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE3, containerFactory = RabbitConfig.PREFETCH_ONE)
    public void receive5(Message message, Channel channel) throws InterruptedException, IOException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@5号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@5号 ] 处理消息数：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitConfig.HELLO_QUEUE3, containerFactory = RabbitConfig.PREFETCH_TWO)
    public void receive6(Message message, Channel channel) throws InterruptedException, IOException {
        Thread.sleep(1000);
        System.out.println(" [ 消费者@6号 ] Received ==> '" + new String(message.getBody()) + "'");
        System.out.println(" [ 消费者@6号 ] 处理消息数：" + count2.getAndIncrement());
    }

}
