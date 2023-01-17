package com.yzm.confirm.receiver;

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
            value = @Queue(value = "confirm_queue"),
            exchange = @Exchange(value = "confirm.exchange", type = ExchangeTypes.FANOUT)
    ))
    public void confirm(Message message) {
        System.out.println(" [ 消费者@cf号 ] Received ==> '" + new String(message.getBody()) + "'");
    }

}
