# 简介
```text
RabbitMO 消息传递模型的核心思想是: 生产者生产的消息从不会直接发送到队列。实际上，通常生产者甚至都不知道这些消息传递传递到了哪些队列中。
相反，生产者只能将消息发送到交换机(exchange)，交换机工作的内容非常简单，一方面它接收来自生产者的消息，另一方面将它们推入队列。
交换机必须确切知道如何处理收到的消息。是应该把这些消息放到特定队列还是说把他们到许多队列中还是说应该丢弃它们。这就的由交换机的类型来决定。

生产者发送消息给交换机，由交换机转发消息给队列，交换机可以转发给所有绑定它的队列，也可以转发给符合路由规则的队列，
交换机本身不会存储消息，如果没有绑定任何队列，消息就会丢失

交换机相当于Map容器，路由对应key，Queue对应value，发送消息时，指定交换机中的key就能将消息加入到对应的队列中
```
# 分类
```text
发布订阅模型
发布订阅使用的交换机是Fanout交换机，也叫广播式交换机
广播式交换机：fanout交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列

路由模型
路由式交换机：direct交换器相对来说比较简单，匹配规则为：路由键完全匹配，消息就被投送到相关的队列

主题模型
主题式交换机：topic交换器采用模糊匹配路由键的原则进行转发消息到队列中

头部订阅
头部订阅(headers 交换机)：headers没有路由键，是根据消息头部header的键值对进行匹配，可以完全匹配也可以匹配任意一对键值对
```
# 发布订阅模型 Fanout
```text
Fanout 这种类型非常简单。正如从名称中猜到的那样，它是将接收到的所有消息广播到它知道的所有队列中。
```
# 路由模型 Direct
```text
路由式交换机：direct交换器相对来说比较简单，匹配规则为：路由键匹配，消息就被投送到相关的队列
```
# 主题模型(通配符模式) Topic
```text
topic交换器采用模糊匹配路由键的原则进行转发消息到队列中

两种特殊字符
*，只匹配一个单词
#，匹配零个或多个单词
```
# 头部订阅 Headers
```text
whereAll：表示完全匹配，满足所有条件
hereAny：表示只要有一对键值对能匹配就可以，满足任意一个条件即可
```