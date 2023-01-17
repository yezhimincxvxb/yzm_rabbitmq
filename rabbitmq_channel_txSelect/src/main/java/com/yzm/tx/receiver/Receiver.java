package com.yzm.tx.receiver;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tx_queue"),
            exchange = @Exchange(value = "tx.exchange", type = ExchangeTypes.DIRECT),
            key = {"tx.yzm"}
    ))
    public void tx(Message message) {
        System.out.println(" [ 消费者@tx号 ] Received ==> '" + new String(message.getBody()) + "'");
    }

}
