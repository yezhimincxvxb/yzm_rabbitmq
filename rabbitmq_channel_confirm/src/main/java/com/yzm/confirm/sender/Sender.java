package com.yzm.confirm.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
public class Sender {

    @Resource(name = "channel")
    private Channel channel;

    @GetMapping("/confirm")
    public void confirm() {
        try {
            // 开启confirm确认模式
            channel.confirmSelect();

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String message = "Hello waitForConfirms!..." + i;
                System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");

                // 发送消息
                channel.basicPublish("confirm.exchange", "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

                // 等待消息被确认
                if (channel.waitForConfirms()) {
                    System.out.println("生产者确认消费者收到消息");
                } else {
                    // 返回false可以进行补发。
                    System.out.println("消费者未收到消息，是否重发");
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("总耗时：" + (endTime - startTime) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // channel.waitForConfirms 可能返回超时异常
            // 可以进行补发。
        }

    }

    @GetMapping("/confirm2")
    public void confirm2() {
        try {
            // 开启confirm确认模式
            channel.confirmSelect();
            long startTime = System.currentTimeMillis();

            // 发送消息
            for (int i = 0; i < 1000; i++) {
                String message = "Hello waitForConfirmsOrDie!..." + i;
                System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");

                channel.basicPublish("confirm.exchange", "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

                // 阻塞线程，等待消息被确认。该方法可以指定一个等待时间。该方法无返回值，只能根据抛出的异常进行判断。
                if (i % 100 == 0) channel.waitForConfirmsOrDie();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("总耗时：" + (endTime - startTime) + "ms");
        } catch (InterruptedException e) {
            // 可以进行补发。
        } catch (IOException e) {
            //
        }
    }

    @GetMapping("/confirm3")
    public void confirm3() {
        try {
            // 开启confirm确认模式
            channel.confirmSelect();

            //异步监听确认和未确认的消息
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("未确认消息，标识：" + deliveryTag + "是否批量处理：" + multiple);
                }

                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("已确认消息，标识：" + deliveryTag + "是否批量处理：" + multiple);
                }
            });

            // 发送消息
            for (int i = 0; i < 1000; i++) {
                String message = "Hello addConfirmListener!..." + i;
                System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");

                channel.basicPublish("confirm.exchange", "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            }

        } catch (IOException e) {
            //
        }

    }
}
