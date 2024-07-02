全网把Kafa概念讲的最透彻的文章，别无二家

消息队列老大哥Kafka在官网的介绍是这么说的，真是霸气：全球财富前100强公司有超过80%信任并使用Kafka。Kafaka目前在GitHub目前也已经有star数27.6k、fork数13.6k

大家好，我是南哥。

一个对Java程序员进阶成长颇有研究的人，今天我们开启新的一篇Java进阶指南，本期的对象是Kafka。

![star-history-202472](D:\code\z-mine\JavaGetOffer\Kafka专栏\Kafka生产者消费者\star-history-202472.png)

> Kafka历史Star趋势图

本文收录在我开源的《Java学习面试指南》中，一份覆盖Java程序员所需掌握核心知识、面试重点的Java指南，目前已经更新到近200道面试官必考的面试题。希望收到大家的 ⭐ Star ⭐支持。GitHub地址：<https://github.com/hdgaadd/JavaGetOffer>，相信你看了一定不会后悔。

## 1. Kafka概念

### 1.1 Kafka组成模块

> ***面试官：你先说说Kafka由什么模块组成？***

Kafka其实是一款基于**发布与订阅模式**的消息系统，如果按常理来设计，大家是不是把消息发送者的消息直接发送给消息消费者？但Kafka并不是这么设计的，Kafka消息的生产者会对消息进行分类，再发送给中间的消息服务系统，而消息消费者通过订阅某分类的消息去接受特定类型的消息。

其实这么设计的目的也是为了满足大量业务消息的接入，要是单一的消息发送和接收，那开个进程的消息通道就可以了。另外如果大家对设计模式的**发布/订阅模式**熟悉的话，对Kafka的设计理念会更容易理解。

总的来说，Kafka由五大模块组成，大家要理解好这些模块的功能作用：消息生产者、消息消费者、`Broker`、主题`Topic`、`Partition`。

（1）消息生产者

消息生产者是消息的创造者，每发送一条消息都会发送到特定的主题上去。

（2）消息消费者

消息生产者和消费者都是Kafka的客户端，消息消费者顾名思义作为消息的读取者、消费者。同时Kafka很灵活的一点是，一个消费者可以订阅多个主题，而且一个主题消息也可被不同消息分组的多个消费者处理。这就给我们变化多端的业务设计带来了众多可能性了，方便大家自由发挥。

（3）`Broker`

孤零零部署在Linux的Kafka服务器被称为`Broker`，也就是我上文提到的`中间的消息服务系统`，大家不要小瞧他，单台Broker可以轻松处理**每秒百万级**的消息量。Broker日常工作内容就是接收消息生产者的消息，为每条消息设置偏移量，最后提交到磁盘进行持久化保存。

（4）主题`Topic`

上文我们知道Kafka的消息是有分类的，而分类的标识就是主题`Topic`。大家可以看下具体代码落地会更容易理解，生消息生产者发送给`clock-topic`主题，消息消费者消费`clock-topic`主题下的消息。

```java
// 消息生产者
public class Producer implements ApplicationRunner {
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RBlockingQueue<Clock> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");

        while (true) {
            Clock clock = blockingFairQueue.take();
            kafkaTemplate.send("clock-topic", "key", clock.toString());
            log.info("time out: {} , clock created: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), clock.getTime());
        }
    }
}
```

```java
    // 消息消费者
    @KafkaListener(topics = "clock-topic", groupId = "kafka-group")
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("listener get message: " + record.value());
        ack.acknowledge();
    }

```

（5）分区`Partition`

每一个主题下的消息都需要提交到Broker的磁盘里，假如我们搭建了三个Broker节点组成的Kafka集群，一般情况下同一个主题下的消息会被分到三个分区进行存储。说到这，由于顺序发送的消息是存储在不同分区中，我们是无法保证消息被按**顺序消费**，只能保证**同一个分区**下的消息被顺序消费.

### 1.2 生产者异步回调

> ***面试官：消息生产者的异步回调，知道吧？***

当我们调用send()异步发送消息时，可以指定一个回调函数，该函数会等Broker服务器响应时触发。如下源码所示，我们可以为响应参数`ListenableFuture`添加一个回调函数实现`callback`。

```java
    public ListenableFuture<SendResult<K, V>> send(String topic, K key, @Nullable V data) {
        ProducerRecord<K, V> producerRecord = new ProducerRecord(topic, key, data);
        return this.doSend(producerRecord);
    }

    public interface ListenableFuture<T> extends Future<T> {
       void addCallback(ListenableFutureCallback<? super T> callback);
    }
```

那这个回调函数有什么作用？我们一般用来进行**异常日志的记录**。

Kafka的**异步提交消息**相比同步提交来说不需要在Broker响应前阻塞线程，这也在一定程度提高了消息的处理速度。但异步提交我们是不知道消息的消费情况的，此时就可以通过Kafka提供的回调函数来告知程序**异常情况**，从而方便程序进行日志记录。

### 1.3 分区

> ***面试官：那分区有什么作用？***

消费分区的作用主要就是为了提高Kafka处理消息的**吞吐量**，谁叫Kafka设计之初就是作为一款高吞吐量、高可用、可扩展的应用程序。

每一个topic会被**分为多个分区**。

假如同一个topic下有n个分区、n个消费者，**每个**分区会发送消息给对应的**一个**消费者，这样**n个消费者就可以负载均衡**地处理消息。

同时**生产者**会发送消息给不同分区，每个分区**分给不同的brocker**处理，让集群平坦压力，这样大大提高了Kafka的吞吐量。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/bb6d32040cfe437c9e2cf6f5dbf8e0ac.png#pic_center)

## 2. 消费者提交消息

### 两种提交消息的方式

> ***面试官：你说说消费者手动提交和自动提交，有什么区别？***

其实就是两种不同的客户端提交方式。

1. 自动提交的话，通过设置enable.auto.commit为true，**每过5秒**消费者客户端就会自动提交最大偏移量
2. 手动提交的话，通过设置enable.auto.commit为false，让消费者客户端消费**程序执行后**提交当前的偏移量

### 2.1 两种提交优缺点

> ***面试官：那它们都有什么优、缺点？***

1. 自动提交的话，比较方便**只需要配置**就可以，不过可能会导致消息丢失或重复消费。
   - 如果刚好到了5秒时**提交了**最大偏移量，此时正在消费中的消费者客户端崩溃了，就会导致**消息丢失**
   - 如果成功消费了，下一秒应该自动提交，但此时消费者客户端奔溃了**提交不了**，就会导致其他分区的消费者**重复消费**
2. 手动提交的话，需要**写程序**手动提交，要分两种提交方式。
   - 手动提交是**同步提交**的话，在broker对请求做出回应之前，客户端会一直阻塞，这样的话限制应用程序的**吞吐量**
   - 是**异步提交**的话，不会有吞吐量的问题。不过发送给broker偏移量之后，**不会管**broker有没有收到消息

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/8239593605f340a3bcb8fc2b3aee4503.png#pic_center)

