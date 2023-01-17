# 1、简单模式(Simple)
```text
生产者生产消息发送给队列，消费者监听到队列有消息就进行消费(底层实现是生产者发送消息给默认交换机，再由交换机转发到队列)
一对一关系
```
# 2、工作模式(竞争模式)
```text
工作队列(又称任务队列)的主要思想是避免立即执行资源密集型任务，而不得不等待它完成。相反我们安排任务在之后执行。
我们把任务封装为消息并将其发送到队列。在后台运行的工作进程将弹出任务并最终执行作业。当有多个工作线程时，这些工作线程将一起处理这些任务。

一对多关系：队列以轮询的方式将消息分发给每个消费者
问题：每个消费者处理消息的时间不一致，可能导致消息堆积
解决：设置prefetch预取值功能，队列分发消息给消费者是通过channel的，prefetch可以设置channel最多有多少条消息
方式一：局部设置
@Bean(name = PREFETCH_ONE)
public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchOne(ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setPrefetchCount(1);
    return factory;
}
使用，需要的才设置
@RabbitListener(queues = RabbitConfig.HELLO_QUEUE, containerFactory = RabbitConfig.PREFETCH_ONE)
方式二：全局设置
spring:
  rabbitmq:
    port: 5672
    host: 192.168.192.128
    username: admin
    password: 1234
    listener:
      simple:
        prefetch: 1        
```
消息应答机制
```text
消费者完成一个任务可能需要一段时间，如果其中一个消费者处理一个长的任务并仅只完成了部分突然它挂掉了，会发生什么情况。
RabbitMQ 一旦向消费者传递了一条消息，便立即将该消息标记为删除。在这种情况下，突然有个消费者挂掉了，我们将丢失正在处理的消息。以及后续发送给该消费这的消息，因为它无法接收到。
为了保证消息在发送过程中不丢失，RabbitMQ 引入消息应答机制，消息应答就是:消费者在接收到消息并且处理该消息之后，
告诉RabbitMQ 它已经处理了，RabbitMQ 才可以把该消息删除了。
```
自动应答
```text
消息发送后立即被认为已经传送成功，这种模式需要在高吞吐量和数据传输安全性方面做权衡,因为这种模式如果消息在接收到之前，消费者那边出现连接或者 channel 关闭，
那么消息就丢失了,当然另一方面这种模式消费者那边可以传递过载的消息，没有对传递的消息数量进行限制，当然这样有可能使得消费者这边由于接收太多还来不及处理的消息，
导致这些消息的积压，最终使得内存耗尽，最终这些消费者线程被操作系统杀死，所以这种模式仅适用在消费者可以高效并以某种速率能够处理这些消息的情况下使用
```
手动应答
```text
spring.rabbitmq.listener.simple.acknowledge-mode=manual

用于肯定确认：basicAck(long deliveryTag, boolean multiple)
用于否定确认：basicNack(long deliveryTag, boolean multiple, boolean requeue)
           basicReject(long deliveryTag, boolean requeue)
           
deliveryTag：交付标签，相当于消息ID 64位的长整数(从1开始递增)
multiple：是否批量，false表示仅确认提供的交付标签；true表示批量确认所有消息(消息ID小于自身的ID)，包括提供的交付标签
requeue：是否重新入队，false表示直接丢弃消息，true表示重新排队

basicReject跟basicNack的区别就是始终只拒绝提供的交付标签
```
消息自动重新入队
```text
如果消费者由于某些原因失去连接(其通道已关闭，连接已关闭或 TCP 连接丢失)，导致消息未发送 ACK 确认，RabbitMQ 将了解到消息未完全处理，并将对其重新排队。
如果此时其他消费者可以处理，它将很快将其重新分发给另一个消费者。这样，即使某个消费者偶尔死亡，也可以确保不会丢失任何消息。
```
