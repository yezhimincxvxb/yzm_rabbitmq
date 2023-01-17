package com.yzm.tx.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
public class Sender {

    @Resource(name = "channel")
    private Channel channel;

    @GetMapping("/tx/{num}")
    public void tx(@PathVariable("num") int num) throws IOException {
        String message = "Hello world!";
        System.out.println(" [ 生产者 ] Sent ==> '" + message + "'");
        try {
            // 声明事务
            channel.txSelect();
            // 发送消息
            channel.basicPublish("tx.exchange", "tx.yzm", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            int i = 1 / num;
            log.info("提交事务");
            channel.txCommit();
        } catch (Exception e) {
            log.info("事务回滚");
            channel.txRollback();
        }
    }

}
