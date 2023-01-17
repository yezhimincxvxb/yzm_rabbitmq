package com.yzm.priority.controller;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.yzm.priority.config.RabbitConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
public class HelloController {

    @Resource
    private Channel channel;

    @GetMapping("/sender")
    public void sender() throws IOException {
        // 声明队列
        Map<String, Object> map = new HashMap<>();
        // 优先级取值范围0-255，一般情况不需要设置过大
        map.put("x-max-priority", 10);
        channel.queueDeclare(RabbitConfig.PRI_QUEUE, true, false, false, map);

        // 发送消息
        for (int i = 0; i < 10; i++) {
            String message = "Hello World!...... " + i;
            System.out.println(" [ Sent ] 消息内容 " + message);
            if (i % 5 == 0) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", RabbitConfig.PRI_QUEUE, properties, message.getBytes());
            } else {
                channel.basicPublish("", RabbitConfig.PRI_QUEUE, null, message.getBytes());
            }
        }
    }

    @GetMapping("/receiver")
    public void receiver() throws IOException {

        // 声明消费方法，当消费者接收到消息要执行的方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(" [ received ] 消息内容 : " + new String(body, StandardCharsets.UTF_8) + "!");
            }
        };

        channel.basicConsume(RabbitConfig.PRI_QUEUE, true, defaultConsumer);
    }
}
