## 1. Kafka事务

### 1.1 Kafka事务是什么

> ***面试官：Kafka事务你说说看？***

Kafka的事务主要应用在以流式处理的应用程序中，流式处理？听起来都觉得很迷糊不知道是什么东西。

Kafka事务支持的流式处理过程一般是这样，A程序从一个A主题消费A消息，对A消息进行处理后，再把结果写入到B主题，后续B程序会对B主题的消息进行消费。也就是`消费 - 处理 - 生产`的过程。

这样的一个过程涉及了两个消息的消费、一个消息的生产，如何保证这整个过程的事务性，让这整个过程要么成功、要么都不成功，这就是Kafka事务要做的事情。

南哥画下流程图，帮助大家理解理解。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/fc55151379244961a3af129d5ea03574.png#pic_center)

### 1.2 重复消费问题

> ***面试官：你说的这个过程，不使用事务有什么问题？***

流式处理程序的`消费 - 处理 - 生产`过程，如果没有事务的保证，可能会出现多种消息重复消费的问题，这就会产生各种奇奇怪怪的问题了。

特别是在金融、支付行业，整个支付过程涉及了多个流程，例如用户下单 -> 库存校验 -> 订单处理-> 实际扣费 -> 清算结算，这些业务场景采用的便是流式处理程序。涉及资金的业务场景，事务的保障就更重要了！！

我说说两个消息重复消费的场景。

还是举例上文的场景：A程序从一个A主题消费A消息，对A消息进行处理后，再把结果写入到B主题，后续B程序会对B主题的消息进行消费。

（1）程序崩溃造成的重复消费

如果A程序对A消息进行处理后，把结果写入到B主题。但**在偏移量提交**的时候崩溃了，此时Kafka会认为A消息还没有被消费，而A程序崩溃了Kafka会把该分区分配给新的消费者。

问题就来了，新的消费者会重新消费A消息，等于B主题被写入了两条相同的消息，A消息被消费了两次。

（2）僵尸程序造成的重复消费

如果一个消费者程序认为自己没有死亡，但因为停止向Kafka发送心跳一段时间后，Kafka认为它已经死亡了，这种程序叫做僵尸程序。

A程序从Kafka读取A消息后，它暂时挂起了，失去和Kafka的连接也不能提交偏移量。此时Kafka认为其死亡了，会把A消费分配给新的消费者消费。

但后续A程序恢复后，会继续把A消息写入B主题，仍然造成了A消费被消费了两次。

**可能很多人会说**，这个流程有重复消费的问题，那处理重复消费的问题不就可以了，不必引入Kafka事务这么复杂。但在金融、支付这么严谨、重要的业务场景，我们要的是整个流程哪怕有一丁点出错，整个处理流程全都要进行回滚。

### 1.3 Kafka事务不能处理的问题

> ***面试官：Kafka事务有不能处理的问题吗？***

当然在整个Kafka事务的过程中，会有某些操作是不能回滚的，Kafka事务并不支持处理，我们来看看。

（1）Kafka事务过程加入外部逻辑

例如A程序消费消息A的过程中，发送了一个通知邮件，那整个外部操作是不可逆的，不在事务的处理范围内。

（2）读取Kafka消息后写入数据库

这其实也可以当成一个外部处理逻辑，数据库的事务并不在Kafka事务的处理范围内。

### 1.4 SpringBoot使用Kafka事务

> ***面试官：接触过SpringBoot发送Kafka事务消息吗？***

在SpringBoot项目我们可以轻松使用Kafka事务，通过以下Kafka事务的支持，我们就可以保证**消息的发送和偏移量的提交**具有事务性，从而避免上述的重复消费问题。

（1）先引入`spring-kafka`依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
        <version>xxx</version>
    </dependency>
</dependencies>
```

（2）配置Kafka事务管理器和生产者工厂

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-");
        
        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configProps);
        factory.setTransactionIdPrefix("tran-");
        return factory;
    }

    @Bean
    public KafkaTransactionManager<String, Object> transactionManager(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
```

（3）使用`KafkaTemplate`发送事务性消息

```java
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableKafka
@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "A")
    public void processMessage(String message) {
        // 处理从主题A接收到的消息
        String processedMessage = "Processed " + message;
        // 将处理后的消息发送到主题B
        kafkaTemplate.send("B", processedMessage);
        // 提交事务，确保消息发送和偏移量提交一起完成
    }
}
```