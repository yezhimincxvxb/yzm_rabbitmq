package com.yzm.exchange.consumer;

import com.yzm.exchange.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Receiver {

    public static final AtomicInteger count1 = new AtomicInteger(1);
    public static final AtomicInteger count2 = new AtomicInteger(1);
    public static final AtomicInteger count3 = new AtomicInteger(1);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_A)
    public void receiveA(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@A号 ] Received ==> '" + message + "'，Dealt with：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_B)
    public void receiveB(String message) throws InterruptedException {
        Thread.sleep(200);
        System.err.println(" [ 消费者@B号 ] Received ==> '" + message + "'，Dealt with：" + count2.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_B)
    public void receiveB2(String message) throws InterruptedException {
        Thread.sleep(500);
        System.err.println(" [ 消费者@B2号 ] Received ==> '" + message + "'，Dealt with：" + count3.getAndIncrement());
    }

    //----------------------------------------------------------------------------------

    @RabbitListener(queues = RabbitMQConfig.QUEUE_C)
    public void receiveC(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@C号 ] Received ==> '" + message + "'，Dealt with：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_D)
    public void receiveD(String message) throws InterruptedException {
        Thread.sleep(200);
        System.err.println(" [ 消费者@D号 ] Received ==> '" + message + "'，Dealt with：" + count2.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_D)
    public void receiveD2(String message) throws InterruptedException {
        Thread.sleep(500);
        System.err.println(" [ 消费者@D2号 ] Received ==> '" + message + "'，Dealt with：" + count3.getAndIncrement());
    }

    //-------------------------------------------------------------------------------------
    @RabbitListener(queues = RabbitMQConfig.QUEUE_E)
    public void receiveE(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@E号 ] Received ==> '" + message + "'，Dealt with：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_F)
    public void receiveF(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@F号 ] Received ==> '" + message + "'，Dealt with：" + count2.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_G)
    public void receiveG(String message) throws InterruptedException {
        Thread.sleep(200);
        System.out.println(" [ 消费者@G号 ] Received ==> '" + message + "'，Dealt with：" + count3.getAndIncrement());
    }

    //---------------------------------------------------------------------------------

    @RabbitListener(queues = RabbitMQConfig.QUEUE_X)
    public void receiveX(String message) {
        System.out.println(" [ 消费者@X号 ] Received ==> '" + message + "'，Dealt with：" + count1.getAndIncrement());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_Y)
    public void receiveY(String message) {
        System.out.println(" [ 消费者@Y号 ] Received ==> '" + message + "'，Dealt with：" + count1.getAndIncrement());
    }

}
