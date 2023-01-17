package com.yzm.channel.controller;

import com.rabbitmq.client.*;
import com.yzm.channel.config.RabbitConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/mq")
public class HelloController {

    @GetMapping("/simple/sender")
    public void sender() throws IOException, TimeoutException {
        //1、获取连接
        Connection connection = RabbitConfig.getConnection();
        //2、创建通道，使用通道才能完成消息相关的操作
        Channel channel = connection.createChannel();
        //3、声明队列
        channel.queueDeclare(RabbitConfig.HELLO_QUEUE, false, false, false, null);
        //4、发送消息
        for (int i = 1; i <= 3; i++) {
            String message = "Hello World!...... " + i;
            System.out.println(" [ Sent ] 消息内容 " + message);
            channel.basicPublish("", RabbitConfig.HELLO_QUEUE, null, message.getBytes());
        }
        //5、释放资源
        channel.close();
        connection.close();
    }

    @GetMapping("/simple/receiver")
    public void receiver() throws IOException {
        // 1、获取连接
        Connection connection = RabbitConfig.getConnection();
        // 2、创建通道
        Channel channel = connection.createChannel();

        // 声明接收到消息的回调
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                System.out.println("消息成功被接收------------------");
                System.out.println("消费者标识 = " + consumerTag);
                System.out.println("delivery = " + new String(delivery.getBody()));
            }
        };

        // 声明消息被取消的回调
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消息消费被中断----------------");
                System.out.println("消费者标识 = " + consumerTag);
            }
        };

        // 声明消费方法，当消费者接收到消息要执行的方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(" [ received ] 消息内容 : " + new String(body, StandardCharsets.UTF_8) + "!");
            }
        };

        // 3、消费消息
//        channel.basicConsume(RabbitConfig.HELLO_QUEUE, true, deliverCallback, cancelCallback);
        channel.basicConsume(RabbitConfig.HELLO_QUEUE, true, defaultConsumer);
    }
}
