# JVM内存区域
## 1. JVM内存布局

### 1.1 堆内存

我们Java程序员相对C语言老哥来说，南友们不需要写内存管理这些东西。具体什么东西呢？不需要为每个对象去写繁琐的**释放内存**代码。

以下是一个C语言示例，C语言需要显式地使用`free`函数来释放内存。

```c
#include <stdio.h>
#include <stdlib.h>

int main() {
    // 分配内存以存储一个整数
    int *ptr = malloc(sizeof(int));
    if (ptr == NULL) {
        printf("内存分配失败\n");
        return 1;
    }
    // 使用分配的内存
    *ptr = 123;
    printf("存储的整数是: %d\n", *ptr);

    // 完成使用后释放内存
    free(ptr);
    return 0;
}
```

我们把重要的内存管理最高权力交给了JVM虚拟机，总得多多了解JVM虚拟机是如何处理内存管理的、包括JVM内存区域包含了什么，否则线上出了什么故障，不了解原理连解决的思路都没有。

JVM内存布局包含了五部分，分别是堆内存、本地方法栈、虚拟机栈、方法区、程序计数器。南哥画画图，给你加深理解。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/0e5456fa5a3e42c6a5b0fd57231aeb10.png#pic_center)

堆内存的作用很方便记忆，它的唯一目的就是存放对象实例。**成员变量**的变量值无论是基本类型、还是引用类型都存储在堆内存中，而**局部变量**的变量值如果是引用类型则存储在堆内存中。这点下文南哥会继续讲到。

```java
public class JavaSouth {

    // 成员变量：无论是基本类型、还是引用类型都存储在堆内存中
    private int memberInt = 10;
    // 成员变量：无论是基本类型、还是引用类型都存储在堆内存中
    private String memberString = "Hello, World!";

    public void displayInfo() {
        // 局部变量：如果是引用类型则存储在堆内存中
        String localString = new String("Local String");

        System.out.println("Member int: " + memberInt);
        System.out.println("Member String: " + memberString);
        System.out.println("Local String: " + localString);
    }
}
```

JVM的堆内存，在国内也被称为GC堆。说到GC回收，目前主流垃圾回收器都使用了分代收集算法，GC堆被分为了新生代、老年代。

新生代、老年代又使用了不同的垃圾回收算法，如新生代的对象特点就是存活时间短，更适合把内存一分为二的复制算法；而老年代的对象存活时间就相对较长了，各种大对象、小对象也比较复杂，可以使用标记清除算法、标记整理算法。这些南哥在JVM垃圾回收章节有提到过。

### 1.2 虚拟机栈

虚拟机栈是和Java中的**方法**相关的，因为每个方法在被一个线程执行时，都会去创建一个**栈帧**，因此虚拟机栈的生命周期也和线程相同，虚拟机栈也属于线程私有。

虚拟机栈的栈帧包含了这么些东西：局部变量表、操作数栈、动态链接、方法返回地址。难记吧？南哥是这么觉得。

在[Oracle官方文档](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6)中，我们可以了解到虚拟机栈一共会报出`StackOverflowError`、`OutOfMemoryError`两个异常。

> - If the computation in a thread requires a larger Java Virtual Machine stack than is permitted, the Java Virtual Machine throws a `StackOverflowError`.
> - If Java Virtual Machine stacks can be dynamically expanded, and expansion is attempted but insufficient memory can be made available to effect the expansion, or if insufficient memory can be made available to create the initial Java Virtual Machine stack for a new thread, the Java Virtual Machine throws an `OutOfMemoryError`.

翻译过来。

- 如果线程中的计算需要比允许值更大的 Java 虚拟机堆栈，则 Java 虚拟机将抛出`StackOverflowError`，也就是堆栈溢出。
- 如果 Java 虚拟机堆栈可以动态扩展，并且尝试扩展但没有足够的内存来实现扩展，或者没有足够的内存来为新线程创建新的初始 Java 虚拟机堆栈，则 Java 虚拟机将抛出`OutOfMemoryError`，也就是内存溢出。

### 1.3 本地方法栈

本地方法栈和虚拟机栈的作用相差不大，都是为方法的运行提供一个栈帧。众所周知Java很多关于数学计算、系统调用等操作，都利用了C语言的本地方法，这些本地方法也叫Native方法。

南哥给一段由C语言实现的Native方法代码。

下面是String类的intern方法，该方法使用的便是本地方法。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /**
     * 返回字符串对象的规范表示。
     */
    public native String intern();
}
```



### 1.4 方法区

上文跟着南哥我们知道虚拟机栈、本地方法栈提供栈帧，而堆内存提供内存区域。其实方法区也起到提供一个内存区域的作用，方法区存放了类相关的数据：类结构信息、常量、静态变量等。

在[Oracle官方文档](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6)中，我们可以知道方法区会出现`OutOfMemoryError`异常。

> If memory in the method area cannot be made available to satisfy an allocation request, the Java Virtual Machine throws an `OutOfMemoryError`.

如果方法区中的内存不足以满足分配请求，则 Java 虚拟机将抛出`OutOfMemoryError`。

### 1.5 程序计数器

程序计数器的主要作用是存储指向**当前线程**正在执行的JVM指令的地址。

而程序计数器在整个JVM内存布局中，是唯一一个不会出现`OutOfMemoryError`的区域。



### 1.6 变量存储位置

南哥在上文有提到堆内存、方法区具体存放了什么内容，现在我们整理整理Java各种变量的变量名、变量值所存储的位置。

这一点，面试官考得细的话会考到。

1. **成员变量**
   - 变量名作为类的一部分，其结构定义存储在**方法区**。
   - 而变量值无论是基本数据类型还是引用类型，都是存储在**堆内存**中的对象实例内。
2. **类变量**
   - 变量名作为类的一部分，其结构定义也存储在**方法区**。
   - 变量值无论是基本数据类型还是引用类型，都存储在**方法区**中，因为它们属于类级别的数据。
3. **局部变量**
   - 局部变量是存在于方法中的变量，变量名存储在虚拟机栈的**栈帧**中。
   - 而变量值如果是基本数据类型，存储在虚拟机栈的栈帧中；如果是引用类型，变量值存储在**栈**中，但引用所指向的对象本身存储在**堆内存**中。
# Kafka事务一般在什么场景下使用呢
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
# MyBatis面试必问： Mybatis一、二级缓存及其优缺点
## 1. Mybatis概要

### 1.1 Mybatis理解

> ***面试官：你说下对MyBatis的理解？***

如果没有MyBatis的支持，大家是怎么实现通过程序控制数据库的？首先我们需要为程序引入MySQL连接依赖`mysql-connector.jar`，加载数据库JDBC驱动，接着创建数据库连接对象`Connection`、SQL语句执行器`Statement`，再把SQL语句发送到MySQL执行，最后关闭SQL语句执行器和数据库连接对象。

整个过程是比较**繁琐**的，这是通过JDBC操作MySQL必走的过程。可实际开发可给不了你那么多时间，如果大家非要用JDBC去写大量的冗余代码也可以，能抗住催你开发进度的压力就行。

这是JDBC操作的过程。

```java
public class JDBCController {
    private static final String db_url = "jdbc:mysql://localhost:3306/db_user";
    private static final String user = "root";
    private static final String password = "root";
    
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        String sql = "select * from user order by id desc";
        try {
            connection = DriverManager.getConnection(db_url, user, password);
            statement = connection.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
```

MyBatis能帮助我们什么？早在2002年，MyBatis的前身iBatis诞生，并于2010年改名为MyBatis。该框架引入了**SQL映射**作为持久层开发的一种方法，也就是说我们不需要把SQL耦合在代码里，只需要把SQL语句单独写在XML配置文件中。

以下是MyBatis编写SQL的写法。SQL的编写已经和程序运行分离开，消除了大量JDBC冗余代码，同时MyBatis还能和Spring框架集成。整个SQL编写的流程变得更加灵活也更加**规范化**。

```java
@Mapper
public interface UserMapper extends BatchMapper<UserDO> {
    List<UserDO> selectAllUser();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.JavaGetOffer.UserDO">

    <select id="selectAllUser" resultType="org.JavaGetOffer.UserDO">
        select * from user order by id desc
    </select>
    
</mapper>
```

### 1.2 SqlSession是什么

> ***面试官：那SqlSession知道吧？***

从我们偷偷访问某个小网站开始，到我们不耐烦地关闭浏览器或者退出登录时，我们作为用户和网站的一次会话就结束了。MyBaits框架要访问数据库同样要与数据库建立通信桥梁，而SqlSession对象表示的就是MyBaits框架与数据库建立的**会话**。

我们可以利用SqlSession来操作数据库，如下代码。

```java
    @Test
    public void testMybatis() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<UserDO> userList = userMapper.listAllUser();
        System.out.println(JSON.toJSONString(userList));
    }
```

## 2. Mybatis缓存

### 2.1 Mybatis缓存分类

> ***面试官：Mybatis的缓存有哪几种？***

软件系统合理使用缓存有一个好处。有了缓存，在原始数据没有更新的情况下，我们不需要重新再去获取一遍数据，这也减少了数据库IO，达到提升数据库性能的目的。

MyBatis同样提供了两个级别的缓存，一级缓存是基于上文提到的SqlSession实现，二级缓存是基于Mapper实现。

一级缓存作用在同一个SqlSession对象中，当SqlSession对象失效则一级缓存也跟着失效。我们梳理下一级缓存的**生命周期**。首先第一次查询时会把查询结果写入SqlSession缓存，如果第二次查询时原始数据没有改变则会读取缓存，但如果是修改、删除、添加语句的执行，那SqlSession缓存会被全部清空掉，这也是为了防止**脏读**的出现。

一级缓存缓存底层使用的是一个简单的Map数据结构来存储缓存，其中key为`SQL + 参数`、val为`查询结果集`。一级缓存的生命周期如下。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7ffbd58ab80d460a877dd0cba43de01b.png#pic_center)


二级缓存的作用域是同一个命名空间**namespace**的Mapper对象，也就是说同一个Mapper下的多个SqlSession是可以共用二级缓存的。二级缓存的缓存写入、清空流程和一级缓存相似，但二级缓存的生命周期是和**应用程序的生命周期**一致的。为什么？因为Mybatis框架与Spring IOC集成的Mapper对象是单例对象。

另外大家还需要注意下，Mybatis的一级缓存是默认开启的且**不能关闭**，而二级缓存则需要我们手动开启，我们需要在配置文件中配置`cacheEnabled`参数。

```xml
<configuration>
  <settings>
    <setting name="cacheEnabled" value="true"/>
  </settings>
```

### 2.2 Mybatis缓存局限性

> ***面试官：那Mybatis缓存有什么问题吗？***

缓存是好，就是问题有点多，目前大厂大都禁止了Mybatis缓存的使用。

南哥总结了下，主要有以下原因。

（1）适用场景少

Mybatis二级缓存更适用于读多写少的业务场景，但是对于**细粒度**的缓存支持并不友好。举个用烂了的商城例子，每个商品信息的更新是非常**频繁**的的，而让用户每次都看到的是最新的商品信息又非常重要。

在同一个namespace的Mapper中一般会包含多个商品信息的二级缓存，只要有某一个商品信息更新了，则所有商品缓存都会全部失效。那其实在这个业务场景中，二级缓存的存在已经没有多大必要了，还反而增加了系统复杂性。

（2）数据不一致性问题

如果多个不同namespace的Mapper都共同操作同一个数据库表的情况下，第一个Mapper更新了数据库表会清空它本身的二级缓存，但其他namespace的Mapper是没有感知的，仍然缓存的是旧数据，数据不一致的问题就出现了。

（3）不适用于分布式系统

现在还用单机部署的业务已经不多了，大家都紧跟潮流搭了个分布式、高可用的系统。在分布式系统中，如果每个节点都使用自己的本地缓存，假如现在节点A更新了缓存，但节点B、节点C是不会进行同步更新的，同样产生了数据不一致的问题。

## 3. Mybatis分页插件

> ***面试官：Mybatis分页插件是怎么实现的？***

Mybatis分页的原理其实很简单，没有想象的那么复杂。我们只需要拦截SQL查询语句，再把SQL语句作为子查询，外面包裹一层`SELECT * FROM`后再加上`LIMIT`的分页约束语句。

如下SQL示例，确实挺简单的。

```sql
SELECT * FROM user
SELECT u.* FROM (SELECT * FROM user) u LIMIT M, N
```
# MySQL面试必问：MySQL事务四大特性、事务隔离级别
## 1. 事务的特性

MySQL事务有四大特性。

1. 原子性(atomicity)：一个事务必须是一个不可分割的**最小工作单元**，整个事务所有的操作，要么成功提交，要么都失败回滚。
2. 一致性(consistency)：事务总是从一个一致性状态转换为另一个一致性状态。
3. 隔离性(isolation)：一个事务所作出的修改在还没有提交之前，对其他事务来说是不可见的。
4. 持久性(durability)：如果事务进行提交后，其所做的修改必须是永久性的，不会因为系统崩溃而丢失修改。

## 2. 事务隔离级别

SQL标准定义了四种隔离级别，较低级别的隔离通常来说系统开销更低些。

1. READ UNCOMMITTED（未提交读）：事务的修改，即使没有提交，对其他事务来说也是可见的。这是最低级别的事务隔离，企业生产中很少使用到。
2. READ COMMITTED（提交读）：事务在未提交前，所做的修改对其他事务是不可见的。这个隔离级别也称为不可重复读，主要是因为两次重复的数据读取，可能会产生两种完全不同的结果。
3. REPEATABLE READ（可重复读）：这个事务隔离级别保证了一个事务多次读取都是同样的结果，能够解决前面两个隔离级别可能产生的不可重复读问题。另外可重复读是`MySQL`默认的事务隔离级别。
4. SERIALIZABLE（可串行化）：该隔离级别会**强制事务串行执行**，同时对读取的每一行数据都加上锁，来。通过这种方式可以解决**幻读**的事务问题，不过可能导致锁竞争问题和大量的`SQL`超时。

### 2.1 幻读

并发事务带来的问题主要有四种，可以用上面我们谈到的事务隔离级别来处理。

1. 脏读：一个事务读取到另一个事务未提交的数据。

2. 不可重复读：一个事务多次读取同一数据，另一个事务**修改了**该数据，导致第一个事务第二次读取数据发现和第一次读取的**数据不一致**。

3. 幻读：一个事务多次读取同一数据，另一个事务给这些数据**插入删除了某些内容**，导致第一个事务数据的**数量**发生改变。

4. 丢失修改：一个事务修改了某个数据，另一个事务与其读取同一数据且原始值都相同，另一个事务修改数据后提交，导致第一个事务的修改操作丢失。

### 2.2 处理幻读问题

幻读可以采用我提到的`SERIALIZABLE`（可串行化）隔离级别来解决幻读，事务按顺序执行，也就不会有幻读问题。

MySQL也提供了其他方法来处理幻读问题。

1. 设置间隙锁，在两个索引值之间的数据进行加锁，可以杜绝其他事务在这个范围内对数据数量的影响。

2. next-key锁就是间隙锁和行锁的**组合**，通过间隙锁锁住**区间值**、行锁锁住**行本身**。

### 2.3 死锁问题

死锁是因为多个事务互相占用对方请求的资源导致的现象，要打破这个问题需要回滚其中一个事务，这样另一个事务就能获得请求资源了，而回滚的事务只需要重新执行即可。

InnoDB引擎目前处理死锁的方法是通过持有行级排他锁的数量来判断，**持有最少行级排他锁**的事务会进行回滚。

### 2.4 隔离级别相关命令

MySQL默认隔离级别是可重复读，企业生产一般也是用的这个隔离级别。

查看隔离级别的指令：

```sql
select @@tx_isolation
```

设置隔离级别为可重复读的指令：

```sql
set session transaction isolation level repeatable read
```
# MySQL面试必问：SQL如何优化、索引如何设计
## 1. 慢查询

> ***面试官：知道MySQL慢查询吗？***

MySQL的慢查询日志可以记录**执行时间超过阈值**的SQL查询语句，所以我们可以利用该日志查找出哪些SQL语句执行效率差，从而对SQL语句进行优化。

MySQL5.7以上版本可以通过SET命令来开启慢查询日志。

```mysql
     SET GLOBAL slow_query_log=ON;
     SET GLOBAL long_query_time=2;
     SET SESSION long_query_time=2;
```

开启完慢查询日志，我们找到该日志的位置，打开文件即可查询慢查询的SQL。

```mysql
     ＃　查询慢查询日志文件位置
     SHOW VARIABLES LIKE '%slow_query_log_file%';
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a44fc1d0c2d7d6077689563284f8ccf1.png)


打开DESKTOP-ALU4BOC-slow.log文件，找到慢查询SQL为：`select sleep(11)`。

```xml
D:\MySQL\bin\mysqld, Version: 5.5.40 (MySQL Community Server (GPL)). started with:
TCP Port: 3306, Named Pipe: MySQL
Time                 Id Command    Argument
# Time: 220828 21:40:28
# User@Host: root[root] @ localhost [127.0.0.1]
# Query_time: 11.004454  Lock_time: 0.000000 Rows_sent: 1  Rows_examined: 0
use mysql;
SET timestamp=1661694028;
select sleep(11);
```

## 2. SQL优化

### 2.1 表设计优化

> ***面试官：在工作中你怎么优化SQL的？***

业务开发中涉及数据库的第一步是表设计，要优化SQL就要从第一步开始做起。

MySQL表设计要尽可能满足数据库三大范式，帮助大家回顾下：

1. 第一范式：数据库表中的每一列都是不可再分的属性，属性相近或相同的列应该**合并**。

2. 第二范式：满足第一范式的条件下，一个表只能描述一个对象。如果某些列经常出现数据重复，应该把这些列作为**另一个表**。

3. 第三范式：满足第二范式的条件下，表中的每一列都只能依赖于**主键**，即直接与主键相关。

我们在业务开发中遇到**反第二范式**的情况是最多的，例如以下订单明细表的设计，每一个订单明细都包含了**重复**的商品名称、商品单位、商品价格，这三个字段属于字段冗余存储。如果表的数据量级很大，那造成的冗余存储量是可想而知的，而且最要命的问题是如果要修改某一个商品名称，那所有的订单明细数据**都要修改**。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/248af310b00226526ad386b104b61d29.png)

我们可以遵循第三范式，把冗余的字段抽出一个新的商品表，当要查询订单明细时只需要把两表通过商品id进行连接即可。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/12daddf87ec5da36859fea06d658314b.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6b8d792d3395ebe87e4886ec1a927ad5.png)

> ***面试官：遵循第二范式就一定最优？***

遵循第二范式的表设计不一定是最优的情况，还是那句话，要根据实际的业务场景权衡利弊。

虽然把冗余数据抽离出去了，但却增加了表的数量，也意味着查询数据时表之间的**join连接操作**也会变多。而join连接的性能是比较低的，有可能join操作会成为数据库性能的瓶颈。

### 2.2 SQL语句优化

> ***面试官：还有呢？***

SQL优化除了做好表设计的优化工作，还需要对SQL语句进行优化。而SQL查询语句的优化主要从**覆盖索引**、**避免索引失效**、**减少不必要的查询**三个方面入手。

一、从覆盖索引的角度。

**order by排序**的字段要尽量覆盖索引。如果使用**非索引字段**进行排序，MySQL会进行额外的**文件排序**，将查询结果根据非索引列在**磁盘**中再排序一次。当我们使用explain关键字分析SQL时会发现Extra会出现`Using filesort`。

**group by分组**要尽量覆盖索引。如果使用**非索引字段**进行分组，MySQL只能进行全表扫描后建立临时表才能得出分组结果。

另外我们可以使用explain关键字来分析SQL语句的效率，查看SQL语句是否覆盖索引。

二、从避免索引失效的角度。

关于如何避免索引失效，大家可以阅读我出版的《JavaGetOffer》专栏关于【MySQL索引】的文章。

三、从减少不必要的查询的角度。

如果只需要查询部分列，尽量不要使用`select *`查询，防止造成不必要的资源消耗、占用过多的网络带宽。

### 2.3 索引如何设计

> ***面试官：在工作中，表索引你怎么设计的？***

索引的设计有以下设计原则，大家在实际业务开发中应该尽量遵循这些原则，可以帮你避开不少坑。

1. 经常进行order by排序、group by分组、join多表联结查询的字段应该建立索引。

2. 经常在where子句中出现的字段应该建立索引。

3. 尽量使用数据量小的字段建立索引。例如对于char(500)和char(10)两个字段类型来说，肯定是以后者进行索引匹配的速度更快。

4. 如果需要建立索引的字段值比较长，可以使用值的部分前缀来建立索引。

   例如varchar类型的name字段，我们需要根据前三个字符来建立前缀索引，可以使用以下SQL命令：`ALTER TABLE example_table ADD INDEX index_name (name(3))`。

> ***面试官：那索引建立越多，查询效率就越高吗？***

另外大家记住一点，索引不是建立越多越好。合理设计的索引确实能大大提高SQL效率，但每建立一个字段索引，MySQL就要为该索引多维护一棵`B-Tree`，越多的索引会造成**表更新**效率变得低下。
# MySQL面试必问：索引的类型、Explain分析SQL、索引失效
## 1. 索引类型

> ***面试官：索引有什么用？***

大家可以把你最近最爱的一本书类比成一个MySQL数据库，你要快速翻到你昨天看到的精彩部分，是不是要先看下书的**目录索引**，要翻到第几章、第几页。

数据库最主要的就是数据存储，其次就是提供复杂查询服务，而索引就是MySQL作为快速找到记录的一种数据结构。索引类型有多种，像常见的B树索引、哈希索引，这些都需要我们去掌握。

不要和我说你看书都用书签，或者靠手感就能翻出来昨天看到的地方。

我们对比下不采用索引和采用索引的差异。

目前我本机数据库的article表有10w条数据，表结构如下。

```sql
CREATE TABLE `article`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `author_id` int(10) NULL DEFAULT NULL,
  `category_id` int(10) NOT NULL DEFAULT 0,
  `views` int(10) NULL DEFAULT NULL,
  `comments` int(10) NULL DEFAULT NULL,
  `title` varbinary(255) NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`, `category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
```

没建立索引前，使用explain关键字分析查询SQL。type显示`ALL`，也就是该SQL执行时对MySQL进行的是全表扫描。

```sql
explain select id from article where category_id = 1 order by views desc;
```

```sql
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra                       |
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
|  1 | SIMPLE      | article | ALL  | NULL          | NULL | NULL    | NULL | 102279 | Using where; Using filesort |
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
```

建立索引后。

```sql
create index idx_ca_vi on article(category_id,views);
```

type显示为`ref`，同时Extra列显示`Using where; Using index`，`Using index`代表该SQL执行时使用了索引，而`Using where`代表了在MySQL服务端再进行了一次`views`字段的排序。

```sql
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
| id | select_type | table   | type | possible_keys | key       | key_len | ref   | rows | Extra       |
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
|  1 | SIMPLE      | article | ref  | idx_ca_vi     | idx_ca_vi | 4       | const |    51139 | Using where; Using index |
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
```

### 1.1 B-Tree索引

> ***面试官：B树索引说一下？***

在杂乱无章的一堆数字里，我要你快速找到唯一的一个数字66，大家要怎么做？

两种选择，你在一堆数字里一个个地找，就如MySQL**全表扫描**。或者把所有数都按大小顺序进行排列，找到第66个位置的数字。

我们假设建立的是主键索引，MySQL索引会根据主键id建立起一棵B-Tree。B-Tree类似于二叉搜索树，同样具有快速查找特定值的功能。

（1）但在**结构方面**，B-Tree又不同于二叉搜索树，它是**多子树的**。即每一个节点可以有两棵以上的子树。

（2）在**值的存储方面**，B-Tree所有的值都存储在叶子节点。并且每一个叶子节点可以存储多个元素，这一点也与二叉搜索树不同。两个人想要去湖里打水，一个人拿着手大的碗，一个人拿着一个水桶，拿水桶的不会比拿碗的装的少。每个叶子节点存储的元素多，每次磁盘访问就可以获得更多的数据，从而减少查询的I/O操作。

面试官经常会问你这个问题，叶子节点是什么数据结构？。实际上叶子节点之间用指针链接形成了一串**双向链表**。这个留到下文解释。

（3）另外大家很容易漏掉一个重要的知识点。如果是二级索引建立的B-Tree，每个叶子节点的值保存的是**对应行数据的主键**。那一级索引叶子节点保存什么呢？一级索引也就是主键索引，下文我会告诉大家。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/639f2a7ec6a043acb535119001e74a1e.png)

### 1.2 B-Tree值的存储

> ***面试官：你说值都存储在叶子节点，那有什么好处？***

数据库数据都存储在叶子节点，会使得非叶子节点**层数更少**。从外表来看，很明显整棵B-Tree的层数变少，B-Tree**高度变得矮胖**。

B-Tree变得矮胖有什么作用？举个爬楼梯的例子，B-Tee的每一层级就像一层楼。相信大家租房都不想租高楼，每次回去都要爬那么多层楼梯，膝盖怎么受得了呢。

B-Tree每一层的搜索可能就代表了一次磁盘I/O操作，B-Tree的层数变少意味着I/O读取的次数就变少，查询的效率也会因此提高。

另外企业业务在查询上更多的是**范围查询**，你对网页的每一次翻页操作都是对MySQL数据的一次范围查询。B-Tree的元素都存储叶子节点，同时形成双向链表结构，很适合范围查询这种复杂查询操作。

### 1.3 哈希索引

> ***面试官：知道为什么主流数据库引擎不采用哈希索引吗？***

企业业务上一般都是**范围查询**，而哈希索引由于其底层数据结构，不能够支持任何范围查询。这也难怪主流数据库引擎不青睐它。

但其实哈希索引也有它的闪光灯，哈希索引会为所有的索引列计算一个哈希码。同时在哈希表中保存哈希码和指向每个数据行的指针，这种结构对**精确匹配查询**的效率极高。

MEMORY数据库引擎底层采用的就是哈希索引。

### 1.4 聚簇索引

> ***面试官：聚簇索引和二级索引有什么关联？***

读到这里，我回答下上文还没回答大家的问题。

首先，聚簇索引和主键索引是等同的，也有一个一般都不提的名称：一级索引。

而B-Tree的**二级索引指的是非主键索引**，它的叶子节点保存的只是**行的主键值**，所以需要另外通过主键来找到行数据。

聚簇索引通过**主键来建树**，它的叶子节点包含了**行的全部数据**。

这就把两者相关联起来了，**通过二级索引查找行**，需要先在二级索引**建立**的B-Tree上找到主键的值，接着再从聚簇索引建立的B-Tree找到行数据。

## 2. 索引效率

### 2.1 Explain关键字

> ***面试官：那我一条SQL，我怎么知道它有没使用到索引？***

检查是否使用索引可以利用**Explain关键字**来分析，它会模拟执行sql语句，查询出sql语句**执行的相关信息**，如哪些索引可以被命中、哪些索引实际被命中。

我说下Explain查询结果的几个关键字段。

- **type**

  - cost：通过索引**一次**查询
  - ref：使用到索引
  - range: 使用到索引
  - all：**全表扫描**

- **Extra**

  - using filesort：使用外部文件排序，发生在无法使用索引的情况下

  - using index：**where查询**的列**被**索引覆盖，直接通过索引就可以查询到数据

  - using where：**where查询**的列，没有**全部被**索引覆盖

  - using join buffer：使用了连接缓存

- **possible_key**

  表示可以使用的索引

- **key**

  表示实际使用的索引

如果简历你写了`精通MySQL`，那问的可就没这么简单。我可以问你在工作中紧急处理了哪些数据库重大事故，优化了哪些业务慢SQL、是怎么优化的、为什么这么做。

### 2.2 索引失效

> ***面试官：有没索引失效的情况呢？***

索引失效一般是这个SQL查询破坏了**使用B-Tree查询**的条件。也有一种可能出现，如果表数据膨胀得太快，即使建立索引你查询起来也会有索引失效的错觉，这个问题就要另外讨论了。

1. 如果在where子句中使用not in、!=和＜＞操作，会使索引失效而导致进行全表扫描。

2. 对索引列进行**数学函数**处理的话，索引会失效。

3. 索引是字符串类型，查询值没有添加**单引号**''那索引会失效。因为值类型与索引列类型。不一致，MySQL**不会使用**索引，而是把索引列数据进行**类型转换**后进行查询。

4. 对索引列进行模糊查询，%要放在**最右侧**，否则索引会失效。`SELECT * FROM user WHERE name LIKE n%`

5. 在组合索引中，如果前一个索引使用**范围查询**，后面的索引也会失效。

大家在实际工作切忌乱加索引，此`切忌`非`切记`。每加一次索引，MySQL都要多去维护一棵新的B-Tree。增加太多索引，数据查询效率会变得低下。
# MySQL高阶知识：主从复制步骤、三种二进制日志格式等
## 1. MySQL主从复制

> ***面试官：MySQL主从复制了解吧？***

回答这个问题前，大家先思考下MySQL主从复制起到了什么作用。知道技术诞生的缘由，技术原理和步骤的整个逻辑推导就很清晰。

MySQL主从复制把数据库数据同步到多台服务器上，同理就可以把**读操作**分布到多台服务器上，这对于那些读密集型的系统性能提升是很大的。

数据有了多台服务器的备份，就不怕单点故障。我们只需要快速切换到另一台MySQL服务器即可，减少了数据库宕机的时间。

MySQL主从复制主要是利用了主库的**Binary Log**二进制文件来进行数据**复制**。

复制的步骤可以分为三步。

1. 主库根据**数据库事务提交的顺序**，把数据更改记录到二进制文件Binary Log中。
2. 备库建立TCP/IP连接后通过IO线程获取Binary Log，同时将Binary Log复制到中继日志**Relay Log。**
3. 备库再读取中继日志Realy Log中的事件，重放到备库的数据里。

如果你现在有两台MySQL，一台版本是03年的MySQL5.0，另一台是18年的MySQL8.0.11。新版本可以作为老版本的从服务器，但反过来是不可行的。MySQL的复制具有**向后兼容性**，老版本可能无法解析新版本的新特性，甚至复制的文件格式都差异太大。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/a17801d54fb9409bbfd00e7010545ebe.png#pic_center)

### 1.1 二进制文件的日志格式

> ***面试官：那Binary Log日志格式知道有哪些？***

MySQL提供了三种二进制日志格式用于主从复制。

1. **基于语句**的二进制文件，保存了在MySQL主库所有执行过的**数据变更语句**，相当于从库需要把主库执行过的SQL都执行一遍。
2. **基于行**的二进制文件，会把每条被改变的行记录都作为事件写入到二进制文件中。缺点也很明显，行记录的事件是很多的，可能会导致二进制文件大小过大。这种复制模式通常来说让**带宽压力**更大些。
3. 混合模式。MySQL能够在以上两种复制模式之间动态切换，默认会使用基于语句的复制方式，如果发现无法被正确复制，就切换成基于行的复制方式。

### 1.2 二进制文件选择

> ***面试官：知道哪种二进制格式比较好吗？***

**基于语句**的二进制文件，有可能会出现**数据不一致**的问题。例如某条删除语句SQL要删除10000条数据中的1000条，这条删除语句没有采用**order by**进行排序。如果主、从服务器存储数据的顺序不一样，就会导致每次执行删除的数据都是不同的。

```sql
# 没有排序的删除语句
delete from test where name = 'JavaGetOffer' limit 1000;
```

```sql
# 有排序的删除语句
delete from test where name = 'JavaGetOffer' order by id asc limit 1000;
```

混合模式的话不确定因素太多，两种复制模式的不断切换可能回导致二进制日志出现**不可预测**的事件。如果从服务器复制该二进制文件后的数据库状态是混乱无序的，那整个复制的过程就没有意义了。

一般来说选择**行的复制**会更加稳妥，也更加安全。虽然二进制文件过大会带来带宽压力大和复制较慢的问题，但比起数据安全性来说，显然后者更加重要。

### 1.3 主从模式的优点

> ***面试官：那MySQL主从模式有什么好处？***

大家如果有细看第一个`面试官问题`就知道上文已经有答案了，我这里再总结下。

1. 对于读密集的应用程序，可以利用MySQL主从模式将**读操作负载均衡**到多个从服务器上，提高系统的抗压能力。
2. MySQL主从还可以**避免单点问题**，有效减少数据库宕机的时间。同时多个数据源支持查询保证了数据库的**可用性**。
3. 另外如果需要对MySQL进行**版本升级**，可以先对备库进步版本升级，保证查询可用的同时，再慢慢升级其他全部MySQL实例。

## 2. 全局事务标识符

> ***面试官：如果把二进制文件丢给从库，从库是不是复制整个文件？***

能设计出MySQL的聪明人肯定不会这么傻。如果二进制文件包含了已存在的数据，就会造成数据重复了。

MySQL从库只会复制它本身缺失的最新数据，利用二进制文件里的**全局事务标识符(GTID)**就可以找到对应的二进制文件**具体位置**。

主库的每一次事务提交都会被分配一个唯一的全局事务标识符，这个标识由server_uuid和一个**递增**的事务编号组成。

MySQL从库是根据本身当前**全局事务标识符**找到二进制文件对应位置才进行复制而不是复制全部。
# Nginx
## 1. Nginx必知必会

### 1.1 Nginx概要

> ***面试官：说说你对Nginx的理解？***

一款**Web服务器**，它叫Nginx，碾压了Apache、Microsoft IIS、Tomact、Lighttpd等一众Web服务器。我们国内没有部署Nginx的科技业务公司，相信也没有多少。

为什么呢？南哥认为和Nginx的出身有关！Nginx在2002年立项开发就是为了服务俄罗斯访问量位居首位的Rambler.ru站点。另外最重要一点，免费开源！让Nginx集结了全球的智慧，帮助它升级迭代、不断攀登宝座。

在Java后端的每一个SpringBoot项目都集成了一个Tomcat服务器，那和Nginx有何区别？其实两者实际上都是提供互联网交互能力的一个节点，同样是Web服务器，不过主要的功能不同。

Tomcat服务器设计小巧轻量，没有集成处理复杂业务场景的功能，更适合作为一个API Web服务器。Nginx提供的功能就很多了，像反向代理、负载均衡、Web缓存，我们企业面向用户的第一关卡便是Nginx，后面的链条才轮到微服务节点。下面我一一道来。

### 1.2 Nginx反向代理、负载均衡

> ***面试官：Nginx常用功能知道吧？***

（1）反向代理

认识Nginx就从它的反向代理功能开始，Nginx可以配置这样的映射关系。

```xml
server {
    listen       9001;
    server_name  localhost;

    location ~ /server01/ {
            proxy_pass   http://localhost:8001;
	}
    location ~ /server02/ {
            proxy_pass   http://localhost:8002;
	}
}
```

以上配置代表了**所有包含**`/server01/`的路径，实际指向的是后台端口为：`http://localhost:8001`。

举个栗子，用户访问浏览器，这代表了用户肉眼可见的`url链接`实际映射到企业内部服务器是哪个地址、哪些微服务节点处理这个url链条的请求等。

当然Nginx的反向代理功能不止上面说的基础功能，Nginx**转发策略**也是它的本事。我们可以设置代理的正则表达式，把一定规则的域名都转发到某一个端口。

```xml
server {
    listen 80;

    server_name example.com;

    location ~ ^/api/ {
        proxy_pass http://api.example.com;
    }
}
```

例如以上Nginx配置，南哥使用了正则表达式 `^/api/` ，严格匹配所有以 `/api/` **开头**的URL路径，我们把这些请求转发到 `http://api.example.com`。

（2）负载均衡

后台一众的微服务节点，前面我们知道了Nginx负责代理转发的功能，那Nginx就少不了支持负载均衡。

例如6个微服务节点，1秒内1万个用户请求过来，Nginx这台Web服务器要如何负载均衡把哪些请求转发到哪些个微服务节点。

Nginx服务器提供的负载均衡策略包含了内置策略、扩展策略两个类别，这期我们先说说内置策略，而扩展策略顾名思义其实是第三方提供的，类似于插件。

内置策略包含了以下 3 种。

1、轮询策略

将每个用户请求（也是客户端请求）按一定顺序逐个地代理转发到不同的微服务节点上。

2、加权策略

权是权重的意思，我们可以调整某些个后端节点的权重，性能足够的话权重可以加些，给其他节点兄弟分担分担压力。

3、IP Hash策略

这个策略对请求IP进行了`Hash`操作，也就是相同的Hash结果都会代理转发到同一个微服务节点上。

### 1.3 正向代理和反向代理

> ***面试官：那正向代理和反向代理有什么区别？***

这两个概念很多网上的解释十分绕口，解释不清。我们先说反向代理。

（1）反向代理

通过上文Nginx反向代理的说明，我们可以知道反向代理配置了暴露给用户的链接与实际服务器地址的映射关系。通过反向代理，可以保护企业内部服务器不被直接暴露、负载均衡处理用户请求等。

（2）正向代理

理解反向代理我们是从企业内部服务器的角度，而理解正向代理，我们要从用户的角度来看。

用户通过浏览器访问某个链接，这个链接的请求先到达**正向代理服务器**，再由正向代理服务器代理用户做这一次请求动作，最后把请求结果返回给用户。

正向代理服务器在其中实际上充当了一部分防火墙的功能，可以保护局域网内用户的安全。
# Redis主从数据同步过程：命令传播、部分重同步、复制偏移量等
## 1. Redis数据同步

### 1.1 数据同步过程

> ***面试官：我看你们项目用的Redis主从，数据同步了解吗？***

回答数据同步过程问题前，大家有没想过为什么Redis要数据同步？不会是MySQL主从架构要数据同步，Redis就照猫画虎吧。

虽然这两者有关系型数据库和非关系型数据库的差异，但都是作为存储数据的数据库系统。而**主从架构**的目的就在于对数据有多个"备份"，有了多个"备份"，就自然而然衍生出众多好处。如负载均衡、灾难恢复、数据备份。

既然要"备份"，那数据同步就必不可少了。Redis主从数据同步大致的过程如下。

1. 首先，从服务器会先向主服务器发送**SYNC命令**。
2. 收到命令后，Redis主服务器会执行**BGSAVE命令**来生成一个**RDB文件**，并使用**AOF缓冲区**来记录在生成期间执行的写命令。关于BGSAVE命令和SAVE命令的区别，大家可以往前阅读我写的Redis系列文章。
3. 完成第二步后，主服务器会将RDB文件发送给从服务器，让从服务器同步RDB文件数据。
4. 当然这还没完，在生成RDB文件的过程中，仍然会有其他写命令到达服务器。Redis主服务器的AOF缓冲区会继续发送给从服务器，让它们之间的数据同步至**最终状态**。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/5840d24a49b24ffea10925353657601f.png#pic_center)

### 1.2 命令传播

> ***面试官：按你这么说，数据同步后主服务器某个键删除了，数据又不同步了怎么办？***

有了AOF缓冲区的概念还没完，Redis主从复制还有一个**命令传播**的概念等着你去学。

从服务器使用SYNC进行初次数据同步后，主、从服务器的数据库状态并不是每时每刻都保持一致的，这种情况反而是常态。肯定不能为了一条写指令的差异就重新执行SYNC命令，因为SYNC命令是一个非常**耗费资源**的操作。

这种情况Redis主服务器会将造成主从服务器数据不一致的写命令，即最近执行的写命令，发送给从服务器执行。这便是**命令传播**的过程，当从服务器执行命令后，主从服务器的数据库状态也就保持了一致。

如果后续有新的命令写入主服务器，主服务器会继续重复命令传播的过程。

### 1.3 部分重同步

> ***面试官：如果主从服务器断线呢？还是用的RDB来同步吗？***

主从服务器**断线**的话，假设你是Redis开发者，要怎么高效地恢复主、从服务器数据同步的状态。

如果还是用的RDB文件来同步，也太浪费资源了。有可能只是短时间断线，执行的写命令不过几十个，上文我已经提到SYNC命令是很耗费资源的一种操作。

能不能有一支记号笔，在主、从服务器断线时在主服务器的命令队列画下一个记号？

其实Redis除了提供SYNC命令的支持，还有一个叫**PSYNC命令**。

主从服务器断线后，Redis从服务器会发送一个PSYNC命令给主服务器。收到命令后主服务器会将两者**断线期间执行的写命令**一条不剩地发送给从服务器。

从服务器执行命令后，主、从服务器的数据也就同步了。这种同步方式也叫**部分重同步**。

### 1.4 复制偏移量

> ***面试官：那主服务器怎么知道断线期间执行了哪些命令？***

提前剧透下，前面提到的记号笔就是**复制偏移量**，命令队列也就是**复制积压缓冲区队列**。

Redis主、从服务器都会去维护一个**复制偏移量**，复制偏移量是什么？例如主从服务器的初始偏移量都是0，主服务器发送给从服务器**N字节数据**后，主从服务器的偏移量就会 + N。复制偏移量通过该数值来代表主服务器发送给从服务器的**字节总量**。

通过复制偏移量就可以来记录同步状态。

Redis其实有是一个容器来存储**命令传播**的写命令，命令传播的命令保存在一个有**复制偏移量**标识的**复制积压缓冲区**队列。

从服务器发送PSYNC命令给主服务器，还会同时发送从服务器的复制偏移量。主服务器只要根据该复制偏移量在复制积压缓冲区队列中找到对应的命令，就可以发送相关命令给到从服务器。

## 2. 服务器运行ID

> ***面试官：知道服务器运行ID吗？***

每个Redis节点都有自己的服务器运行ID，这个ID由服务器启动时自动生成。

当从服务器对主服务器进行**初次复制**时，主服务器会将自己的**运行ID**传送给从服务器，而从服务器则会将这个运行ID保存起来。

当断线后数据同步时，从服务器会向当前连接的主服务器发送之前保存的**主服务器运行ID**。

如果此时主服务器发现从服务器发送的**运行ID**与自己的不一致。那就说明此时的主服务器是**新的**主服务器，它也没有**复制积压缓冲区**队列，也就不能进行**部分重同步**。此时Redis主服务器会向从服务器发送RDB文件来进行数据同步。

## 3. Redis心跳检测

> ***面试官：Redis心跳检测知道吧？***

从服务器默认会**每秒一次**向主服务器发送心跳检测命令，如果主服务器超过1s没有收到replconf命令，说明主从服务器的网络连接有问题了。

以下是心跳检测命令。

```js
REPLCONF ACK ＜replication_offset＞
```

同时这个心跳检测命令还会附带传送一个**复制偏移量**，也就是上文的`replication_offset`。

在心跳检测时的过程中，如果主服务器发现他们的复制偏移量不一致，就会通过该偏移量找到从服务器**丢失的写命令**，从而发送给从服务器保持同步。

到这我们就知道了，心跳检测不仅仅能让主服务器检测从服务器是否存活。Redis开发者很聪明，在从服务器发送心跳检测命令时添加复制偏移量，让心跳检测也具有**检测命令丢失**的功能。
# Redis重要知识点：哨兵是什么？哨兵如何选择Redis主服务器
## 1. Redis哨兵

### 1.1 哨兵作用

> ***面试官：Redis哨兵知道吧？***

哨兵的含义是什么？我们来看看百度百科的解释。

> 哨兵，汉语词语，是指站岗、放哨、巡逻、稽查的士兵

Redis主从架构也有自己的哨兵，名为Sentinel。Sentinel是什么含义，我们看看英文含义，很遗憾这个英文起名没有什么故事可讲，英文意思还是哨兵。

Redis哨兵本质是一个运行在特殊模式下的Redis服务器，并不是特殊要另外部署的服务模块。哨兵可以是一个，如果公司资金充足的话，部署由多个Sentinel实例组成的哨兵系统也是可以的。

那哨兵有什么作用？

它的主要作用是通过检测Redis主从服务器的下线状态，**选举出新Redis主服务器**，也就是**故障转移**，来保证Redis的高可用性。

### 1.2 检测主从下线状态

> ***面试官：你说说是怎么检测Redis主从服务器的下线状态的？***

我们先来讲讲哨兵最重要的第一个功能，检测Redis主从服务器下线状态，后面我们再来讲讲故障转移。

哨兵检测主从服务器下线状态有两种方式，分为主观和客观，我们可以给哨兵配置其中一种。

（1）**检测主观下线状态**：默认情况Sentinel会每隔 1 s向Redis主、从服务器发送PING命令，通过PING命令返回的信息来判断Redis主从服务器的下线状态。

（2）**检测客观下线状态**：Sentinl在主观判断下线后，会向其他Sentinel进行询问**是否同意**该节点已下线，当标记下线的**数量足够多**就会判断客观下线。

下面是哨兵们和Redis主从服务器之间藕断丝连的关系。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/41279bc71813415bb35dd0f59a3c5d9e.png#pic_center)

### 1.3 检测下线状态不一致

> ***面试官：有没有A哨兵判断Redis实例下线，但B哨兵判断Redis实例仍然存活的情况？***

各个**哨兵的配置**对检测下线的配置不同，可能会产品奇奇怪怪的问题，大家要注意下。

假如我们的A、B两个哨兵配置的是检测主观下线状态，哨兵会判断Redis实例进入主观下线**所需的响应时间长度**。

南哥假设A哨兵的配置是10000毫秒、B哨兵是50000毫秒，但此时Redis实例要在20000毫秒才响应，像这种情况就会发生A哨兵判断Redis实例下线，但B哨兵判断Redis实例仍然存活的情况。

## 2. 哨兵选举

### 2.1 选举领头哨兵

> ***面试官：领头哨兵怎么选举出来的？***

大家注意不要把领头哨兵和Redis主服务器弄混淆了，不然可就尴尬了哈。

南哥先说说领头哨兵的作用，免得大家误解。**领头Sentinel**起到执行故障转移的作用，也就是**选举出新的Redis主服务器**，而且只有当Redis主服务器被判断**客观下线**后才会选举出领头Sentinel。

那领头哨兵要怎么选择出来呢？选举出这个天选之子。

Sentinel哨兵设置局部领头Sentinel的规则是**先到先得**。

最先向**目标Sentinel**发送设置要求的源Sentinel将成为目标Sentinel的**局部领头Sentinel**，而之后接收到的所有设置要求都会被目标Sentinel拒绝。

如果有某个Sentinel被**半数以上**的Sentinel设置成了局部领头Sentinel，那么这个Sentinel就会成为领头Sentinel。

### 2.2  选举Redis主服务器

> ***面试官：知道怎么选举新的Redis主服务器吗？***

看到这，我来和大家讲讲哨兵最重要的第二个功能：选举出新的Redis主服务器。

（1）领头Sentinel会将已下线Redis主服务器的所有Redis从服务器保存到一个列表里面。

（2）通过**删除策略**，删除所有处于下线或者断线状态的、删除最近五秒内没有回复过领头Sentinel命令的、删除与已下线主服务器连接断开超过10毫秒的。

（3）如果有多个相同优先级的从服务器，将按照**复制偏移量**进行排序选出偏移量最大的，复制偏移量最大也就是数据同步最新的。

（4）最后选出的Redis实例也就成为新的Redis主服务器。
# Redis面试必备：Redis两种内存回收策略，Redis键空间、过期字典等
## 1. Redis数据库

### 1.1 Redis数据库的理解

> ***面试官：Redis的数据库知道吧？***

我们可以把Redis的数据库和MySQL的数据库理解成同一个东西，不同数据库之间都是相互隔离的，在一个数据库中定义的键对其他数据库**不可见**。例如我们在Redis的数据库1设置键值对，在数据库1可以查询出来，而在数据库2中是查询不出来的。

```shell
# 示例命令
127.0.0.1:6379> select 1
OK
127.0.0.1:6379[1]> set name JavaGetOffer
OK
127.0.0.1:6379[1]> select 2
OK
127.0.0.1:6379[2]> get name
(nil)
127.0.0.1:6379[2]> select 1
OK
127.0.0.1:6379[1]> get name
"JavaGetOffer"
```

Redis默认会创建**16**个数据库，在业务上我们可以把**不同业务所需键值对**存储在不同Redis数据库，来达到根据业务划分不同数据库存储的作用。

```shell
# 查询一共有几个数据库
127.0.0.1:6379> config get databases
1) "databases"
2) "16"
```

另外Redis数据库主要由这两部分组成：dict字典即键空间、expires字典即过期字典，我们下文会讲到。

### 1.2 数据库的键空间

> ***面试官：那数据库的键空间呢？***

键空间顾名思义是存储键的容器，在Redis上**字典**存储了数据库中**所有的键值对**，这个字典也就是键空间。

大家记住不要把字典和Redis提供的哈希对象弄混淆了，前者是Redis的底层数据结构支持，而后者是Redis提供给外部使用的。

键空间的概念图如下，dict字典存储了所有键，每个键的指针指向值的引用地址。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/802e0ec1aa520fc95d1c0d0dd307c490.png#pic_center)


Redis对键值对的添加、删除、更新、查询操作都是基于**键空间**的基础上，先从dict字典查询出键，再根据键找到对应值进行操作。

### 1.3 键过期时间

> ***面试官：一个键要怎么设置过期时间？***

可以先设置键值对，后使用`EXPIRE命令`来设置键的过期时间，过期时间的单位是**秒**。

```shell
127.0.0.1:6379> set name0 JavaOffer训练营
OK
127.0.0.1:6379> expire name0 66
(integer) 1
127.0.0.1:6379> ttl name0
(integer) 66
127.0.0.1:6379> get name0
"JavaOffer训练营"
```

另外也可以使用`SETEX命令`一步到位，同时设置值和过期时间。

```shell
127.0.0.1:6379> setex name 66 JavaGetOffer
OK
127.0.0.1:6379> ttl name
(integer) 66
127.0.0.1:6379> get name
"JavaGetOffer"
```

大家回答面试官时补充企业实战的具体细节是可以加分的，例如对键值对设置过期时间，可以使用Jedis客户端的setex方法。

```java
    public String setex(String key, int seconds, String value) {
        this.checkIsInMultiOrPipeline();
        this.client.setex(key, seconds, value);
        return this.client.getStatusCodeReply();
    }
```

### 1.4 过期字典

> ***面试官：那键的过期时间知道用什么存储吗？***

既然所有键使用字典存储起来，那键的过期时间也可以使用字典存储起来，这个字典我们称它为**过期字典**。

因为键空间已经存储了所有的键值对，过期字典没必要再存储一次，所以过期字典的键地址**指向的是键空间的指针**。而过期字典的值是一个long long类型的整数，代表了过期日期的**UNIX时间戳**。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8bd023e13118de5b5632556b907ea62b.png#pic_center)


## 2. 内存回收策略

### 2.1 过期键删除策略

> ***面试官：键的过期删除策略是什么？***

过期键删除策略一共有三种：定时删除策略、惰性删除策略、定期删除策略。其中定时删除、定期删除是主动删除，而惰性删除是被动删除。

每一种删除策略都有其优缺点，也适应不同的业务场景。

一、**定时删除**对内存友好，对CPU不友好。定时删除策略会为设置过期时间的键**创建一个定时器**，使用定时器可以定时删除过期的键值对，释放出内存；但在**大量定时器**执行过程中会占用一部分CPU。如果在Redis的内存充沛但CPU非常紧张的业务场景下，此时定时器再执行，无疑会影响Redis的响应时间和吞吐量。

二、**惰性删除**对CPU友好，对内存不友好，可能会出现内存泄漏。该策略会**放任**过期的键不管，直到每次获取键，如果发现键过期了，才会释放出键内存。如果在大量键没被访问的业务场景下，Redis内存会大量浪费在已过期的键上。

三、**定期删除策略**。每隔一段时间检查数据库中一部分的键，删除其中的过期键，该策略可以设置删除操作的执行时长和频率。它的缺点在于确认删除操作的执行时长和频率比较麻烦。

三种过期键删除策略各有优缺点，Reids服务器实际上是采用了**惰性删除策略、定期删除策略**这两种策略配合使用，让服务器在避免CPU紧张和内存消耗过多之间取得平衡。

### 2.2 内存淘汰策略

> ***面试官：Redis还有什么策略可以释放内存？***

为了节约内存，Reids除了会对过期键进行删除外，还会在内存达到**内存上限**时进行内存回收，也就是Redis的**内存淘汰策略**。

内存上限可以通过config命令来动态配置。

```shell
127.0.0.1:6379> config set maxmemory 1GB
OK
127.0.0.1:6379> config get maxmemory
1) "maxmemory"
2) "1073741824"
```

而内存溢出控制策略一共有六种，我们可以通过配置`maxmemory-policy`参数来进行控制。

1. noeviction：默认策略不会删除任何键值对，同时会拒绝所有写命令。
2. volatile-lru：根据**LRU最近最少使用算法**删除设置了过期时间的键，直到腾出足够的空间。如果没有可删除的键对象，则会回退到noeviction策略。
3. allkeys-lru：和volatile-lru同样的作用，不过针对的是**所有键**。
4. allkeys-random：随机删除所有键，直到腾出足够的空间。
5. volatile-random：随机删除过期键，直到腾出足够的空间。
6. volatile-ttl：删除最近将要过期的键。看到后缀ttl我们就知道这个策略和过期时间相关。

以下是Redis配置文件提供的六种内存淘汰策略介绍，大家可以参考下。

```shell
# MAXMEMORY POLICY: how Redis will select what to remove when maxmemory
# is reached. You can select among five behaviors:
#
# volatile-lru -> Evict using approximated LRU among the keys with an expire set.
# allkeys-lru -> Evict any key using approximated LRU.
# volatile-lfu -> Evict using approximated LFU among the keys with an expire set.
# allkeys-lfu -> Evict any key using approximated LFU.
# volatile-random -> Remove a random key among the ones with an expire set.
# allkeys-random -> Remove a random key, any key.
# volatile-ttl -> Remove the key with the nearest expire time (minor TTL)
# noeviction -> Don't evict anything, just return an error on write operations.
#
# LRU means Least Recently Used
# LFU means Least Frequently Used
#
# Both LRU, LFU and volatile-ttl are implemented using approximated
# randomized algorithms.
#
# Note: with any of the above policies, Redis will return an error on write
#       operations, when there are no suitable keys for eviction.
#
#       At the date of writing these commands are: set setnx setex append
#       incr decr rpush lpush rpushx lpushx linsert lset rpoplpush sadd
#       sinter sinterstore sunion sunionstore sdiff sdiffstore zadd zincrby
#       zunionstore zinterstore hset hsetnx hmset hincrby incrby decrby
#       getset mset msetnx exec sort
#
# The default is:
#
# maxmemory-policy noeviction
```
# Redis面试必问：Redis为什么快？Redis五大基本数据类型
## 1. Redis快的秘密

> ***面试官：Redis什么这么快？***

相信大部分Redis初学者都会忽略掉一个重要的知识点，Redis其实是**单线程模型**。我们按直觉来看应该是多线程比单线程更快、处理能力更强才对，比如单线程一次只可以做一件事情，而多线程却可以同时做十件事情。

但Redis却可以做到**每秒万级别**的处理能力，主要是基于以下原因：

（1）Redis是**基于内存**操作的，Redis所有的数据库状态都保存在内存中。而内存的响应时长是非常快速的，大约在100纳秒。大家可以对比下其他服务器磁盘，固态硬盘（SSD）、机械硬盘（HDD）响应时长大约几十微秒，很明显远远没有基于内存的响应时长快速。

（2）Redis采用**I/O多路复用**技术，这种I/O模型是非阻塞I/O，应用程序在等待I/O操作完成的过程中不需要阻塞。

（3）最后一点也是我开头提到的，Redis采用了**单线程模型**。单线程模型避免了多线程产生的线程切换和锁竞争带来的资源消耗，这两种消耗对性能影响是很大的。另外一点是单线程相比多线程来说实现更简单高效，如果引入多线程设计相信Redis实现起来会更加复杂不易优化。

## 2. Redis数据类型

### 2.1 Redis五大基本数据类型

> ***面试官：你说说Redis五大基本数据类型？***

Redis基本数据类型一共有五种，这也是面试官**重点考查**的基础，大家要重点关注下。

（1）字符串。

字符串是Redis最基础，也是业务开发中最常见的一种数据类型。在业务上一般使用MySQL作为实际存储层，而Redis字符串作为**缓冲层**对象。

```sh
127.0.0.1:6379> set name JavaGetOffer
OK
127.0.0.1:6379> get name
"JavaGetOffer"
```

（2）哈希。

哈希的键值本身是一个**键值对结构**，类似于`key = {{field, value}, {field, value}}`。

我们可以使用`hset`命令设置哈希键值，而`hget`命令可以获取哈希对象中某个field的值。

```sh
127.0.0.1:6379> hset msg name JavaGetOffer
(integer) 1
127.0.0.1:6379> hset msg avator 思考的陈
(integer) 1
127.0.0.1:6379> hget msg name
"JavaGetOffer"
127.0.0.1:6379> hget msg avator
"思考的陈"
```

（3）列表。

Redis的列表是一个**有序列表**，但大家注意一点，此处所说的**有序**不是按数据大小排序的有序，而是按插入顺序的有序。另外一点特殊之处是我们可以往列表的左右两边添加元素。

```sh
# 从右边添加
127.0.0.1:6379> rpush number 1 2 3
(integer) 3
# 从左边添加
127.0.0.1:6379> lpush number 4 5 6
(integer) 6
127.0.0.1:6379> lrange number 0 5
1) "6"
2) "5"
3) "4"
4) "1"
5) "2"
6) "3"
```

（4）集合。

集合类型和列表不同之处在于它是无序的，同时也不支持保存**重复的元素**。

另外两个集合之间可以获得**交集、并集、差集**。利用这一点，如果在业务上要求得两个用户相同的兴趣标签，可以使用Redis集合存储用户兴趣标签，再使用交集命令来查询。

```sh
127.0.0.1:6379> sadd user:1:like game bask run
(integer) 3
127.0.0.1:6379> sadd user:2:like game basketball fitness
(integer) 3
# 求交集
127.0.0.1:6379> sinter user:1:like user:2:like
1) "game"
```

（5）有序集合。

有序集合算是Redis中比较特殊的一种数据类型，有序集合里的每个元素都带有一个score属性，通过该score属性进行排序。如果我们往有序集合插入元素，此时它就不像**列表对象**一样是插入有序，而是根据score进行排序的。

```sh
127.0.0.1:6379> zadd 100run:ranking 13 mike
(integer) 1
127.0.0.1:6379> zadd 100run:ranking 12 jake
(integer) 1
127.0.0.1:6379> zadd 100run:ranking 16 tom
(integer) 1
127.0.0.1:6379> zrange 100run:ranking 0 2
1) "jake"
2) "mike"
3) "tom"
```

### 2.2 有序集合业务场景 

> ***面试官：有利用过有序集合开发过什么功能吗？***

有序集合典型的业务开发场景是实现一个**排行榜**，我们可以通过有序集合的score元素来作为排行榜排序的标准。

而排行榜的获取一般是分页获取，我们可以使用jedis客户端提供的`zrevrangeWithScores`方法来获得，返回的类型是一个`Set<Tuple>`，从Tuple对象中可以获得元素和score值，如代码所示。

```java
        try (Jedis jedis = jedisPool.getResource()) {
            String rankKey = "rankKey";
            Set<Tuple> rankTuple = jedis.zrevrangeWithScores(rankKey, index, index + pageSize - 1);

            List<UserRankBO> = rankTuple.stream().map(r -> UserRankBO.builder()
                    .uid(Integer.parseInt(r.getElement()))
                    .score(r.getScore())
                    .build()).collect(Collectors.toList());
        }
```

```java
    public Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        this.checkIsInMultiOrPipeline();
        this.client.zrevrangeWithScores(key, start, stop);
        return this.getTupledSet();
    }
```

### 2.3 有序集合数据结构

> ***面试官：有序集合用什么数据结构来实现？***

有序集合有两种内部编码：ziplist和skiplist。ziplist编码是以压缩列表来实现，而在skiplist编码中是同时使用字典和跳跃表两种数据结构来实现，原因下个`面试官问题`有提及。

（1）字典。

字典里保存的是**键值对结构**，和上文提交的哈希对象不是同一个级别的产物，字典是Redis内部的数据结构，而哈希对象是提供给外部使用的。例如存储键的键空间、存储建过期时间的过期字典都是由字典来实现的。

字典的组成结构如下所示。可以看到ht数组有两个dictht哈希表，Redis的平常使用时只使用其中一个哈希表，而另一个是在迁移扩展哈希表**rehash**时使用。当迁移完成后，原先日常使用的旧哈希表会被**清空**，而新的哈希表变成日常使用的。

```c
typedef struct dict {
    dictType *type;
    void *privdata;
    // 哈希表
    dictht ht[2];
    in trehashidx;
} dict;
```

（2）跳跃表。

跳跃表的底层结构类似于一个值 + 保存了**指向其他节点的level数组**，而这个level数组的作用就是用来**加快访问**其他节点的速度。跳跃表的查询效率是比较快的，可以和平衡二叉树相媲美，同时跳跃表相比平衡树的实现更加的简单。

跳跃表的组成结构如下所示。

```c
typedef struct zskiplistNode {
    // level数组
    struct zskiplistLevel {
        // 前进指针
        struct zskiplistNode *forward;
        // 跨度
        unsigned int span;
    } level[];
    // 后退指针
    struct zskiplistNode *backward;
    // 分值
    double score;
    robj *obj;
} zskiplistNode;
```

### 2.4 为什么使用字典和跳跃表

> ***面试官：那有序集合为什么要使用字典和跳跃表？***

同时使用字典和跳跃表的设计主要是考虑了**性能**因素，两者都有其效率最高的场景，要高效利用它们来提高Redis性能。

1. 如果单纯使用字典，**查询时**的效率很高，可以达到高效的O(1)时间复杂度。但执行类似ZRANGE、ZRNK命令时，效率是比较低的。因为每次排序需要在内存上对字典进行排序一次，这消耗了额外的O(n)内存空间。
2. 如果单纯使用跳跃表，虽然执行类似ZRANGE、ZRNK命令时的效率高，但**查询性能**又会从O(1)上升到了O(logN)。

所以Redis内部会对有序集合采用字典和跳跃表两种实现，当使用对应不同场景时，就采用对应的不同数据结构来高效操作有序集合。

## 3. 压缩列表

> ***面试官：压缩列表呢？***

压缩列表顾名思义作用在于压缩，主要是Redis为了节约内存开发的一种数据结构。一共有三种数据类型使用到了压缩列表。

列表键里如果包含的都是类似小整数、短字符串类型的，会采用压缩列表的底层实现。

```sh
127.0.0.1:6379> rpush number 1 2 3
(integer) 3
127.0.0.1:6379> object encoding number
"ziplist"
```

哈希键如果只包含少量的键值对，同时键、值都是类似小整数、短字符串类型的，会采用压缩列表的底层实现。

```sh
127.0.0.1:6379> hset msg name JavaGetOffer
(integer) 1
127.0.0.1:6379> hset msg avator 思考的陈
(integer) 1
127.0.0.1:6379> object encoding msg
"ziplist"
```

有序集合当元素个数小于128个时，内部编码会转换为压缩列表ziplist。

```sh
127.0.0.1:6379> zadd 100run:ranking 13 mike 12 jake 16 tom
(integer) 3
127.0.0.1:6379> object encoding 100run:ranking
"ziplist"
```
# Spring面试必备：Spring IOC和AOP的理解、如何解决Spring循环依赖
## 1. Spring IOC和AOP

### 1.1 Spring IOC

> ***面试官：你说下对Spring IOC的理解？***

Spring IOC其实是一种通过**描述**来创建和获取对象的技术，相比于最原始的通过new来创建对象，所有的对象都交由Spring IOC进行管理，我们管这些对象称为Spring Bean。

Spring Bean可以看成是班级里的学生，那IOC容器就是容纳学生的班级。每个Bean的分类、不同的生命周期，包括Prototype、Singleton、Request、Session、Global session都可以在IOC容器里进行管理。这其实是一种控制反转的思想，我们程序员把控制对象的权限都交由了靠谱的Spring IOC容器。

通过XML方式我们可以向Spring IOC描述我需要一个A对象。当Spring启动时这个Bean也就自动注入到IOC容器等待我们的使用。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="bean1" class="org.springframework.beans.factory.ConcurrentBeanFactoryBenchmark$ConcurrentBean"
			scope="prototype">
    <property name="date" value="2004/08/08"/>
  </bean>

</beans>
```

现在商业公司通过以上XML的方式已经是很少见了，Spring Boot提供了另一种通过注解来**描述**Bean的方式。Spring Boot底层基于注解的IOC容器是`AnnotationConfigApplicationContext`，这个留到我后续的文章再来讲解。

```java
// 通过注解的方式来创建Bean
@Configuration
public class TokenConfig {
    /**
     * 设置token的类型
     **/
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter()); // 设置token类型为JWT
    }
}
```

### 1.2 Spring AOP

> ***面试官：AOP呢？***

在整个软件编程的历史长河中，最先面世的一种编程范式叫做**面向过程编程**。但随着软件系统越来越复杂，面向过程编程越来越难以管理软件的复杂性。于是**面向对象编程OOP**诞生，它致力于解决困扰前者的软件复杂性问题。

但面向对象编程并不是银弹，它存在一些弊端，例如我们需要为整个项目的所有对象都引入一个公共行为：打印对象每个方法的执行耗时。如果采用OOP需要把公共行为的代码都**重复**贴到每个对象的类里。。。

现在救世主来了，AOP面向切面编程弥补了OOP的缺陷。

通过Spring  AOP，我们可以为每个对象约定一套流程，为对象方法执行前执行后织入一些行为。如果是日志的话，在对象方法执行后触发就可以了。

Spring  AOP提供了多个注解，我们来看看。

1. @Before。前置通知，在方法执行之前执行。
2. @After。后置通知，在方法执行之后执行。
3. @AfterReturning。返回通知，方法不抛出异常，**正常退出**方法时执行。
4. @AfterThrowing。异常通知，方法抛出异常后执行 。
5. @Around。环绕通知，围绕着方法执行，可以自定义在方向执行前、执行后执行一段逻辑，**自由度更高**。
6. @Pointcut。切点。定义了要拦截的对象。

```java
// 防重复提交的切面
@Aspect
@Component
public class SubmitAspect {
    
    @Pointcut("@annotation(banRepeatSubmit)")
    public void pointCut(BanRepeatSubmit banRepeatSubmit) {}

    @Around("pointCut(banRepeatSubmit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, BanRepeatSubmit banRepeatSubmit) throws Throwable {
        // 防止对象方法重复执行
    }
}
```

### 1.3 AOP的原理

> ***面试官：那AOP的原理是什么？***

在使用上文的六种注解时，这些注解是封装在一个由`@AspectJ`注解修饰的类，我们管这个类叫做**切面**。

Spring AOP扫描到@Pointcut定义的切点时，就会自动为该Bean创建一个代理。而Spring Boot目前底层的代理模式有两种：JDK动态代理、CGLIB动态代理。

如果被代理的对象实现了接口，则Spring会使用JDK动态代理；如果被代理对象没有实现接口，则Spring会使用CGLIB动态代理。原因是JDK动态代理要求被代理对象必须实现至少一个接口。

JDK动态代理通过生成代理对象的**字节码文件**，使要拦截的方法跳转到invoke()方法，而在invoke()里就是在**切面**里定义的各种拦截逻辑。

而CGLIB是通过生成代理类的**子类实现**，同时**修改字节码**文件让子类方法覆盖代理类的方法，从而实现对拦截方法的代理。

另外Spring AOP还集成了AspectJ，一种编译织入的方式来代理对象。

### 1.4 JDK和CGCLIB动态代理

> ***面试官：JDK和CGCLIB动态代理哪个更快？***

两者的生命周期可以分为创建对象阶段、实际运行阶段，我们要根据具体情况具体分析。

1. 在**实际运行**阶段，CGLIB性能比JDK运行性能**更高**。
2. 在**创建对象**阶段，基于两者的原理，CGLIB花费在**创建对象**的时间要比JDK多。JDK**只需**创建代理类的字节码，**而**CGLIB既要修改源代码的字节码文件，又要生成代理类的子类的字节码文件。

综上所述，对于需要创建大量对象的场景，JD动态代理K比CGLIB动态代理效率更高，反之CGLIB效率更高。

## 2. Spring循环依赖

### 2.1. 解决Spring循环依赖

> ***面试官：知道怎么解决Spring循环依赖吗？***

谈到循环依赖，大多数人都是望而生畏。一旦发生了循环依赖，说明了这部分软件设计的**职责划分**出现了问题，而且要修复起来不是一件容易的事。如果是屎山代码，那可就头大了。。。

如下例子，对象A的成员变量引用了对象B，而对象B的成员变量引用了对象A。也就是说，对象A的初始化完成要先完成对象B的初始化，但对象B的初始化完成又要先完成对象A的初始化，形成了一个永远无法实现的环。

```java
    class A {
        B b = new B();
    }
    class B {
        A a = new A();
    }
```

Spring设计出了三个级别的缓存。一级缓存存放实例化、初始化的bean、二级缓存存放已实例化但没有初始化的bean、三级缓存存放创建**bean的工厂对象**。

例如现在有对象A依赖对象B，对象B依赖对象A。

Spring首先从**三级缓存**获得实例化的A、B；接着让A进入**二级缓存**，同时将**未初始化**的B注入A，这就得到了实例化、初始化的**A**，此时A就会进入**一级缓存**；最后一步，只需要再把初始化的A注入到B，此时循环依赖解决。
# ZooKeeper面试必备：ZooKeeper4种数据节点类型、了解事务ID
## 1. ZooKeeper数据模型

### 1.1 ZooKeeper数据节点

> ***面试官：你说说ZooKeeper数据模型？***

ZooKeeper的数据模型是一颗树结构，每一个树节点是一个数据节点，我们称它为**ZNode**。

而每一个ZNode的节点路径标识使用`斜杠/`作为分隔符，我们可以在ZNode节点下写入数据、创建节点，这种`斜杠/`作为路径分隔符的方式和Unix文件系统路径非常相似。

大家可以看下Unix文件系统路径，以`斜杠/`作为路径分隔符。

```shell
# 根目录
/
# 可执行文件所在位置
/bin
# 设备驱动
/dev
```

这是ZooKeeper数据模型概念图，是不是非常类似呢？

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/db44d3c7fdae43389ecf0388bb36d5ae.png#pic_center)


另外ZooKepper这种`斜杠/`作为路径分隔符正好和**Windows相反**，Windows使用的是`反斜杠\`。

```shell
# Windows路径示例
C:\Java\jdk1.8.0_311
```

### 1.2 数据节点类型

> ***面试官：那ZooKeeper数据节点有几种类型？***

ZooKeeper一共有四种节点类型，但从整体来看主要是持久节点类型、临时节点类型这两种，另外两种类型只是在以上两种节点类型基础上增加了顺序的特性。大家这样理解会更方便记忆~

1. **持久节点**：这种数据节点一旦背创建后，就会一直存在于ZooKeeper服务器上，除非对该数据节点执行删除操作。
2. 持久顺序节点：刚刚和大家说了，该节点类型就是在持久节点基础上增加了顺序的特性。如果在持久顺序节点类型的父节点创建子节点，ZooKeeper会为该子节点名加上一个数字后缀来维护子节点的顺序。
3. **临时节点**：临时节点比较特殊，它的生命周期是和**客户端会话**绑定在一起的。这个客户端可以是连接ZooKeeper的某一个终端命令窗口，也可以是连接ZooKeeper的某一个Spring服务线程。如果客户端会话失效了，那这个临时节点就会被自动清除。
4. 临时顺序节点：在临时节点的基础上添加了顺序特性。

另外大家记住一点，**临时节点只能作为叶子节点**，是不能在临时节点下面创建任何子节点的。原因大概是临时节点子节点没有存在的意义，创建子节点的场景大多是基于持久节点的场景，这种设计也可以防止对临时节点的误用。

### 1.3 数据节点的版本

> ***面试官：数据节点版本知道吧？***

ZooKeeper数据节点的版本概念和CAS操作的版本概念是一样的，同样是在多线程环境下，通过乐观锁这种无锁操作来保证线程安全性。

当一个数据节点被创建后，该节点的version值为0，代表它被**更新过0次**，如果后续对该节点进行更新操作，那version便会递增。

对数据节点的每次更新，都会对比数据节点的version是否是预期值，只有**符合预期值**，才会将新值更新到该数据节点。

大家可以通过以下CAS例子代码来了解CAS操作的执行过程。

```java
@Slf4j
public class CAS implements Runnable {

    private static AtomicInteger val = new AtomicInteger(0);

    private void compareSwap(int expectVal, int operateVal) {
        if (val.compareAndSet(expectVal, operateVal)) {
            log.info("the thread called {} operated", Thread.currentThread().getName());
        } else {
            this.run(); // 相当于获取不到锁则重新执行
        }
    }

    @Override
    public void run() {
        int curVal = val.get();
        log.info("the thread called {} get val is {}", Thread.currentThread().getName(), curVal);
        compareSwap(curVal, curVal + 1);
    }

    public static void main(String[] args) throws InterruptedException {
        CAS cas0 = new CAS();
        CAS cas1 = new CAS();

        Thread thread0 = new Thread(cas0, "cas0");
        Thread thread1 = new Thread(cas1, "cas1");
        thread0.start();
        thread1.start();
        thread0.join();
        thread1.join();
        log.info("current thread name: {} , the value of val: {}", Thread.currentThread().getName(), val);
    }
}

控制台打印：
[ INFO ]the thread called cas0 get val is 0
[ INFO ]the thread called cas1 get val is 0
[ INFO ]the thread called cas0 operated
[ INFO ]the thread called cas1 get val is 1
[ INFO ]the thread called cas1 operated
[ INFO ]current thread name: main , the value of val: 2
```

### 1.4 事务ID

> ***面试官：ZooKeeper事务ID呢？***

我们熟悉的数据库事务一般是包含**对数据库状态的读写操作**，数据库事务具有ACID特性：原子性、一致性、持久性、隔离性。数据库事务这块大家有不懂的可以看我往期文章《MySQL事务的性情很“原子“，要么执行要么不执行》。

但ZooKeeper的事务和数据库事务大相径庭。ZooKeeper事务一般是包括对**数据节点**的创建、删除、更新，也包括客户端会话创建、失效情况对**临时节点**的影响。

每一个事务请求，ZooKeeper都会为其分配一个全局唯一的事务ID，我们称它为**ZXID**。所以ZXID也可以间接反映出对数据节点操作的全局顺序，这个全局顺序在Follower服务器对Leader服务器的数据复制上相当重要，可以用来保证数据的一致性。

## 2. Watcher机制

> ***面试官：ZooKeeper数据变更通知使用什么对象？***

ZooKeeper拥有分布式通知的功能，这个功能是基于**Watcher机制**来实现的。一个Watcher对象就像一个订阅者，当订阅的主题状态发生变化，就会通知Watcher订阅者作出一定动作。

Watcher机制的工作流程，首先是客户端向ZooKeeper服务器注册Watcher通知，接着会将Watcher对象存储在客户端本身的**WatchManager**中。当ZooKeeper服务器触发Watcher事件后会向客户端发起通知，客户端就从本身的WatchManager取出对应的Watcher对象来执行**回调操作**。

Watcher机制的大致流程大家可以参考下图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/c40605ce22374bf0b19963b5cbe3d3d1.png#pic_center)
# 了解Redis集群概念，集群如何选举主节点
## 1. Redis集群

### 1.1 集群概念

> ***面试官：我看你简历写了Redis集群，你说一说？***

Redis主从架构和Redis集群架构是两种不同的概念，大家刚接触Redis时经常弄混淆。南哥给大家贴下Redis官网对两者的解释。

（1）Redis主从架构

> Redis主从实现了有一个易于使用和配置的领导者跟随者复制，它允许副本 Redis 实例成为主实例的精确副本。

（2）Redis集群架构

> Redis 集群将数据自动分片到多个 Redis 节点，Redis 集群还在分区期间提供一定程度的可用性，当某些节点发生故障或无法通信时，Redis集群能够继续运行。

它们两者都是Redis**高可用的解决方案**，但偏向点不同。Redis主从对数据的完整性更看重，Redis主从节点都保存了完整的一套数据库状态。

而Redis集群则对抗压能力更看重，整个集群的数据库整合起来才是一个完整的数据库。

在功能性上它们也有不同，Redis主从有哨兵，而Redis集群有分片。我们要看业务选择不同的Redis方案，当然，Redis集群还可以搭配Redis主从一起使用，我们可以在某一个集群节点上配置一套主从模型。

如果要6002、6003节点添加到6001节点的Redis集群里，我们可以使用以下命令。

```shell
127.0.0.1:6001＞ CLUSTER MEET 127.0.0.1 6002
OK
127.0.0.1:6001＞ CLUSTER MEET 127.0.0.1 6003
OK
```



### 1.2 集群分片

> ***面试官：那Redis集群怎么实现负载均衡的？***

大家要记住Redis集群一个很重要的知识点，那就是分片。

Redis集群通过分片的方式来保存数据库中的键值对，Redis集群把整个数据库分为**16384**个槽，而集群中的每个节点可以处理这里面的0个或最多16384个槽。

假如南友们在公司里配置了一个包含 3 个节点的集群，那么这3个节点的槽分配会是这样的：

- 节点 A 包含从 0 到 5500 的哈希槽。
- 节点 B 包含从 5501 到 11000 的哈希槽。
- 节点 C 包含从 11001 到 16383 的哈希槽。

那这样分片有什么作用？

大家想一想，有了分片，我们对某一个键值对的增删改查就会在三个集群节点中的其中一个进行，这样对Redis的各种操作也就**负载均衡**地下落到各个集群的节点中。

### 1.3 重新分片

> ***面试官：要是热点数据都是某个Redis节点的槽，负载均衡不是没用了？***

Redis集群甚至可以在线上环境直接执行**重新分片**功能，分片是不是很灵活呢？南哥给Redis点赞。

Redis官网对分片是这么解释的。

> Moving hash slots from a node to another does not require stopping any operations; therefore, adding and removing nodes, or changing the percentage of hash slots held by a node, requires no downtime.
>
> 
>
> 将哈希槽从一个节点移动到另一个节点不需要停止任何操作；因此，添加和删除节点，或更改节点持有的哈希槽百分比，不需要停机。

Redis集群重新分片可以将任意数量已指派给某个节点的槽改为指派给另一个节点，而相关槽所属的键值对也会从源节点被移动到目标节点。重新分片操作也不需要集群节点下线，源节点和目标节点也都可以继续处理命令请求。

要是小伙伴遇到热点数据都精确命中了Redis集群的某一个节点，赶快在线上环境紧急重新分片，把相关热点槽**指派**给其他节点处理，这也是一个不错的选择。

## 2. 集群的主从模型

### 2.1 主从模型

> ***面试官：Redis集群的主从模型，知道吗？***

还记得上文南哥提到过可以给Redis集群的某一个节点配置主从模型吗？

Redis集群把键值都分散在多个集群节点中，这也有缺点。例如某一个节点失效了，那这个节点里所有槽的键值对也都无法访问了。Redis官方当然也知道，主从模型可以让集群节点有1~N个副本节点。

像上文的Redis集群的A、B、C三个节点，主从模型可以为这每一个主节点添加一个副本节点。这样的话集群就变成了由A、B、C、A1、B1、C1组成，例如当A节点失效了，那它的副本节点A1就会提升为新的主节点。

主从模型也有另外的好处，我们可以让主节点用于处理槽，而副本节点用来分担**读的压力**。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/0d7eca445153433b8289f1ba3960decd.png#pic_center)


> 为集群B节点添加B1、B2副本节点

### 2.2 主节点选举

> ***面试官：那集群里怎么选举主节点的？***

Redis集群的主从模型选举主节点和Redis哨兵选举出主节点非常相似，但大家不要搞混了，Redis集群中并没有哨兵的概念。

主从模型选举主节点和**哨兵**选举领头哨兵一样是先到先得，而且它们投票的对象是**集群中的其他主节点**。

选举的流程如下。

（1）当从节点发现主节点进入下线状态时，会广播一条`CLUSTERMSG_TYPE_FAILOVER_AUTH_REQUEST`消息，要求其他集群主节点向改从节点进行投票。

（2）投票遵循先到先得的规则，集群主节点会投票给第一个发送选举信息的该从节点，返回一条`CLUSTERMSG_TYPE_FAILOVER_AUTH_ACK`消息。

（3）如果集群主节点的个数是N，当某个从节点收到大于等于`N / 2 + 1`张支持票时，代表该从节点获胜，该从节点也将成为新的主节点。
# 了解掌握WebSocket相比传统长轮询的优点
## 1. WebSocket概念

### 1.1 为什么会出现WebSocket

一般的Http请求我们只有主动去请求接口，才能获取到服务器的数据。例如前后端分离的开发场景，自嘲为切图仔的前端大佬找你要一个`配置信息`的接口，我们后端开发三下两下开发出一个`RESTful`架构风格的API接口，只有当前端主动请求，后端接口才会响应。

但上文这种基于HTTP的**请求-响应**模式并不能满足**实时数据通信**的场景，例如游戏、聊天室等实时业务场景。现在救世主来了，WebSocket作为一款主动推送技术，可以实现服务端主动推送数据给客户端。大家有没听说过全双工、半双工的概念。

> 全双工通信允许数据同时双向流动，而半双工通信则是数据交替在两个方向上传输，但在任一时刻只能一个方向上有数据流动

HTTP通信协议就是半双工，而数据实时传输需要的是全双工通信机制，WebSocket采用的便是全双工通信。举个微信聊天的例子，企业微信炸锅了，有**成百条消息轰炸**你手机，要实现这个场景，大家要怎么设计？用iframe、Ajax异步交互技术配合以客户端**长轮询**不断请求服务器数据也可以实现，但造成的问题是服务器资源的**无端消耗**，运维大佬直接找到你工位来。显然服务端主动推送数据的WebSocket技术更适合聊天业务场景。

### 1.2 WebSocket优点

大家先看看传统的Ajax长轮询和WebSocket性能上掰手腕谁厉害。在websocket.org网站提供的`Use Case C`的测试里，客户端轮询频率为10w/s，使用Poling长轮询每秒需要消耗高达665Mbps，而我们的新宠儿WebSocet仅仅只需要花费1.526Mbps，435倍的差距！！

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/872b8bcd6ed04404bfa2f3343e1e6054.png#pic_center)


为什么差距会这么大？南哥告诉你，WebSocket技术设计的目的就是要取代轮询技术和Comet技术。Http消息十分冗长和繁琐，一个Http消息就要包含了起始行、消息头、消息体、空行、换行符，其中**请求头Header**非常冗长，在大量Http请求的场景会占用过多的带宽和服务器资源。

大家看下百度翻译接口的Http请求，拷贝成curl命令是非常冗长的，可用的消息肉眼看过去没多少。

```sh
curl ^"https://fanyi.baidu.com/mtpe-individual/multimodal?query=^%^E6^%^B5^%^8B^%^E8^%^AF^%^95&lang=zh2en^" ^
  -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7" ^
  -H "Accept-Language: zh-CN,zh;q=0.9" ^
  -H "Cache-Control: max-age=0" ^
  -H "Connection: keep-alive" ^
  -H ^"Cookie: BAIDUID=C8FA8569F446CB3F684CCD2C2B32721E:FG=1; BAIDUID_BFESS=C8FA8569F446CB3F684CCD2C2B32721E:FG=1; ab_sr=1.0.1_NDhjYWQyZmRjOWIwYjI3NTNjMGFiODExZWFiMWU4NTY4MjA2Y2UzNGQwZjJjZjI1OTdlY2JmOThlNzk1ZDAxMDljMTA2NTMxYmNlM1OTQ1MTE0ZTI3Y2M0NTIzMzdkMmU2MGMzMjc1OTRiM2EwNTJQ==; RT=^\^"z=1&dm=baidu.com&si=b9941642-0feb-4402-ac2b-a913a3eef1&ss=ly866fx&sl=4&tt=38d&bcn=https^%^3A^%^2F^%^2Ffclog.baidu.com^%^2Flog^%^2Fweirwood^%^3Ftype^%^3Dp&ld=ccy&ul=jes^\^"^" ^
  -H "Sec-Fetch-Dest: document" ^
  -H "Sec-Fetch-Mode: navigate" ^
  -H "Sec-Fetch-Site: same-origin" ^
  -H "Sec-Fetch-User: ?1" ^
  -H "Upgrade-Insecure-Requests: 1" ^
  -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36" ^
  -H ^"sec-ch-ua: ^\^"Not/A)Brand^\^";v=^\^"8^\^", ^\^"Chromium^\^";v=^\^"126^\^", ^\^"Google Chrome^\^";v=^\^"126^\^"^" ^
  -H "sec-ch-ua-mobile: ?0" ^
  -H ^"sec-ch-ua-platform: ^\^"Windows^\^"^" &
```

而WebSocket是基于帧传输的，只需要做一次握手动作就可以让客户端和服务端形成一条通信通道，这仅仅只需要2个字节。我搭建了一个SpringBoot集成的WebSocket项目，浏览器拷贝WebSocket的Curl命令十分简洁明了，大家对比下。

```sh
curl "ws://localhost:8080/channel/echo" ^
  -H "Pragma: no-cache" ^
  -H "Origin: http://localhost:8080" ^
  -H "Accept-Language: zh-CN,zh;q=0.9" ^
  -H "Sec-WebSocket-Key: VoUk/1sA1lGGgMElV/5RPQ==" ^
  -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36" ^
  -H "Upgrade: websocket" ^
  -H "Cache-Control: no-cache" ^
  -H "Connection: Upgrade" ^
  -H "Sec-WebSocket-Version: 13" ^
  -H "Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits"
```

如果你要区分Http请求或是WebSocket请求很简单，WebSocket请求的请求行前缀都是固定是`ws://`。

## 2. WebSocket实践

### 2.1 集成WebSocket服务器

大家要在SpringBoot使用WebSocket的话，可以集成`spring-boot-starter-websocket`，引入南哥下面给的pom依赖。

```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-websocket</artifactId>
		</dependency>
	</dependencies>
```

感兴趣点开`spring-boot-starter-websocket`依赖的话，你会发现依赖所引用包名为`package jakarta.websocket`。这代表SpringBoot其实是集成了Java EE开源的websocket项目。这里有个小故事，Oracle当年决定将Java EE移交给Eclipse基金会后，Java EE就进行了改名，现在Java EE更名为Jakarta EE。Jakarta是雅加达的意思，有谁知道有什么寓意吗，评论区告诉我下？

我们的程序导入websocket依赖后，应用程序就可以看成是一台小型的WebSocket服务器。我们通过@ServerEndpoint可以定义WebSocket服务器对客户端暴露的接口。

```java
@ServerEndpoint(value = "/channel/echo")
```

而WebSocket服务器要推送消息给到客户端，则使用`package jakarta.websocket`下的Session对象，调用`sendText`发送服务端消息。

```java
    private Session session;
    
    @OnMessage
    public void onMessage(String message) throws IOException{
        LOGGER.info("[websocket] 服务端收到客户端{}消息：message={}", this.session.getId(), message);
        this.session.getAsyncRemote().sendText("halo, 客户端" + this.session.getId());
    }
```

看下`getAsyncRemote`方法返回的对象，里面是一个远程端点实例。

```java
    RemoteEndpoint.Async getAsyncRemote();
```

### 2.2 客户端发送消息

客户端发送消息要怎么操作？这点还和Http请求很不一样。后端开发出接口后，我们在Swagger填充参数，点击`Try it out`，Http请求就发过去了。

但WebSocket需要我们在浏览器的控制台上操作，例如现在南哥要给我们的WebSocket服务器发送`Halo，JavaGetOffer`，可以在浏览器的控制台手动执行以下命令。

```sh
websocket.send("Halo，JavaGetOffer");
```

实践的操作界面如下。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/1ff67a75514f4f87ac0ffc01a05b0017.png#pic_center)
# 可能是最漂亮的Java IO流详解
## 1. Java I/O的理解

> ***面试官：你说下对Java IO的理解？***

Java I/O有两个参与对象，一个是**I/O源端**，一个是想要和`I/O`源端通信的各种**接收端**，比如程序控制IDEA控制台输出、读取文件A写入文件B等，我们程序要保证的就是IO流的顺利**读取**和顺利写入。JDK把对Java IO的支持都放在了`package java.io`包下，南哥数了数，一个有86个类和接口。

我们看下`package java.io`包最常用的Reader和Writer接口，他们的作者都是Mark Reinhold。这位老哥是谁？[他](https://mreinhold.org/)是Oracle Java平台组的首席架构师，也是字符流读取器和写入器的首席工程师。这么有来头，看来Java I/O的程序设计不简单，我们可以从中学到不少好用的东西。

```java
/** 
 * @author      Mark Reinhold
 * @since       JDK1.1
 */
public abstract class Reader implements Readable, Closeable { }
public abstract class Writer implements Appendable, Closeable, Flushable { }
```

## 2. 输入流

### 2.1 字节输入流抽象基类

> ***面试官：那要怎么读取字节流？***

我们先讲输入流，后面再讲下输出流。输入流又分为字节流和字符流，顾名思义，字节流按字节来读取，操作的数据单元是8位的字节；而字符流按字符来读取，操作的数据单元是16位的字符。

读取字节的抽象基类是**InputStream**，这个基类提供了3个方法给我们来读取字节流。

1. 从输入流读取下一个数据字节，值字节以0到255范围内的`int`返回。

   ```java
   public abstract int read() throws IOException
   ```
2. 从输入流读取一定数量的字节并将它们存储到缓冲区数组`b`中。

   ```java
   public int read(byte b[]) throws IOException
   ```
3. 从输入流读取最多`len`个字节的数据到字节数组中。

   ```java
   public int read(byte b[], int off, int len) throws IOException
   ```
   

大家注意以上方法的返回参数都是int类型，当正常读取时，int返回的是读取的字节个数；而当int**返回-1**，就表明输入流到达了末尾。

### 2.2 字节输入流读取

> ***面试官：你说的这些不是实例，我要的是能真正读取的？***

上文的是抽象的接口，本身并不具备实际的功能。真正能够读取文件的是`InputStream`抽象基类的子类实现，例如**文件流FileInputStream**，有了他，我们读取音频、视频、gif等等都不是问题。

```java
// 文件流读取文件
FileInputStream stream = new FileInputStream(SOURCE_PATH);
```

我们还可以在外面加一层**缓存字节流**来提高读取效率，在外层套上BufferedInputStream对象，为什么可以提高读取效率我下文会讲到。

```java
BufferedInputStream stream = new BufferedInputStream(new FileInputStream(SOURCE_PATH));
```

以上通过字节流我们是以n个字节来读取的，如果要用`readLine()`读取某一行这种场景下就不适用了。我们可以把缓存字节流换成**缓存字符流**来承接，使用`InputStreamReader`**转换流**把字节输入流转换成字符输入流。

如下代码所示。

```java
BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_PATH)));
```

### 2.3 提高读取效率

> ***面试官：为什么加一层缓存流就能提高读取效率？***

为什么加一层缓存流就能提高读取效率？因为直接使用 `FileInputStream` 读取文件的话，每次调用 `read()` 都是从**磁盘读取一个字节**，而每次读取都是一次系统调用。**系统调用是操作系统层面的调用**，涉及到用户空间和内核空间之间的上下文切换，这些切换的成本是很昂贵的。

而如果使用缓存流，一次性从文件里读取多个字节到缓存中，减少系统调用同时也减少了磁盘读取，读取的效率明显提高了。

除了Java I/O采用缓存流来提高读取效率，大多应用程序也采用缓存来提升程序性能，例如我们后端在业务开发会使用Redis缓存来减少数据库压力。关于`为什么使用缓存来提高应用程序效率`，大家也可以看看国外[Quora](https://www.quora.com/Why-is-caching-used-to-increase-read-performance/answer/Gaive-Gandhi)的回答，解释得很详细。

### 2.4 字符输入流

> ***面试官：那字符流读取呢？***

字符输入流的抽象基类是`Reader`，同样是提供了3个方法来支持字符流读取。

1. 读取单个字符。

   ```java
   public int read() throws IOException
   ```
2. 将字符读入数组。

   ```java
   public int read(char cbuf[]) throws IOException
   ```
3. 将字符读入数组的一部分。

   ```java
   abstract public int read(char cbuf[], int off, int len) throws IOException
   ```

字符流读取的实例是`FileReader`，同样可以使用缓存字符流提高读取效率。

```java
BufferedReader reader = new BufferedReader(new FileReader(new File(SOURCE_PATH)));
```

我们来具体实操下，读取`C:\\Users\\Desktop\\JavaProGuide\\read`下的所有文件，把他们合并在一起，写入到`C:\\Users\\Desktop\\JavaProGuide\\write`下的`PRODUCT.txt`文件中。

```java
public class Client {

    private static final String PATH = "C:\\Users\\Desktop\\JavaProGuide\\read";

    private static final String FILE_OUT = "C:\\Users\\Desktop\\JavaProGuide\\write\\PRODUCT.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(PATH);
        File[] files = file.listFiles();

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_OUT));

        for (File curFile : files) {
            BufferedReader reader = new BufferedReader(new FileReader(curFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            reader.close();
        }

        writer.close();
    }

}
```

## 3. 输出流

### 3.1 输出流

> ***面试官：输出流你也讲一讲？***

字节输出流的抽象基类是`OutputStream`，字符输出流的抽象基类是`Writer`。他们分别提供了以下方法。

字节输出流`OutputStream`：

1. 将指定字节写入此输出流。

   ```java
   public abstract void write(int b) throws IOException
   ```
2. 将指定字节数组中的`b.length`字节写入此输出流。

   ```java
   public void write(byte b[]) throws IOException 
   ```
3. 将指定字节数组中从偏移量`off`开始的`len`个字节写入此输出流。

   ```java
   public void write(byte b[], int off, int len) throws IOException
   ```

字符输出流`Writer`：

1. 写入单个字符。

   ```java
   public void write(int c) throws IOException
   ```
2. 写入字符数组。

   ```java
   public void write(char cbuf[]) throws IOException
   ```
3. 写入字符数组的一部分。

   ```java
   abstract public void write(char cbuf[], int off, int len) throws IOException
   ```

另外字符输出流是使用**字符**来操作数据，所以可以用**字符串来代替字符数组**，JDK还支持以下**入参**是字符串的方法。

1. 写入一个字符串。

   ```java
   public void write(String str) throws IOException
   ```

2. 写入字符串的一部分。

   ```java
   public void write(String str, int off, int len) throws IOException
   ```

## 4. 字节流和字符流区别

> ***面试官：那字节流和字符流有什么区别？***

字节流和字符流的区别主要是三个方面。

- **基本单位不同**。字节流以字节（8位二进制数）为基本单位来处理数据，字符流以字符为单位处理数据。
- **使用场景不同**。字节流操作可以所有类型的数据，包括文本数据，和**非文本数据**如图片、音频等；而字符流只适用于处理文本数据。
- **关于性能方面**。因为字节流不处理字符编码，所以处理大量文本数据时可能不如字符流高效；而字符流使用到**内存缓冲区**处理文本数据可以优化读写操作。
# 四大集合之Queue
## 1. Queue集合

### 1.1 Queue集合概述

> ***面试官：你说说Queue集合都有什么常用类？***

JDK源码对Queue集合是这么解释的，大家看看。

> A collection designed for holding elements prior to processing. 
>
> 专为在处理之前保存元素而设计的集合。

南哥是这么理解的，List集合用于存储常用元素、Map集合用于存储具有映射关系的元素、Set集合用于存储唯一性的元素。Queue集合呢？所有的数据结构都是为了解决业务问题而生，而Queue集合这种数据结构能够存储具有先后时间关系的元素，很适用于在业务高峰期，需要缓存当前任务的业务场景。像Kafka、RabbitMQ、RocketMQ都是队列任务这种思想。

Queue集合底层接口提供的方法很简单，一共有 6 个。

```java
    // 添加元素。没有可用元素则简单返回false
    boolean offer(E e);
```

```java
    // 添加元素。没有可用元素则抛出IllegalStateException
    boolean add(E e);
```

```java
    // 移除队列的头部元素。如果此队列为空则返回null 。
    E poll();
```

```java
    // 移除队列的头部元素。该方法和poll不同之处在于，如果此队列为空，则抛出异常。
    E remove();
```

```java
    // 检索但不移除此队列的头部。如果此队列为空，则返回null 。
    E peek();
```

```java
    // 检索但不移除此队列的头部。该方法和peek唯一不同之处在于，如果此队列为空，则抛出异常
    E element();
```

Queue集合常用的实现类如下，我会一一讲来。包括双端队列的两个实现类：LinkedList、ArrayDeque，优先级队列：PriorityQueue，线程安全相关的Queue实现类：LinkedBlockingQueue、ArrayBlockingQueue、ConcurrentLinkedQueue。

![](https://i-blog.csdnimg.cn/direct/dacab50f96c34c67b94b394164882591.png)


### 1.2 双端队列

> ***面试官：双端队列的两个实现类知道吧？***

双端队列是Queue集合的一个子接口，顾名思义相比普通队列来说，双端队列可以往前、也可以往后顺序插入元素。比如我下面给出一段队列的初始化。

```java
        Queue<Integer> queue = new LinkedList<>();
        Deque<Integer> deque = new LinkedList<>();

        queue.add(1);
        deque.addLast(1);
        deque.addFirst(1);
```

同样是`new LinkedList<>()`来实例化队列，如果使用双端队列Deque接口，那这个队列就可以使用`addFirst`、`addLast`等双端队列特有的方法。

有南友就会问：那ArrayQueue呢？这两者都是双端队列Deque的底层实现，但底层数据结构不同，LinkedList底层数据结构是一个双向链表，看看它有前指针`next`、后指针`prev`。

```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

而ArrayDeque底层数据结构是一个Object类型的数组。

```java
    transient Object[] elements;
```

为什么要这么设计呢？其实这两种不同的设计就可以高效适用于不同的业务场景。双向链表实现的Deque随机查找的性能差，但插入、删除元素时性能非常出色，适用于修改操作更频繁的业务场景。

而数组实现的Deque，也就是ArrayDeque，它的插入、删除操作性能不如前者，但随机查找的性能非常出色，更适用于查询操作更频繁的业务场景。

### 1.3 优先级队列

> ***面试官：优先级队列有什么作用？***

优先级队列的实现类叫PriorityQueue，PriorityQueue虽然属于队列的一份子，不过它违背了队列最基本的原则：FIFO先进先出原则。它背叛了组织！

PriorityQueue的特性是它并不按常规队列一样顺序存储，而是根据元素的自然顺序进行排序，使用出队列的方法也是输出当前优先级最高的元素。例如以下代码输出的一样。

```java
    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();
        queue.offer(6);
        queue.offer(1);
        queue.offer(3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
```

```shell
# 执行结果
1
3
6
```

但如果我们直接打印PriorityQueue的所有元素，发现他其实并不是按元素的自然顺序进行存储。

```java
    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();
        queue.offer(6);
        queue.offer(1);
        queue.offer(3);

        System.out.println(queue);
    }
```

```shell
# 执行结果
[1, 6, 3]
```

why？其实PriorityQueue底层数据结构是一个**平衡二叉堆**：`transient Object[] queue`，如果你直接打印，打印的是堆里面的存储元素。

对于PriorityQueue来说，它只保证你使用`poll()`操作时，返回的是队列中最小、或最大的元素。

### 1.4 阻塞队列

> ***面试官：阻塞队列呢？***

JDK提供的阻塞队列接口为BlockingQueue，南哥先说说BlockingQueue的子类实现之一：ArrayBlockingQueue。

阻塞队列的特别之处在于当生产者线程会往队列放入元素时，如果队列已满，则生产者线程会进入阻塞状态；而当消费者线程往队列取出元素时，如果队列空了，则消费者线程会进入阻塞状态。

但队列的状态从满变为不满，消费者线程会唤醒生产者线程继续生产；队列的状态从空变为不空，生产者线程会唤醒消费者线程继续消费。

所以ArrayBlockingQueue很适合用于实现生产者、消费者场景。大家看看这个Demo。

```java
public class Test {
    public static void main(String[] args) {
        // 创建一个容量为 3 的ArrayBlockingQueue
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        // 创建并启动生产者线程
        Thread producerThread = new Thread(new Producer(queue));
        Thread consumerThread = new Thread(new Consumer(queue));
        producerThread.start();
        consumerThread.start();
    }
}

// 生产者类
class Producer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 6; i++) {
                System.out.println("生产者生产了: " + i);
                queue.put(i);
                Thread.sleep(150); // 模拟生产过程中的延迟
            }
            queue.put(-1); // 使用特殊值表示结束生产
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take();
                if (item == -1) {
                    break; // 遇到特殊值，退出循环
                }
                System.out.println("消费者消费了: " + item);
                Thread.sleep(100); // 模拟消费过程中的延迟
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

```shell
# 执行结果
生产者生产了: 1
消费者消费了: 1
生产者生产了: 2
消费者消费了: 2
生产者生产了: 3
消费者消费了: 3
生产者生产了: 4
消费者消费了: 4
生产者生产了: 5
消费者消费了: 5
生产者生产了: 6
消费者消费了: 6
```

LinkedBlockingQueue也是阻塞队列的实现之一，不过它和上面的ArrayBlockingQueue区别在于底层数据结构是由`双向链表`进行实现。

```java
    // LinkedBlockingQueue源码双向链表
    transient Node<E> head;
    private transient Node<E> last;
```
# 四大集合之Set
## 1. Set集合

### 1.1 HashSet

> ***面试官：你说说对HashSet的理解？***

Set集合区别于其他三大集合的重要特性就是元素具有唯一性，南友们记不住这个特性的话，有个易记的方法。Set集合为什么要叫Set呢？因为Set集合的命名取自于我们小学数学里的集合论（Set Theory），数学集合一个很重要的概念就是每个元素的值都互不相同。

Set集合常见的有实例有：HashSet、LinkedHashSet、TreeSet，南哥先缕一缕HashSet。

```java
// HashSet类源码
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {...}
```

HashSet底层实现其实是基于HashMap，HashMap的特点就是`Key`具有唯一性，这一点被HashSet利用了起来，每一个HashMap的`Key`对应的就是HashSet的`元素值`。来看看官方源码的解释。

> 此类实现Set接口，由哈希表（实际上是HashMap实例）支持。它不保证集合的迭代顺序；特别是，它不保证顺序随时间保持不变。此类允许null元素。

我们创建一个HashSet对象，实际上底层创建了一个HashMap对象。

```java
    // HashSet构造方法源码
    public HashSet() {
        map = new HashMap<>();
    }
```

HashSet一共提供了以下常用方法，不得不说HahSet在业务开发中还是用的没那么多的，南哥在框架源码上看HashSet用的就比较多，比如由Java语言实现的zookeeper框架源码。

（1）添加元素

```java
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```

我们看上面add方法的源码，是不是调用了HashMap的put方法呢？而put方法添加的Key是HashSet的值，Val则是一个空的Object对象。`PRESENT`是这么定义的。

```java
    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
```

（2）判断元素是否存在

```java
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
```

HashSet的contains方法同样是调用HashMap判断Key是否存在的方法：`containsKey`。

（3）移除元素

```java
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }
```

### 1.2 LinkedHashSet

> ***面试官：LinkedHashSet呢？***

接着轮到LinkedHashSet，同为Set集合之一，它和上文的HashSet有什么区别？南哥卖个关子。

源码对`LinkedHashSet`的解释。

> Hash table and linked list implementation of the Set interface, with predictable iteration order. This implementation differs from HashSet in that it maintains a doubly-linked list running through all of its entries. This linked list defines the iteration ordering, which is the order in which elements were inserted into the set (insertion-order). 

源码的大概意思就是：Set接口的哈希表和链表实现，具有可预测的迭代顺序。此实现与HashSet的不同之处在于，它维护一个贯穿其所有条目的**双向链表**。此链表定义迭代顺序，即**元素插入集合的顺序 **(插入顺序)。

底层数据结构是一条**双向链表**，每个元素通过指针进行相连，也就有了按插入顺序排序的功能。

知道了LinkedHashSet的特性，看看他的构造方法。

```java
    /**
     * 构造一个新的、空的链接哈希集，具有默认初始容量（16）和负载因子（0.75）。
     */
    public LinkedHashSet() {
        super(16, .75f, true);
    }
```

这个`super`方法向上调用了底层C语言源码实现的LinedHashMap的构造方法。LinkedHashMap的特点就是元素的排序是根据插入的顺序进行排序，那LinkedHashSet也就继承了这个特性。

```java
    // C语言源码
    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }
```

LinkedHashSet的常见方法和HashSet一样，同样是add()、contains()、remove()，这里我写个简单的Demo。

```java
    public static void main(String[] args) throws IOException {
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        set.add(1);
        System.out.println(set.contains(1));
        set.remove(1);
        System.out.println(set.contains(1));
    }
```

```shell
# 运行结果
true
false
```



### 1.3 TreeSet

> ***面试官：TreeSet和它们比有什么特性？***

轮到你了，TreeSet。我们南友们很好奇为什么他叫TreeSet？

因为他是基于TreeMap实现的。。。

但根本原因不是，TreeMap的底层是通过**红-黑树**数据结构来实现自然排序，那TreeSet也就继承了这个特性。

官方源码对TreeSet的解释：

> 基于TreeMap的NavigableSet实现。元素使用其**自然顺序**进行排序，或者根据使用的构造函数，使用创建集合时提供的Comparator进行排序。

源码解释告诉我们，TreeSet和HashSet、LinkedHashSet不同的特性在于，元素既不像HashSet一样无序，也不是像LinkedHashSet一样是以插入顺序来排序，它是根据元素的自然顺序来进行排序。

`b、c、a`这三个元素插入到TreeSet中，自然顺序就和字母表顺序一样是：`a、b、c`。

```java
    public static void main(String[] args) throws IOException {
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("b");
        treeSet.add("c");
        treeSet.add("a");
        System.out.println(treeSet);
    }
```

```shell
# 运行结果
[a, b, c]
```

TreeSet除了拥有以下的add()、contains()、remove()方法。

```java
    // 如果指定元素尚不存在，则将其添加到此集合中。
    public boolean add(E e) {
        return m.put(e, PRESENT)==null;
    }
```

```java
    // 如果此集合包含指定元素，则返回true 
    public boolean contains(Object o) {
        return m.containsKey(o);
    }
```

```java
    // 如果存在指定元素，则从此集合中移除该元素。
    public boolean remove(Object o) {
        return m.remove(o)==PRESENT;
    }
```

值得提出来的是，TreeSet还拥有first()、last()，可以方便我们提取出第一个、最后一个元素。

```java
    // 返回集合中的第一个元素。
    public E first() {
        return m.firstKey();
    }
```

```java
    // 返回集合中的最后一个元素。
    public E last() {
        return m.lastKey();
    }
```

### 1.4 TreeSet自定义排序

> ***面试官：那TreeSet要怎么定制排序？***

TreeSet的自定义排序我们要利用`Comparator`接口，通过向TreeSet传入自定义排序规则的`Comparator`来实现。

官方源码是这么解释的，南友们看一看。

```java
    // 构造一个新的空树集，根据指定的比较器进行排序。
    // 插入到集合中的所有元素都必须能够通过指定的比较器相互比较： comparator. compare(e1, e2)不得对集合中的任何元素e1和e2抛出ClassCastException 。
    // 如果用户尝试向集合中添加违反此约束的元素，则add调用将抛出ClassCastException 
    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }
```

传入`Comparator`接口时，我们还需要定义`compare`方法的游戏规则：如果`compare`方法比较两个元素的大小，返回正整数代表第一个元素 > 第二个元素、返回负整数代表第一个元素 < 第二个元素、返回0代表第一个元素 = 第二个元素。

下面我写了一个Demo，Comparator接口的规则是这样：人的岁数越小，那么他排名越靠前。

```java
public class JavaProGuideTest {
    public static void main(String[] args) {
        TreeSet set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                Person p1 = (Person)o1;
                Person p2 = (Person)o2;
                return (p1.age > p2.age) ? 1 : (p1.age < p2.age) ? -1 : 0;
            }
        });

        set.add(new Person(5));
        set.add(new Person(3));
        set.add(new Person(6));
        System.out.println(set);
    }

    @Data
    @AllArgsConstructor
    private static class Person {
        int age;
    }
}
```

```shell
# 执行结果
[JavaProGuideTest.Person(age=3), JavaProGuideTest.Person(age=5), JavaProGuideTest.Person(age=6)]
```
# 并发编程面试必备：synchronized原理、锁升级
## 1. 可重入锁

> ***面试官：知道可重入锁有哪些吗?***

可重入意味着获取锁的粒度是**线程**而不是**调用**，如果大家知道这个概念，会更容易理解可重入锁的作用。

既然获取锁的粒度是线程，意味着线程自己是可以获取自己的内部锁的，而如果获取锁的粒度是调用则每次经过同步代码块都需要重新获取锁。

举个例子。线程A获取了某个对象锁，但在线程代码的流程中仍需再次获取该对象锁，此时线程A可以继续执行不需要重新再获取该对象锁。另外线程如果要使用**父类**的同步方法，由于可重入锁也无需再次获取锁。

在Java中，可重入锁主要有ReentrantLock、**synchronized**。

## 2. synchronized实现原理

> ***面试官：你先说说synchronized的实现原理?***

synchronized的实现是基于monitor的。任何对象都有一个**monitor**与之关联，当monitor被持有后，对象就会处于**锁定状态**。而在同步代码块的**开始位置**，在编译期间会被插入**monitorenter指令**。

当线程执行到monitorenter指令时，就会尝试获取**monitor**的所有权，如果获取得到则代表获得锁资源。

### 2.1 synchronized缺点

> ***面试官：那synchronized有什么缺点？***

在Java SE 1.6还没有对synchronized进行了各种优化前，很多人都会称synchronized为**重量级锁**，因为它对资源消耗是比较大的。

1. synchronized需要频繁的**获得锁、释放锁**，这会带来了不少性能消耗。

2. 另外没有获得锁的线程会被**操作系统**进行挂起阻塞、唤醒。而**唤醒操作**需要保存当前线程状态，切换到下一个线程，也就是进行**上下文切换**。上下文切换是很耗费资源的一种操作。


### 2.2 保存线程状态

> ***面试官：为什么上下文切换要保存当前线程状态？***

这就跟读英文课文时查字典一样，我们要先记住课文里的**页数**，查完字典好根据页数翻到英文课文原来的位置。

同理，CPU要保证可以**切换**到上一个线程的状态，就需要保存当前线程的状态。

### 2.3 锁升级

> ***面试官：可以怎么解决synchronized资源消耗吗？***

上文我有提到Java SE 1.6对synchronized进行了各种优化，具体的实现是给synchronized引入了**锁升级**的概念。synchronized锁一共有四种状态，级别从低到高依次是无锁、偏向锁、轻量级锁、重量级锁。

大家思考下，其实多线程环境有着各种不同的场景，同一个锁状态并不能够适应所有的业务场景。而这四种锁状态就是为了**适应各种不同场景**来使得线程并发的效率最高。

1. 没有任何线程访问同步代码块，此时synchronized是**无锁**状态。

2. 只有**一个线程**访问同步代码块的场景的话，会进入**偏向锁**状态。偏向锁顾名思义会**偏向访问它的线程**，使其加锁、解锁不需要额外的消耗。

3. 有**少量线程竞争**的场景的话，偏向锁会升级为**轻量级锁**。而轻量级采用**CAS操作**来获得锁，CAS操作不需要获得锁、释放锁，减少了像synchronized重量级锁带来的**上下文切换**资源消耗。

4. 轻量级锁通过CAS自旋来获得锁，如果自旋10次失败，为了减少CPU的消耗则锁会膨胀为**重量级锁**。此时synchronized重量级锁就回归到了悲观锁的状态，其他获取不到锁的都会进入阻塞状态。

### 2.4 锁升级优缺点

> ***面试官：它们都有什么优缺点呢？***

由于每个锁状态都有其不同的优缺点，也意味着有其不同的适应场景。

1. **偏向锁**的优点是加锁和解锁操作不需要额外的消耗；缺点是如果线程之间存在锁竞争，偏向锁会撤销，这也带来额外的撤销消耗；所以偏向锁适用的是只有一个线程的业务场景。
2. **轻量级锁**状态下，优点是线程不会阻塞，提高了程序执行效率；但如果始终获取不到锁的线程会进行**自旋**，而自旋动作是需要消耗**CPU**的；所以轻量级锁适用的是追求响应时间、同时同步代码块执行速度快的业务场景。
3. **重量级锁**的优点是不需要自旋消耗CPU；但缺点很明显，线程会阻塞、响应时间也慢；重量级锁更适用在同步代码块执行速度较长的业务场景。
# 并发编程面试必备：ThreadLocal作用、线程生命周期
## 1. 线程通信

### 1.1 线程的等待/通知机制

> ***面试官：Java线程的等待/通知机制知道吧？***

Java线程的等待/通知机制指的是：线程A获得了synchronized同步方法、同步方法块的锁资源后，调用了锁对象的wait()方法，释放锁的同时进入**等待状态**；而线程B获得锁资源后，再通过锁对象的notify()或notifyAll()方法来**通知**线程A恢复执行逻辑。

其实Java的所有对象都拥有等待/通知机制的本领，大家可以在JDK源码package java.lang`下找到Java.lang.Object里提供的五个与等待/通知机制相关的方法。

一、等待。

（1）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法。

```java
    public final void wait() throws InterruptedException {
        wait(0);
```

（2）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法，或者指定的毫秒timeout过去。

```java
    public final native void wait(long timeout) throws InterruptedException;
```

（3）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法，或者指定的毫秒timeout过去，另外nanos是额外时间，以纳秒为单位。

```java
    public final void wait(long timeout, int nanos) throws InterruptedException {
    }
```

所以其实wait()、watit(0)、watit(0, 0)执行后都是**同样的效果**。

二、通知。

（1）唤醒在此对象监视器上等待的单个线程。

```java
    public final native void notify();
```

（2）唤醒在此对象监视器上等待的所有线程。

```java
    public final native void notifyAll();
```

大家有没听说过**消费者生产者问题**呢？消费者生产者之间要无限循环生产和消费物品，解决之道就是两者形成完美的等待、通知机制。而这套机制就可以通过上文的wait、notify方法来实现。

### 1.2 线程通信方式

> ***面试官：还有没有其他线程通信方式？***

（1）利用Condition进行线程通信。

如果大家的程序直接采用的是**Lock对象**来同步，则没有了上文synchronized锁带来的隐式同步器，也就无法使用wait()、notify()方法。

此时的线程可以使用Condition对象来进行通信。例如下文的示例代码： condition0的await()阻塞当前线程，同时释放、等待获取锁资源；接着等待其他线程调用condition0的signal()来通知其获取锁资源继续执行。

```java
@Slf4j
public class UseReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Condition condition0 = lock.newCondition();

    private static final Condition condition1 = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                lock.lock();

                for (int i = 1; i < 4; i++) {
                    log.info(i + "");

                    condition1.signal();
                    condition0.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.lock();

                for (int i = 65; i < 68; i++) {
                    log.info((char) i + "");

                    condition0.signal();
                    condition1.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
```

```sh
# 程序执行结果
2024-05-30 10:30:30[ INFO ]1
2024-05-30 10:30:30[ INFO ]A
2024-05-30 10:30:30[ INFO ]2
2024-05-30 10:30:30[ INFO ]B
2024-05-30 10:30:30[ INFO ]3
2024-05-30 10:30:30[ INFO ]C
```

（2）Thread采用join方法进行通信。

线程Thread对象还提供了join方法，也是一种通信的方式。当某个程序的执行流调用了某个thread对象的join方法，调用线程将会被阻塞，等到thread对象终止后才通知调用线程继续执行。

```java
    public final void join() throws InterruptedException {
        join(0);
    }
```

（3）volatile共享变量。

volatile的出现，大家是不是有些意外呢？虽然volatile适用的多线程场景不多，但它也是线程通信的一种方式。被volatile修饰的变量如果更新了值，则会通过**主内存**这条消息总线通知所有使用该变量的线程，让其把主内存同步到**工作内存**里，则所有线程都会获取共享变量最新值。

### 1.3 更加灵活的ReentrantLock

> ***面试官：你说的Lock对象说下你的理解？***

在线程同步上，JDK的Lock接口提供了多个实现子类，如下所示。下面我按面试官**面试频率高**的`ReentrantLock`来讲解。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ee4a152a01fb2e135010c76999881edd.png)


ReentrantLock相比synchronized来说使用锁更加灵活，可以自由进行加锁、释放锁。ReentrantLock类提供了lock()、unlock()来实现以上操作。具体实操代码可以看上一个面试官问题关于Condition的示例代码。

```java
// ReentrantLock源码
package java.util.concurrent.locks;
public class ReentrantLock implements Lock, java.io.Serializable {
    
    // 获取锁
    public void lock() {
        sync.lock();
    }
    
    // 尝试释放此锁
    public void unlock() {
        sync.release(1);
    }
}
```

另外ReentrantLock和synchronized都是**可重入锁**，即线程获取锁资源后，下一步如果进入**相同锁资源**的同步代码块，不需要再获取锁。

ReentrantLock也可以实现**公平锁**，即成功获取锁的顺序与申请锁资源的顺序一致。我们在创建对象时进行初始化设置就可以设置为公平锁。

```java
 ReentrantLock lock = new ReentrantLock(true);
```

## 2. ThreadLocal作用

> ***面试官：ThreadLocal知道吧？***

上文我们讨论的都是在多个线程**对共享资源进行通信**的业务场景上，例如商城业务秒杀的库存要保证数据安全性。而如果在多个线程**对共享资源进行线程隔离**的业务场景上，则可以使用ThreadLoccal来解决。

ThreadLocal可以保存当前线程的副本值，提供了set、get方法，通过set方法可以把指定值设置到当前线程副本；而通过get方法可以返回此当前线程副本中的值。

例如要实现一个功能，每个线程打印`当前局部变量:局部变量 + 10`，我们就可以利用ThreadLocal保存共享变量i，来避免对变量i的共享冲突。

```java
public class UseThreadLocal {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 3; i++) {
            int number = i;
            es.execute(() -> System.out.println(number + ":" + new intUtil().addTen(number)));
        }
    }

    private static class intUtil {
        public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>(); // 使用threadLocal保存线程保存的当前共享变量num

        public static int addTen(int number) {
            threadLocal.set(number);

            try { // 休息1秒
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return threadLocal.get() + 10;
        }
    }
}
```

```sh
# 程序执行结果
0:10
2:12
1:11
```

## 3. 线程生命周期

> ***面试官：那线程生命周期都有什么？***

在校招笔试、或面试中，这道面试题还是比较常见的，大家简单记忆下就可以。

1. 初始状态。创建了线程对象还没有调用start()。

2. 就绪或运行状态。执行了start()可能运行，也可能进入就绪状态在等待CPU资源。

3. 阻塞状态 。一直没有获得锁。

4. 等待状态。等待其他线程的通知唤醒。

5. 超时状态。

6. 终止状态。
# 并发编程面试必备：如何创建线程池、线程池拒绝策略
## 1. 线程池使用

### 1.1 如何配置线程池大小

> ***面试官：你说下线程池的大小要怎么配置？***

这个问题要看业务系统执行的任务更多的是计算密集型任务，还是I/O密集型任务。大家可以从这**两个方面**来回答面试官。

（1）如果是**计算密集型任务**，通常情况下，CPU个数为N，设置N + 1个线程数量能够实现最优的资源利用率。因为N + 1个线程能保证**至少有N个**线程在利用CPU，提高了CPU利用率；同时不设置过多的线程也能减少线程状态切换所带来的上下文切换消耗。

（2）如果是**I/O密集型任务**，线程的主要等待时间是花在等待I/O操作上，另外就是计算所花费的时间。一般可以根据这个公式得出线程池合适的大小配置。
$$
线程池大小 = CPU数量 * CPU期望的利用率 * (1 + IO操作等待时间/CPU计算时间)
$$

### 1.2 创建线程池

> ***面试官：那线程池怎么创建？***

可以使用ThreadPoolExecutor自定义创建线程池，这也是创建线程池**推荐**的创建方式。

```java
public ThreadPoolExecutor(int corePoolSize, // 要保留在池中的线程数
                          int maximumPoolSize, // 池中允许的最大线程数
                          long keepAliveTime, // 当线程数大于corePoolSize时，多余的空闲线程在终止之前等待新任务的最长时间
                          TimeUnit unit, // 时间单位
                          BlockingQueue<Runnable> workQueue, // 在执行任务之前用于保存任务的队列
                          ThreadFactory threadFactory) { // 执行程序创建新线程时使用的工厂
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         threadFactory, defaultHandler);
}
```

另外Executors类也提供了一些**静态工厂方法**，可以用来创建一些预配置的线程池。

newFixedThreadPool可以设置线程池的固定线程数量。

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());
}
```

newSingleThreadExecutor可以让线程**按序执行**，适用于需要确保所有任务按序执行的场景。

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```

大家看下以下源码，newCachedThreadPool的线程数**没有上限限制**，同时空闲线程的存活时间是**60秒**。newCachedThreadPool更适合系统负载不太高、线程执行时间短的场景下，因为线程任务不需要经过排队，直接交给空闲线程就可以。

```java
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}
```

newScheduledThreadPool可以安排任务在给定的**延迟**后运行，或者定期执行。

```java
public static ScheduledExecutorService newScheduledThreadPool(
        int corePoolSize, ThreadFactory threadFactory) {
    return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
}
```



### 1.3 预配置线程池弊端

> ***面试官：你说的这些预配置线程池会有什么问题？***

小伙伴要记得上述静态工厂方法在使用过程中可能会出现**OOM内存溢出**的情况。

1. `newFixedThreadPool`、`newSingleThreadExecutor`：因为线程池指定的请求队列类型是链表队列`LinkedBlockingQueue<Runnable>()`，故允许的请求队列长度是无上限的，可能会出现OOM内存溢出。
2. `newCachedThreadPool`、`newScheduledThreadPool`：线程池指定的线程数上限是Integer.MAX_VALUE，故允许创建的线程数量是无上限的Integer.MAX_VALUE，可能会出现OOM内存溢出。

### 1.3 Spring创建线程池

> ***面试官：你们项目线程池用的这种创建方式？***

一般Spring工程创建线程池不直接使用ThreadPoolExecutor。

Spring框架提供了以**Bean形式**来配置线程池的`ThreadPoolTaskExecutor`类，ThreadPoolExecutor类的底层实现还是基于JDK的ThreadPoolExecutor。

```java
# 示例代码
@Bean(name = "testExecutor")
public ThreadPoolTaskExecutor testExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 配置核心线程数
    executor.setCorePoolSize();
    // 配置最大线程数
    executor.setMaxPoolSize();
    // 配置队列大小
    executor.setQueueCapacity();
    executor.initialize();
    return executor;
}
```

## 2. 线程池拒绝策略

> ***面试官：线程池请求队列满了，有新的请求进来怎么办？***

大家如果有看ThreadPoolExecutor源码就知道，ThreadPoolExecutor类实现了`setRejectedExecutionHandler`方法，顾名思义意思是设置拒绝执行处理程序。

```java
# ThreadPoolExecutor源码
/**
* Sets a new handler for unexecutable tasks. // 为无法执行的任务设置新的处理程序
*
* @param handler the new handler
* @throws NullPointerException if handler is null
* @see #getRejectedExecutionHandler
*/
public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
    if (handler == null)
        throw new NullPointerException();
    this.handler = handler;
}
```

该方法可以为线程池设置**拒绝策略**，目前JDK8一共有四种拒绝策略，也对应入参RejectedExecutionHandler的四种子类实现。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/29561a96e3f9705be7eff027876e00fc.png)


1. AbortPolicy：**默认**的拒绝策略，直接抛出RejectedExecutionException异常。
2. DiscardPolicy：直接丢弃被拒绝的任务
3. CallerRunsPolicy：直接在execute方法的**调用线程**中运行被拒绝的任务。。
4. DiscardOldestPolicy：丢弃最旧的未处理请求，然后重试execute 。

另外如果线程池拒绝策略设置为DiscardOldestPolicy，线程池的请求队列类型最好不要设置为优先级队列**PriorityBlockingQueue**。因为该拒绝策略是丢弃**最旧的请求**，也就意味着丢弃**优先级最高的请求**。

## 3. 线程工厂的作用

> ***面试官：线程池的入参ThreadFactory有什么作用吗？***

ThreadFactory定义了创建线程的工厂，回答这个问题我们就要结合实际场景了。

ThreadFactory线程工厂能够为线程池里每个线程**设置名称**、同时设置**自定义异常的处理逻辑**，可以方便我们通过日志来定位bug的位置。

以下是一个代码示例。

```java
@Slf4j
public class CustomGlobalException {
    public static void main(String[] args) {
        ThreadFactory factory = r -> {
            String threadName = "线程名称";
            Thread thread = new Thread(r, threadName);
            thread.setUncaughtExceptionHandler((t, e) -> {
                log.error("{}执行了自定义异常日志", threadName);
            });
            return thread;
        };
        ExecutorService executor = new ThreadPoolExecutor(6,
                6,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(66),
                factory);

        executor.execute(() -> {
            throw new NullPointerException();
        });
        executor.shutdown();
    }
}

控制台打印：2024-04-26 22:04:45[ ERROR ]线程名称执行了自定义异常日志
```
# 并发编程面试必备：指令重排序、volatile可见性原理及局限性
## 1. 指令重排序

### 1.1 重排序是什么

> ***面试官：重排序知道吧？***

指令重排序字面上听起来很高级，但只要理解了并不难掌握。我们先来看看指令重排序究竟有什么作用。

指令重排序的主要作用是可以优化编译器和处理器的执行效率，提高程序性能。例如多条执行顺序不同的指令，可以重排序让**轻耗时的指令**先执行，从而让出CPU流水线资源供其他指令使用。

但如果指令之间存在着**数据依赖**关系，则编译器和处理器不会对相关操作进行指令重排序，避免程序执行结果改变。这个规则也称为`as-if-serial语义`。例如以下代码。

```java
String book = "JavaGetOffer"; // A
String avator = "思考的陈"; // B
String msg = book + abator; // C
```

对于A、B，它们之间并没有依赖关系，谁先执行对程序的结果没有任何影响。但C却依赖于A、B，不能出现类似C -> A -> B或C -> B -> A或A -> C -> B或B -> C -> A之类的指令重排，否则程序执行结果将改变。

### 1.2 重排序的问题

> ***面试官：那重排序不会有什么问题吗？***

在单线程环境下，有`as-if-serial语义`的保护，我们无需担心程序执行结果被改变。但在多线程环境下，指令重排序会出现**数据不一致**的问题。举个多线程的例子方便大家理解。

```java
       int number = 0;
       boolean flag = false;
       public void method1() {
           number = 6;                // A
           flag = true;               // B
       }
       public void method2() {
           if (flag) {               // C
               int i = number * 6;   // D
           }
       }
```

假如现在有两个线程，线程1执行`method1`、线程2执行`method2`。因为`method1`其中的A、B之间没有数据依赖关系，可能出现B -> A的指令重排序，大家注意这个指令重排序会影响到线程2执行的结果。

当B指令执行后A指令还没有执行`number = 6`，此时如果线程2执行`method2`同时给i赋值为`0 * 6`。很明显程序运行结果和我们预期的并不一致。

## 2. volatile

### 2.1 volatile特性

> ***面试官：有什么办法可以解决？***

关于上文的重排序问题，可以使用volatile关键字来解决。volatile一共有以下特性：

1. **可见性**。volatile修饰的变量每次被修改后的值，对于任何线程都是可见的，即任何线程会读取到最后写入的变量值。
2. **原子性**。volatile变量的读写具有原子性。
3. **禁止代码重排序**。对于volatile变量操作的相关代码不允许重排序。

```java
       int number = 0;
       volatile boolean flag = false;
       public void method1() {
           number = 6;                // A
           flag = true;               // B
       }
       public void method2() {
           if (flag) {               // C
               int i = number * 6;   // D
           }
       }
```

由于volatile具有禁止代码重排序的特性，所以不会出现上文的**B -> A的指令重排序**。另外volatile具有可见性，falg的修改对线程2来说是可见的，线程会立刻感知到`flag = ture`从而执行对i的赋值。以上问题可以通过volatile解决，和使用synchronized加锁是一样的效果。

另外大家注意一点，volatile的原子性指的是对volatile的读、写操作的原子性，但类似于`volatile++`这种**复合操作**是没有原子性的。

### 2.2 可见性原理

> ***面试官：那volatile可见性的原理是什么？***

内存一共分为两种，线程的本地内存和线程外的主内存。对于一个volatile修饰的变量，任何线程对该变量的修改都会同步到**主内存**。而当读一个volatile修饰的变量时，JMM（Java Memory Model）会把该线程对应的**本地内存**置为无效，从而线程读取变量时读取的是主内存。

线程每次读操作都是读取主内存中最新的数据，所以volatile能够实现**可见性**的特性。

### 2.3 volatile局限性

> ***面试官：volatile有什么缺点吗？***

企业生产上还是比较少用到volatile的，对于加锁操作会使用的更多些。

1. synchronized加锁操作虽然开销比volatile大，但却适合复杂的业务场景。而volatile只适用于**状态独立**的场景，例如上文对flag变量的读写。
2. volatile编写的代码是比较难以理解的，不清楚整个流程和原理很难维护代码。
3. 类似于`volatile++`这种**复合操作**，volatile不能确保原子性。
# 掌握HashMap底层原理、HashMap为什么线程不安全
## 1. HashMap内部结构

> ***面试官：你说下HashMap的内部结构？***

好的面试官。

HashMap内部存储数据的对象是一个实现Entry接口的**Node数组**，也称为哈希桶`transient Node<K,V>[] table`，后面我们称Node数组为Entry数组。Entry数组初始的大小是**16**。

Node节点的内部属性key、value分别代表键和值，hash代表key的hash值，而next则是指向下一个链表节点的指针。

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

### 1.1 键值的添加流程

> ***面试官：那一个键值是怎么存储到HashMap的？***

首先会调用hash方法**计算key的hash值**，通过key的hashCode值与key的hashCode高16位进行异或运算，使hash值更加随机与均匀。

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

再通过该hash值与Entry数组的长度**相与**，得到要存储到的索引位置`int index = (table.length - 1) & hash`。如果该索引位置是空的，会把键值直接添加到表头，如果哈希冲突了则会用**链表法**形成一条链表。

数据添加后，会判断当前容量是否到达了threshold阈值，threshold等于负载因子`loadFactor * table.length`。负载因子默认是**0.75**，threshold第一次扩容时为0.75 * 16 = **12**。

如果到达阈值了则会对Entry数组进行扩容，扩容成为原来**两倍容量**的Entry数组。

### 1.2 红黑树

> ***面试官：HashMap链表还会转换成什么？***

当链表长度 >= 8时，会把链表转换为**红黑树**。

是这样的，HashMap的链表元素如果数量过多，查询效率会越来越低，所以需要将链表转换为其他数据结构。而二叉搜索树这种数据结构是绝对的子树平衡，左节点比父节点小，右节点比父节点大，在极端情况会退化为**链表结构**。

而红黑树放弃了绝对的子树平衡，转而追求的是一种大致平衡，在极端情况下的数据查询效率更优。

```java
# 红黑树数据结构
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;
}
```

## 2. 线程安全的Map

### 2.1 线程不安全的HashMap

> ***面试官：HashMap为什么线程不安全？***

一、在多线程环境下，可能会出现**数据覆盖**的问题。

例如前面提到如果索引位置为空则直接添加到表头，如下面源码所示。此时如果有两个线程同时进入if语句，线程A把数据插入到表头，接着线程B把他的数据覆盖到表头，这样就产生了数据覆盖的问题，线程A的数据相当于消失了。

```java
if ((p = tab[i = (n - 1) & hash]) == null)
	tab[i] = newNode(hash, key, value, null);
```

二、另外在多线程环境下，还可能会出现**数据不一致**的问题。

在插入数据后，判断是否需要扩容是以下源码。

```java
if (++size > threshold)
	resize();
```

若两个线程同时进入了++size代码块，对size的修改分为三个步骤：读取、计算、赋值。线程A、线程B同时读取了size是0，两者计算时size都为1，后面赋值时把size = 1赋值给了size两次。

但实际上期望的size应该是2，此时就出现了数据不一致的问题，Entry数组的容量会出现错误。

### 2.2 线程安全的ConcurrentHashMap

> ***面试官：有线程安全的Map吗？***

有的，JDK提供了线程安全的ConcurrentHashMap。

（1）ConcurrentHashMap对于底层Entry数组、size容量都添加了可见性的修饰，保证了其他线程能实时监听到该值的**最新修改**。

```java
transient volatile Node<K,V>[] table;
private transient volatile int sizeCtl;
```

（2）在添加键值的操作，对**元素级别**进行加锁。若该索引位置不存在元素，则使用乐观锁CAS操作来添加值，而CAS是原子操作，不怕多线程的干扰。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d628abadbd05b72922c24f36f26026ab.png)


若该索引位置存在元素，则使用**synchronized**对该索引位置的**头节点**进行加锁操作，保证**整条链表**同一时刻只有一个线程在进行操作。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/850b5e88feabd1b1de40fbf7874a3147.png)


（3）另外在JDK7版本中ConcurrentHashMap的实现和JDK8不同。

JDK7版本的数据结构是大数组Segment + 小数组HashEntry，其中小数组HashEntry的**每个元素**是一条链表，**一个Segment**是一个HashEntry数组。对每个Segment即每个分段，使用ReentrantLock进行加锁操作。

可以看到JDK8版本相比JDK版本的实现**锁粒度**更小，且JDK8版本的链表还可以升级为查询效率高的红黑树，所以JDK7版本的ConcurrentHashMap目前被JDK8版本的代替了。

### 2.3 HashTable和ConcurrentHashMap区别

> ***面试官：HashTable和ConcurrentHashMap有什么区别吗？***

HashTable也是线程安全的Map，不过它不仅对修改操作添加加锁操作，获取操作也进行了加锁。

```java
public synchronized V put(K key, V value)
public synchronized V get(Object key)
```

而ConcurrentHashMap没有对get进行加锁处理，不适用于**强一致性**的场景。例如要求获取操作必须严格获取到最新的值，这种强一致性场景则更适合使用HashTable。

另外HashTable和HashMap、ConcurrentHashMap还有以下不同。

1. HashTable继承了Dictionary，而HashMap、ConcurrentHashMap继承了AbstractMap
2. HashTable初始容量为11，HashMap、ConcurrentHashMap是16
3. HashTable扩容为原来的**2n+1**，HashMap、ConcurrentHashMap是扩容为原来的**2n**
# 掌握Kafka高水位，Kafka如何保证消息可靠性
## 1. Kafka高水位

> ***面试官：知道Kafka高水位吗？***

我们都知道Kafka消息保存在首领分区和分区副本中，Kafka要保证即使从分区副本读取消息也只会读取**已提交**的消息。Kafka的高水位就是为了**这个目标**而开发出来的。

如果大家对消息已提交的概念不清楚的话，可以看下以下的解释。

> Kafka的消息只有在**所有分区副本**都同步该消息后，才算是**已提交**的消息

在分区复制的过程中，首领分区会在发送的数据里加入当前高水位。当前高水位就是复制偏移量，记录了当前**已提交消息的最大偏移量**。而分区副本就可以根据**首领分区副本**提供的高水位，来避免**未提交**的消息被消费者消费。

就如下图，**最大偏移量**的限制就像海面上的水位。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/c72ee82cb79e466ebaa36518b780b4e5.png)


## 2. Kafka消息可靠性

### 2.1 消息存储可靠性

> ***面试官：你说说Kafka是怎么保证消息可靠性的？***

大家在回答面试官问题前可以思考下，**可靠性的含义**是什么？

在业务系统中，消息的不丢失是最重要的，数据即是金钱。如果把客户的一条支付消息丢失，而这条支付信息的涉及的金额不菲，想想对公司的损失有多大。所以可靠性意味着对消息的存储和保护。

Kafka在这方面采用了复制机制和分区多副本架构来作为消息可靠性的核心。

（1）分区多副本架构。

Kafka的所有主题被分为了**多个分区**存储在多个Broker里，而每个分区可以有**多个副本**。例如有4个Broker节点，Broker1存储了分区首领副本，而Broker2、Broker3可以存储其分区副本。

Kafka对消息的存储有多个分区副本来支持，可以避免单点问题导致数据丢失找不回来的情况。

（2）复制机制。

在通常情况下消费者都是从**首领副本**里读取消息，同时会有n（**复制系数**）个Broker机器会去**同步复制**首领副本后，生成**跟随者副本**也就是分区副本。

如果首领副本的机器挂了，分区副本就会选举成为**新的首领副本**。

复制机制保证了分区副本和首领副本的数据一致性，有复制机制的加持，分区多副本架构才是可用的。

### 2.2 生产者消费者可靠性

> ***面试官：还有呢？***

上面所说的其实是基于Broker层面带给Kafka的可靠性保障，我们还需要在生产者、消费者层面下功夫，来使整个系统减少丢失数据的风险。

一、在生产者方面。

Kafka提供了多种**发送确认模式**，我们可以根据业务的可靠性需求配置合适的acks。

1. ack = 0。如果消息生产者能够把消息通过网络发送出去，则认为消息已成功写入。
2. ack = 1。如果首领分区收到消息并成功写入，生产者收到**确认返回**，则认为消息已成功写入。
3. ack = all。只有在消息成功**写入所有分区副本**后，才认为消息已成功写入。这保证了消息的多备份。

以上的各种acks情况如果失败的话，我们可以让生产者继续**重试**发送消息，直到Kafka返回成功。

二、在消费者方面

大家如果能回答上文第一个面试官问题：`知道Kafka高水位吗`，就知道Kafka高水位保证了消费者只会读取到已提交的数据，即被写入所有分区副本的数据。所以消费者要确保的是跟踪哪些数据已读取了、哪些数据未读取。

1. 消费者消费消息时会先获取一批消息，同时从最后一个偏移量开始读取，这保证了**消息的顺序性**。
2. 消费者消费消息后会同步提交、异步**提交偏移量**，保证了消息不被其他消费者**重复消费**。

### 2.3 消费堆积问题

> ***面试官：那要是Kafka消费堆积了你怎么处理？***

这个问题是面试官常考的一个问题，我们要从Broker和消费者两方面来看。

一、Broker的话。
1. 每个topic是分为多个分区给不同Broker进行处理，要**合理分配分区数量**来提高Broker的消息处理能力。比如3个Broker2个分区，可以改为3个Broker3个分区。
2. 可以**横向扩展**Broker集群，来提高Broker的消息处理能力。

二、消费者的话。
1. 可以增加消费者服务数量来提高消息消费能力。
2. 在提交偏移量时，可以把同步提交改为**异步提交**。异步提交无需等待Kafka的确认返回，减少了同步等待Broker的时间。

## 3. Kafka控制器

> ***面试官：知道Kafka控制器吧？***

Kafka控制器其实也是一个Broker，不过它还负责选举**分区首领**。Kafka的控制器和Redis集群的哨兵的选举功能是一样的。

也就是在首领副本所在的分区失效后，Kafka会通过控制器来在分区副本里选举出新的**首领副本**。
# 掌握MySQL高级特性：分区表、视图、全文索引
## 1. MySQL高级特性

### 1.1 分区表

分区的一个主要目的是将数据按照一个较粗的粒度**分在不同的区域**，这样的话就有很多好处。

1. 在执行查询的时候，优化器会根据分区定义过滤不需要查询的分区，这样的话就**不需要扫描所有数据**
2. 可以把数据分布在**不同的物理设备**上，高效利用多个硬件设备
3. 在表非常大且业务**热点数据**是最新表数据的情况下，根据时间进行分区可以快速过滤掉大量无关的历史数据

### 1.2 分区表的缺点

1. 分区表是根据**列进行分区**的话，查询那些和分区列无关的数据，需要扫描所有分区表
2. 分区列和SQL的**索引列不匹配**，也需要扫描所有分区表
3. 当对分区表增删改查时，MySQL需要**打开并锁住**所有的底层表，这是分区表的另一个开销

```sql
# 创建表时同时设置分区
CREATE TABLE sales (
    order_date DATETIME NOT NULL,
    -- Other columns omitted
) ENGINE=InnODB PARTITION BY RANGE(YEAR(order_date)) (
    PARTITION P2010 VALUES LESS THAN(2010),
    PARTITION P2011 VALUES LESS THAN(2011),
    PARTITION P2012 VALUES LESS THAN(2012),
    PARTITION pCatchall VALUES LESS THAN MAXVALUE);
```

## 2. 视图

MySQL视图本身是一个虚拟表，不存放任何数据，其实就相当于保存了一条Select语句，把这条Select语句封装成视图。

我举个例子吧。在业务开发中，如果不得不改变MySQL表名，而不想改动代码的表名。可以用视图查询新表名的内容，然后把视图命名为旧表名，这样查询视图也能查询出数据。

```sql
CREATE VIEW 新表名 AS
	SELECT * FROM 旧表名
```

## 3. 其他高级特性

MySQL高级特性还包括了存储过程、触发器和事件。

1. 存储过程其实就是在MySQL里写**方法函数**

   > 例如可以让MySQL执行函数来插入1万条数据

2. 触发器可以让你在SQL语句**操作表数据**的时候，在SQL语句执行前、执行后触发一些特定操作

   > 例如可以编写触发器，在插入A表数据时，给日志记录B表插入一条日志

3. 事件类似于**Linux的定时任务**，可以是在某个时候、每隔一个时间间隔执行一段SQL代码。

   > 例如可以创建一个事件每隔一段时间调用下我们定义的一个**存储过程**

### 3.1 全文索引

MySQL全文索引类似于ElasticSearch的全文索引。

主要是针对文本内容这种格式的数据，MySQL全文索引会对字段进行分词处理，返回匹配相关的文本内容。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/191cc611991a44feb0c937d751de3107.png#pic_center)
# 掌握NIO是什么？零拷贝的概念
## 1. Java NIO

### 1.1 NIO介绍

NIO的出现在于提高`IO`的速度，它相比传统的输入/输出流速度更快。NIO通过管道`Channel`和缓冲器`Buffer`来处理数据，可以把管道当成一个矿藏，缓冲器就是矿藏里的卡车。

程序通过管道里的缓冲器进行数据交互，而**不直接处理数据**。程序要么从缓冲器获取数据，要么输入数据到缓冲器。

### 1.2 通道和缓冲器

NIO提供了通道和缓冲器这两个核心对象。

（1）管道`Channel`：

与传统的`IO`流只能只读或只写的**单向流**不同，NIO通道是**双向的**，也就是说读写操作可以同时进行，使得数据的处理效率也更高。

（2）缓冲器`Buffer`：

传统的输入/输出流一次只处理一个字节，而每一次字节读取都是一次系统调用，涉及到用户空间和内核空间之间的上下文切换，通常来说效率不高。

而`NIO`采用内存映射文件方式来处理输入/输出，Channel通过`map()`方法把**一块数据**映射到内存中。程序通过`Buffer`进行数据交互，减少了与原始数据源的直接访问。NIO**面向块**的处理方式使得效率更高。

### 1.3 非阻塞IO模型

传统的输入/输出流是同步阻塞`IO`模型，如果数据源没有数据了，此时程序将进行阻塞。

而`NIO`是I/O多路复用模型，线程可以**询问**通道有没可用的数据，而不需要在没有数据时阻塞掉线程。

### 1.4 字符流处理字符吗

所有数据包括文本数据最终都是以**字节形式**存储的，因为计算机底层只能理解二进制数据。

字符最终也是要转换成字节形式，之所以可以在文本文件看到字符，是因为系统将底层的二进制序列转换成了字符。

## 2. Channel和Buffer使用

### 2.1 Buffer

Buffer里有3个**关键变量**。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/d37341999383489896eeb066b43b3531.jpeg#pic_center)

1. capcity：表示缓冲器`Buffer`的最大数据容量。
2. position：用来指出下一个可以读出/写入`Buffer`的索引位置，也就是记录指针的作用。
3. limit：用来表示在`Buffer`里第一个不能被读出/写入的索引位置。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/206dbd62fd7d4b4592da7eba1477c1e4.png#pic_center)


另外`Buffer`还提供了`get`、`put`方法来供我们操作数据，而使用`get/put`后，position的指针位置也会随之移动。

```java
public abstract byte get();

public abstract ByteBuffer put(byte b);
```

### 2.2 Channel

Channel有常见的3个方法，map()、read()和write()。

```java
// 将通道文件的区域直接映射到字节缓冲区中
public abstract MappedByteBuffer map(MapMode mode, long position, long size)

// 从此Channel通道读取字节序列到给定缓冲区dst
public abstract int read(ByteBuffer dst)
    
// 将给定缓冲区中src的字节序列写入此Channel通道
public abstract int write(ByteBuffer src)
```

以下是`Channel`的简单使用代码。

```java
public class TestFileChannel {
    public static void main(String[] args) {
        File f = new File("D:\\JavaGetOffer\\TestFileChannel.java");
        try {
            FileChannel inChannel = new FileInputStream(f).getChannel();
            FileChannel outChannel = new FileOutputStream("a.txt").getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, f.length());

            outChannel.write(buffer);
            buffer.clear();
            CharBuffer charBuffer = StandardCharsets.UTF_8.newDecoder().decode(buffer);
            System.out.println(charBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

## 3. NIO零拷贝

在`NIO`零拷贝出现之前，一个I/O操作会将同一份数据进行**多次拷贝**。可以看下图，一次I/O操作对数据进行了四次复制，同时来伴随两次内核态和用户态的上下文切换，众所周知上下文切换是很耗费性能的操作。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/b8cfa31e08854d5683514d4e4c9fbd20.png#pic_center)


而零拷贝技术改善了上述的问题。可以对比下图，零拷贝技术**减少**了对一份数据的拷贝次数，不再需要将数据在**内核态和用户态**之间进行拷贝，也意味不再进行上下文切换，让数据传输变得更加高效。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/845e712aeed14342b9c6b901023e1ff7.png#pic_center)
# 掌握Redis持久化：RDB文件、AOF文件、AOF重写
## 1. Redis持久化

### 1.1 持久化概念

> ***面试官：知道Redis持久化吗？***

Redis本身是一个基于内存的数据库，它提供了RDB持久化、AOF持久化两种方式，用来将存储在内存中的数据库状态保存到磁盘中。前者是保存了整个Redis**数据库状态**，而后者是保存了从Redis启动后所有执行的写命令。接下来我们就从这两方面展开。

### 1.2 生成RDB文件

> ***面试官：你说一说生成RDB文件的命令是什么？***

触发RDB持久化过程分为手动触发和自动触发，手动触发的命令有两个，一个是`SAVE`命令，一个是`BGSAVE`命令，执行命令后会在根目录生成名为`dump.rdb`的文件。

大家看下以下手动触发的使用。

```shell
# 手动生成RDB文件指令
127.0.0.1:6379> save
OK
127.0.0.1:6379> bgsave
Background saving started
```

另外RDB文件是在Redis启动时**自动载入**，如果把`dump.rdb`文件删除，重启Redis后会发现原先的数据库状态都不存在了。

```shell
# 初始化
127.0.0.1:6379> set name JavaGetOffer
OK
127.0.0.1:6379> get name
"JavaGetOffer"
127.0.0.1:6379> save
OK

# 重启Redis
127.0.0.1:6379> get name
"JavaGetOffer"

# 删除dump.rdb，重启Redis后name为nil
127.0.0.1:6379> get name
(nil)
```

### 1.3 两种命令的选择

> ***面试官：你会在什么场景使用什么命令？***

SAVE命令会**阻塞**Redis服务器进程，直到RDB文件创建完毕为止，在服务器进程阻塞期间，服务器不能处理其他任何命令请求。

而BGSAVE命令则**不进行阻塞**，它会派生出一个子进程，然后由**子进程**负责创建RDB文件，服务器进程继续处理命令请求。可以在上面的指令中看到执行BGSAVE指令后，终端显示`Background saving started`。

所以如果在**业务高峰期**要使用进行RDB持久化，建议是使用后者，可以防止某些请求丢失了。

### 1.4 生成AOF文件

> ***面试官：AOF文件生成呢？***

AOF文件生成需要在Redis配置文件配置`appendonly`的属性值。

```shell
appendonly yes
```

重启Redis执行写命令后，会生成`appendonly.aof`文件。

也可以在终端手动设置`appendonly`属性值。

```shell
config set appendonly yes
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/274cc76129a0490ab46f0a662a1b0b1e.png#pic_center)

## 2. AOF重写

### 2.1 AOF概念

> ***面试官：知道AOF文件重写吗？***

AOF文件是AOF持久化的产物，AOF持久化通过保存服务器所有执行的写命令来记录数据库状态。而AOF文件重写主要是为了解决**AOF文件体积膨胀**的问题。

对于一个键值对，AOF旧的文件会保存数十条对该键值对的修改命令，这样浪费了大量内存空间。

而AOF文件重写可以创建一个新的AOF文件来**替代**现有的AOF文件，新旧两个AOF文件所保存的数据库状态相同，但新AOF文件**不会包含任何浪费空间**的冗余命令，使得新的AOF文件体积很小。

简单来说，就是新的AOF文件只会保存键值对的**最终状态的创建命令**。

### 2.2 多条命令记录键值

> ***面试官：照你这么说，只会保存创建命令，那每个键的创建只有一条命令对吧？***

如果每个键的创建只有一条命令，在执行命令时可能会造成**客户端输入缓冲区**溢出。

Redis重写程序在处理列表、哈希表、集合、有序集合这四种可能会带有多个元素的键时，如果元素的数量超过了`redis.h/REDIS_AOF_REWRITE_ITEMS_PER_CMD`常量的值，那么重写程序将使用**多条命令**来记录键的值，而不单单只使用一条命令。

### 2.3 AOF重写缓冲区

> ***面试官：AOF重写过程中，有新的创建请求进来怎么办？***

AOF重写过程中，有新的创建请求进来怎么办？可以把这些新的创建请求写入到一个缓冲区里。

Redis服务器会维护一个**AOF重写缓冲区**，该缓冲区会在**子进程**创建新AOF文件期间，记录服务器执行的所有写命令。

等新的AOF文件创建完成，Redis服务器会将重写缓冲区中的所有内容**追加**到新AOF文件的末尾，从而保证两个新旧AOF文件状态一致。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/eaf7036c67d7465f8600465e164da251.png#pic_center)
# 掌握ZooKeeper的业务使用场景，ZooKeeper如何实现分布式锁
## 1. ZooKeeper分布式锁

### 1.1 排他锁实现分布式锁

> ***面试官：知道Zookeeper有什么应用场景吗?***

目前地球村里大型公司部署的分布式技术，绝大部分都是由Zookeeper提供底层的技术支持，所以Zookeeper多么重要就不用我多说了吧。

我们可以利用Zookeeper来完成分布式系统涉及的各种**核心功能**，例如以下4种：

1. 数据发布/订阅。可以用来实现配置中心。

2. 命名服务。类似于UUID，可以生成全局唯一的ID。

3. 集群管理。每一个服务器是一个子节点，可以用来检测到集群中机器的上/下线情况。

4. 分布式锁。

南哥先讲下我们可以怎么利用Zookeeper来实现分布式锁，要实现分布式锁，分为**获取锁和释放锁**两个步骤。

ZooKeepr获取锁时会在`/exclusive_lock`节点下创建子节点，如果创建成功则获得锁。如果创建失败，则访问Zookeeper的客户端会在该节点注册一个`Watcher监听`，用来实时监控子节点的变更从而重新获得锁，这有点类似于线程的循环等待。

当要释放锁时，Zookeeper会**删除**该子节点，此时`/exclusive_lock`节点下就有空位了。Watcher监听则通知客户端可以重新创建子节点来获得锁资源。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1bcea41c87db4d0aa2d786bb0836bb16.png#pic_center)

### 1.2 共享锁实现分布式锁

> ***面试官：你说的是排他锁，共享锁呢？***

大家有没发现，上面分布式锁的实现方式是排他锁，我们也可以使用共享锁的实现方式，来看看两者的区别。

> 排他锁，又称为写锁或独占锁，是一种基本的锁类型。如果事务T1对数据对象O1加上了排他锁，那么在整个加锁期间，只允许事务T1对O1进行读取和更新操作，其他任何事务都不能再对这个数据对象进行任何类型的操作——直到T1释放了排他锁。
>
> 共享锁，又称为读锁，同样是一种基本的锁类型。如果事务T1对数据对象O1加上了共享锁，那么当前事务只能对O1进行读取操作，其他事务也只能对这个数据对象加共享锁——直到该数据对象上的所有共享锁都被释放。

Zookeeper以共享锁方式来实现分布式锁，每次**读、写**请求都会去创建子节点，这是一个类似于“`/shared_lock/[Hostname]-请求类型-序号`”的**临时顺序节点**。

每一个要获得分布式锁的客户端都会去获取子节点列表，同时注册Watcher监听，读、写这两步有不同的步骤。

（1）获取读锁的话，如果前面比自己小的序号**没有写请求**，则表示可以读。

（2）获取写锁的话，只有在自己是**序号最小**的情况下，才可以读成功。

另外共享锁的释放锁和排他锁都是一样的，只需要删除所创建的子节点就可以。

### 1.3 共享锁羊群效应

> ***面试官：有没听说过共享锁的羊群效应？***

大家要注意下，共享锁来实现分布式锁，在**集群规模比较大**的场景下，可能会出现羊群效应。

什么是羊群效应？我们看看百度百科的解释。

> 羊群效应是个人的观念或行为由于真实的或想象的群体的影响或压力，而向与多数人相一致的方向变化的现象。

其实共享锁的特别之处，在于每次**读、写**请求都要注册Watcher监听来获取子节点列表，特别是数量更多的读请求，每1分钟可能是上百万次的请求。

以共享锁来实现，子节点列表只要每次一变动，就要通知**所有**的服务器客户端。这明显造成了短时间大量的**事件通知**，给Zookeeper带来的性能消耗是巨大的。

### 1.4 处理羊群效应

> ***面试官：那怎么解决呢？***

如何看待共享锁带来的羊群效应，我们从两个方面来看待。如果在集群不大的情况下羊群效应发生带来的影响不会太大，而且这种设计**简单实用**。

而如果在集群规模大的场景下，我们可以这样**改进**。客户端的读、写请求首先获取子节点列表，但都**不注册Watcher监听**。

（1）读请求：**只向**比自己序号小的最后一个写请求节点注册Watcher监听。

（2）写请求：**只向**比自己序号小的最后一个节点注册Watcher监听。

这样的设计就可以避免羊群效应，主要是从监听子节点列表，改进为只监听**某个子节点**。

## 2. Kafka应用场景

### 2.1 Kafka应用场景

> ***面试官：Kafka应用场景呢，知道Kafka是怎么利用Zookeeper吗？***

南哥了解到的Kafka利用Zookeeper的主要有 4 点，我们来看看。

（1）使用Zookeeper来对所有Broker服务器、Topic进行管理。Broker启动后都会到Zookeeper上创建属于自己的**临时节点**，其节点路径为`/broker/ids/[0…N]`，注册Topic节点也是一样。

（2）而在Kafka防止消费重复消费方面，消费者消费消息后，都会在消息分区写入**临时节点**，代表该消息已消费。

（3）另外在Kafka生产者负载均衡方面，Kafka消息生产者会通过**监听Broker节点列表**，负载均衡地分发到某一个Broker。

（4）在消费者负载均衡有两方面。一方面，每一个消费者服务器都会在Zookeeper创建**消费者节点**。当有新消息时，Kafka就可以通过Zookeeper的**消费者节点列表**负载均衡地通知某个消费者；另一方面，Kafka将一个Topic分成了多个分区，多个分区由**不同的Broker**处理，这是实现**对Broker的负载均衡**。
# 掌握ZooKeeper的二阶段提交及其优缺点
## 1. ZooKeeper的协议

### 1.1 ZAB协议

> ***面试官：知道ZAB协议吗？***

要深入学习ZooKeeper前，南哥认为我们要先学习ZooKeeper的核心理念，所有的ZooKeeper行为都是围绕这个核心来进行的。说了那么多，它就是——ZAB协议。

ZAB协议英文全称叫ZooKeeper Atomic Broadcast，我们透过中文含义可以大概了解他做了什么事情：ZooKeeper原子消息广播协议。

来看看原子广播在维基百科的解释。

> 在容错分布式计算中，原子广播或全序广播是指多进程系统中的所有正确进程都以相同顺序接收同一组消息（即相同的消息序列）的广播。

那ZooKeeper广播啥呢？我们知道ZooKeeper集群有Leader服务器、Follower服务器，这个Leader服务器接收了客户端所有的**事务请求**，事务请求可以是新增某一个ZNode节点，也可以是删除某一个ZNode节点。

这些事务请求的变更要不要提交、如何通知其他Follower服务器进行同步变更，这就是广播涉及的主要内容了。

ZAB协议主要包含了消息广播、崩溃模式，跟着南哥往下看看。

### 1.2 消息广播

> ***面试官：消息广播你讲一讲？***

ZAB协议的消息广播类似于**二阶段提交过程**。顾名思义事务最终的提交要分为两个阶段。

消息广播的流程如下：

（1）针对客户端的事务请求，Leader服务器会为其生成对应的事务Proposal，同时**广播**给集群中其余Followr机器。这个事务Proposal我们可以把他理解为事务提案。

（2）Follower服务器在接收到事务Proposal后会以事务日志形式写入到本地磁盘中，如果写入成功，则反馈一个Ack反馈给到Leader服务器。

（3）Leader服务器会收集其他Follower服务器的选票，只有**半数**的Follow服务器同意本次事务请求，那Leader服务器就会**广播**一个Commit消息，通知所有Follower服务器进行事务提交。

总结下来，也就是分为二个阶段，第一阶段是询问事务Proposal的写入尝试能否成功，第二阶段就是在Leader服务器、Follower服务器进行事务提交。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/f14fefb4cd6a4314b47b3dd492bea93c.png#pic_center)

### 1.3 消息广播的缺点

> ***面试官：那二阶段提交有什么缺点吗？***

当然消息广播的二阶段提交有所缺点。

（1）在消息广播的第二阶段，如果有部分Follower服务器没有收到Leader服务器广播的事务提交消息，这就会出现数据不一致的情况了。

（2）单点问题。如果Leader服务器在第二阶段奔溃了，那其他Follower服务器仍然会处于锁定上一次事务资源的状态中。

（3）同步阻塞问题。参与一个客户端事务请求时，Leader、Follower服务器的其他逻辑都需要进行阻塞，直到等到上一个二阶段提交完成之后才会开始执行。

### 1.4. 崩溃模式

> ***面试官：崩溃模式呢？***

ZAB协议涉及Leader服务器、Follower服务器，Leader服务器充当了最重要的作用。如果Leader服务器**崩溃**了或者失去和Follwer服务器之间的联系，那上面南哥提到的二阶段提交各种问题很可能都会出现。

开头不是说ZAB协议包含了消息广播、崩溃模式？别慌，崩溃模式就是为此而生的。

崩溃模式总的来说就做了两个事情，我们记住这两点方便理解：一个是确保提交已经被Leader提交的事务Proposal，另一个是丢弃已经被跳过的事务Proposal。

（1）为了解决上文的第1点问题。Leader服务器会为每一个Follower服务器都准备一个Proposal消息队列，通过该队列发送那些没有被各Follower服务器同步的事务Proposal，同时在Proposal消息后面加上Commit消息让Follower服务器进行事务提交。这可以解决二阶段提交带来的数据不一致问题。

（2）为了解决上文的第2点问题。ZooKeeper设计了一个高32位的epoch，用来作为Leader服务器的标识；设计了一个低32位的事务偏移量ZXID，用来作为最新已提交事务的偏移量。

新的Leader服务器上线后，新的Leader服务器拥有集群里最大的事务偏移量，Leader服务器会和Follower服务器的ZXID进行比对，从而让Follower服务器回退被跳过的事务Proposal。
# 掌握垃圾回收器与四种垃圾回收算法
## 1. 判断可回收对象

### 1.1 引用计数法

> ***面试官：JVM为什么不采用引用计数法？***

每个Java对象在引用计数法里都有一个**引用计数器**，引用失效则计数器 - 1，有新的引用则计数器 + 1，通过计数器的数值来判断该对象是否是可回收对象。

大家看下这个例子，如果对象A和对象B没有被任何对象引用，也没有被任何线程访问，这两个对象按理应该被回收。但如果对象A的成员变量引用了对象B，对象B的成员变量引用了对象A，它们的引用计数器数值都**不为0**，通过引用计数法并不能将其视为垃圾对象。

```java
    class A {
        B b = new B();
    }
    class B {
        A a = new A();
    }
```

就因为引用计数法很难解决对象之间相互**循环引用**的问题，所以目前JVM采用可达性分析算法来判断Java对象是否是可回收对象。

### 1.2 可达性分析算法

> ***面试官：那你讲讲可达性分析算法？***

可达性分析顾名思义就是以某个起始点来判断它是否可达，这个起始点称为**GC Roots**。如果Java对象不能从GC Roots作为起始点往下搜索到，那该对象就被视为垃圾对象，即可回收对象。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a85d71aa6e918d552bfa60735f8eec7d.png#pic_center)


可以作为GC Roots对象一共包括以下四种，这点也是面试官常问的：

1. 虚拟机栈中引用的对象。
2. 本地方法栈中引用的对象。
3. 方法区中类静态属性引用的对象。
4. 方法区中常量引用的对象。

## 2. 垃圾回收器

### 2.1 垃圾回收区域

> ***面试官：垃圾回收器回收的是哪个区域？***

JVM由五大区域组成：堆内存、方法区、程序计数器、虚拟机栈、本地方法栈。先说结论，垃圾回收器回收的是**堆内存和方法区**两大区域。

程序计数器、虚拟机栈、本地方法栈的内存分配和回收都具备确定性，都是随着线程销毁而销毁，因此**不需要进行回收**。

但在堆内存、方法区中，内存分配和回收都是**动态**的，我们只有在**运行期间**才能知道会创建哪些对象；另外这些垃圾对象不会自动销毁，如果任由这两部分区域的垃圾对象不管，势必造成内存的浪费甚至有内存泄漏的可能。

垃圾回收器存在的意义就是通过自动检测和回收这些垃圾对象，来减少内存泄漏的风险。

### 2.2 回收永久代

> ***面试官：那永久代不会进行垃圾回收对吧？***

虽然永久代的垃圾回收效率是比较低的，但永久代里的**废弃常量和无用的类**仍然会被回收。

例如创建一个**字符串常量**name，该字符串会存在于常量池中。如果该字符串没有任何String对象去引用它，当发生内存回收时有必要会清除该废弃常量。

```java
private static final String name = "JavaGetOffer";
```

### 2.3 垃圾回收器

> ***面试官：你说说都有哪些垃圾回收器？***

目前市面上共有七种垃圾回收器。

1. Serial是一个作用在**新生代**的**单线程**垃圾回收器。在垃圾回收期间系统的所有线程都会阻塞，因此垃圾回收效率也**相对较高**。

2. ParNew则是Serial的**多线程版本**。这也是第一款并发的垃圾回收器，相比Serial来说垃圾回收不需要阻塞所有线程，第一次实现了让垃圾回收线程和用户线程同时工作。

3. Serial Old是Serial的**老年代版本。**

4. Parallel Scavenge同样是作用在**新生代**且是**多线程**，不过它的设计目标是达到一个可控制的**吞吐量**。

5. Parallel Old是Parallel Scavenge收集器的**老年代版本**，我们可以把它和Parallel Scavenge搭配一起使用。

6. CMS是一种以**最短停顿时间**为目标的**多线程**收集器，下文我会介绍CMS实现最短停顿的原理。

7. G1收集器可以说是CMS的**升级版**。

我们可以根据业务实际情况来为各个年代搭配不同的垃圾回收器，以下的垃圾回收器如果有线连接，说明它们之间可以搭配使用。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/04164d4bf2586f7eb32e38ac94ca13e0.png#pic_center)


### 2.4 CMS原理

> ***面试官：你说的CMS为什么有较短的停顿？***

CMS采用了**标记-清除**算法，整个运作过程分为了初始标记、并发标记、重新标记、并发清除四个阶段。

其中初始标记、重新标记的停顿时间是比较短的，而**耗时最长**的并发标记、并发清除能够和用户线程一起**并发工作不需要停顿**，可以说CMS只需要造成初始标记、重新标记带来的短时间停顿。

### 2.5 CMS的缺点

> ***面试官：那它有什么缺点？***

1. CMS是**多线程**的，在垃圾回收时会占用一部分线程，可能会使系统变得相对较慢。
2. CMS并发清理时用户线程还在运行着，也就是说还会有新的垃圾不断产生，这些垃圾被称为**浮动垃圾**。因为浮动垃圾产生在标记阶段后，很明显CMS本次收集是无法处理这些浮动垃圾的，只能等到下一次GC回收。
3. CMS采用**标记-清除**算法，标记-清除算法的缺点是会产生空间碎片，有可能造成大对象找不到足够的连续空间而发生OOM的情况。

### 2.6 G1垃圾回收器

> ***面试官：你说G1是CMS的升级版，为什么？***

G1垃圾回收器设计之初被赋予的使命是未来可以替换掉JDK1.5中发布的CMS垃圾回收器。所以大家可想而知，CMS垃圾回收器的优点G1垃圾回收器都有，另外G1垃圾回收器也避免了CMS的一些不足。

1. G1采用的垃圾回收算法是标记-整理算法，避免了CMS采用**标记-清除**可能产生的空间碎片。
2. 其他收集器在新生代、老年代分别采用**不同收集器进行配合**，而G1垃圾回收器可以不需要其他收集器配合就能独立管理整个GC。

## 3. 垃圾回收算法

> ***面试官：垃圾回收算法都有什么？***

垃圾回收算法一共有四种，其中最基础的垃圾回收算法是标记-清除算法，其他算法其实都是对标记-清除算法的优化而产生的，我们继续往下看。

（1）标记-清除算法

标记-清除算法顾名思义分为**标记**和**清除**两个阶段，首先标记出所有可回收的对象，标记完成后统一进行清除。但该算法有一个缺点，被标记和未标记的对象都是**分散**存储在内存中的，当清除标记对象后会出**现空间碎片**的情况，如下图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/54d01a6a4a7104277b6b2823a7b76d55.png#pic_center)


（2）复制算法

复制算法把内存划分为容量相等的两块，每次只使用一块，当这一块内存不足时就将**存活的对象复制到另一块中**，同时清除当前块的内存空间。这种算法实现简单且运行高效，也不会产生空间碎片的情况，因为新生代的GC是比较频繁的，所以复制算法也广泛用于新生代的垃圾回收。但缺点很明显是**浪费了50%的内存空间**。

（3）标记-整理算法

标记-整理算法是对标记-清除算法的优化。该算法在内存到达一定量后，会把所有已标记的垃圾对象都向一端里移动，然后以存活对象所在的一端为边界，清除边界内所有内存，避免了**标记-清除算法**可能产生的空间碎片。

（4）分代收集算法

一般实际业务系统都是采用分代收集算法。分代顾名思义把JVM内存拆分，分为了新生代、老年代，对不同年代的垃圾回收采用不同的垃圾回收算法来确保回收效率。

大家可以看下自己公司的JDK使用了什么垃圾回收器，加深下对本篇的理解。

```shell
# 打印JVM启动时的命令行标志
java -XX:+PrintCommandLineFlags -version
```

### 3.1 优化复制算法

> ***面试官：复制算法可以怎么优化吗？***

复制算法把内存划分为容量相等的两块，也就是按1：1分配内存，但这也**浪费了50%空间**。

可以把内存分为一块**较大的Eden空间和两块较小的Survivor空间**，每次只使用Eden空间和其中一块Survivor空间，而另一块Survivor空间用来保存回收时**还存活的对象**。这样就只浪费了其中一块Survivor空间的内存。
# 接口和多态
## 1. 抽象类

### 1.1 子类调用父类

现在有IDEA集成开发环境，可以给大家实时提醒哪个地方编译错误，但假如要大家用`.txt`文件编写程序呢。南哥问：现在这段代码错在了哪？

```java
class Base {
    public Base(String s) {
        System.out.print("B");
    }
}

public class Derived extends Base {
    public Derived (String s) {
        System.out.print("D");
    }
    public static void main(String[] args) {
        new Derived("C");
    }
}
```

假如父类和子类同时拥有**有参构造方法**，子类的构造方法必须显性地调用父类的构造方法，否则会**编译错误**。所以正常的写法应该是这样。

```java
    public Derived (String s) {
        super(s);  
        System.out.print("D");
    }
```

另外大家还需要注意一点，调用父类的构造方法必须在子类构造方法的第一行，调用父类的构造方法也只能出现在子类的构造方法上，否则也会是编译报错。

### 1.2 子类访问父类

如下代码，一共有两处编译错误。提示：错误在Child类里，能快速找出来吗？

```java
class Parent {
    public static String staticVar = "Static Variable from Parent";
    private static String privateStaticVar = "Private Static Variable from Parent";

    public static void staticMethod() {
        System.out.println(staticVar);
    }

    private static void privateStaticMethod() {
        System.out.println(privateStaticVar);
    }
}

class Child extends Parent {
    public void staticMethod() {
        System.out.println("Static method in Child");
    }

    public void display() {
        System.out.println(staticVar);

        System.out.println(privateStaticVar);
        privateStaticMethod();
        
        staticMethod();
}
```

（1）父类的私有变量、私有方法，子类是有**继承**的，但是不能访问。所以`Child.display()`里的以下调用是编译错误的。

```java
System.out.println(privateStaticVar);
privateStaticMethod();
```

（2）子类可以继承，同时也可以访问父类的static变量、方法。但父类的`static`方法大家需要注意，子类是不能**直接覆盖**的，所以以下代码会编译错误。

```java
    public void staticMethod() {
        System.out.println("Static method in Child");
    }
```

正确的做法是为该方法添加一个static修饰符，代表这是子类的一个新方法。这种写法叫做**方法隐藏**，子类和父类中都有一个相同名称和参数的静态方法时，子类的方法将隐藏父类的方法。

```java
    public static void staticMethod() {
        System.out.println("Static method in Child");
    }
```

另外如果父类的方法使用final修饰，子类也是不能覆盖的。

### 1.3 父类不可访问的方法

紧跟着上文代码的例子，父类的方法同样使用`static`修饰，子类的`privateStaticMethod`方法算不算覆盖父类的方法呢？有没有编译报错？

```java
class Parent {
    public static String staticVar = "Static Variable from Parent";
    private static String privateStaticVar = "Private Static Variable from Parent";

    private static void privateStaticMethod() {
        System.out.println(privateStaticVar);
    }
}

class Child extends Parent {
    public void privateStaticMethod() {
        System.out.println(staticVar);
    }
}
```

答案是编译正常。

父类中不可访问的方法，子类编写相同名称和参数的方法并不算覆盖。父类的方法都不能访问了，也就没有覆盖这一说法了。。。





## 2. 接口

### 2.1 访问修饰符的区别

接口和抽象类有三个方面的区别，分布是类的修饰、方法的修饰、变量的修饰。我们往下看看。

（1）类

接口使用`interface`修饰，而抽象类使用`abstract`修饰。当它们作为**外部类**时，只能使用public、default修饰，不能使用private修饰。

（2）方法

**普通接口方法**只能由`public abstract`、`default`、`static`修饰。

**抽象接口方法**可以由所有修饰符修饰，除了final。

总结下，它们两者也有共同点，就是都不能使用**final**修饰。

（3）变量

**普通接口变量**只能由`public static final`修饰。

**抽象接口变量**可以由**所有**修饰符修饰。

### 2.2 静态分派

这算是一个很偏的知识点了，如下代码有三个名为`getType`的重载方法，它们的返回类型相同、方法名也相同，只有入参类型不同。

南哥问：程序执行结果是什么？

```java
public class Test {
    public static void main(String[] args) {
        for(Collection<?> collection: collections) {
            System.out.println(getType(collection));
        }
    }
    
    public static final Collection<?>[] collections = {new HashSet<String>(), new ArrayList<String>()};

    public static String getType(Collection<?> collection) {
        return "Super:collection";
    }
    public static String getType(List<?> list) {
        return "Super:list";
    }
    public String getType(ArrayList<?> list) {
        return "Super:arrayList";
    }
}
```

南哥给大家这么一行代码：`Collection<?> collection = new ArrayList<Integer>()`，**左边**的`Collection<?>`其实是静态类型，右边的`new ArrayList<Integer>()`其实是动态类型。

而**编译器在处理重载方法**时，是根据参数的**静态类型**作为判断依据，而不是根据动态类型。`collections`数组里面的所有实例的静态类型都是`Collection<?>`，`getType`方法也都是执行上文的第一个重载方法。

```sh
# 程序员执行结果
Super:collection
Super:collection
```
# 数据类型和程序运算
## 1. 数据类型

### 1.1 static修饰的变量

本文所有内容在企业考核的笔试题出现频率很高，而且是易错题大家注意下！

南友们在玩Java时有没发现，下面这样一个对象，我们即使没有给变量赋值，在创建它后这个变量依旧会有**默认值**。

```java
class A {
    int a;
}

System.out.println(new A().a);
```

```java
程序执行结果：
0
```

有时前端同学要求后端给个默认值0，我们甚至不用动手，Java编译器就把活给干完。

这实际上是Java语言的一个特性，对于实例变量即成员变量，如果是基本数据类型都会有一个默认值。不同的基本类型默认值不同，我们看看以下各种基本类型的默认值。

```java
int a; //0
short b; //0
long c; //0
float d; //0.0
double e; //0.0
boolean f; //false
byte g; //0
char h; //空字符
```

### 1.2 自动类型提升

（1）Java中的byte、short、char进行数学计算时都会**提升**为int类型，很容易忽略的基础知识，南哥慢慢道来。

以下代码的运行正常吗？

```java
byte b1 = 1, b2 = 2, b3;
b3 = b1 + b2;
```

答案在你意料之中，就是编译报错。

```shell
# 报错内容
java: 不兼容的类型: 从int转换到byte可能会有损失
```

既然byte、short、char进行数学计算时都会提升为int类型，那我们就需要在运行过程中把结果转换成byte类型。正确的做法如下。

```java
b3 = (byte)(b1 + b2);
```

（2）但假如byte变量是这样的写法，我们给b1和b2都加个final，很神奇，编译不会报错。 

```java
final byte b1 = 1, b2 = 2, b3;
b3 = b1 + b2;
```

这种情况是一个特殊情况，Java编译器会为其进行特殊处理，我们称它为编译时常量表达式的求值。b1、b2、b3都是常量值，b3在编译阶段就会被编译器进行赋值，不会涉及到上面我们提到的数学计算提升为int类型，也就不会编译错误。

（3）但如果是这种情况呢？

```java
final byte b1 = 1; byte b2 = 2, b3;
b3 = b1 + b2;
```

以上两个byte变量，只有一个final修饰，也就是说**对b3赋值运算**不能在编译时进行，那这段代码依旧会报错，我们还是需要把结果转换为byte类型。

正确做法如下。

```java
b3 = (byte)(b1 + b2);
```

### 1.3 byte溢出

byte类型的数据范围在-128 ~ 127，当这个值超过127会转变成 - 128。为什么呢？

```java
byte i = 127;
System.out.println(++i);
```

```shell
程序执行结果：
-128
```

byte类型的最大值127在二进制中表示为`01111111`，当我们对127的值增加1时，每位加1后都会产生进位，导致的结果就是所有的位都会翻转（从`01111111`变成`10000000`），而`10000000`十进制的表示就是-128。

### 1.4 Bollean赋值

业务开发编写最多就是条件语句了，特别在迭代年代比较旧的老项目，一套接一套的if语句。

既然见识了那么多条件语句，那以下代码的执行结果是什么？

```java
Boolean flag = false;
if (flag = true) {
    System.out.println("true");
}
else {
    System.out.println("false");
}
```

在Java里，条件判断是有赋值的功能，try语句同样也有。此时falg在条件判断里被赋值了。

```shell
程序执行结果：
true
```



## 2. 程序运算

### 2.1 三元运算符

三元运算符的坑，相信不少南友遇到过。。。我们来看看三元运算符是什么？

> Java中的三元运算符是一种简洁的条件表达式工具，其语法格式为：条件 ? 表达式1 : 表达式2。
>
> 如果`条件`为真（true），则表达式的结果是`表达式1`；如果为假（false），则结果是`表达式2`。

假如是这种情况呢，南哥问：o1最终的数据类型是什么？

```java
Object o1 = true ? new Integer(1) : new Double(2.0);
```

上面的代码行其实等同于这一行。

```java
Object o1 = true ? new Double(1.0) : new Double(2.0);
```

三元运算符的一个非常关键的细节就是类型的统一化。Double类型的数据范围更大于Interger类型，所以Java编译器会对值类型进行**类型提升**，最终把Integer类型提升为Double类型。

### 2.2 自增问题

下面是南哥编写的两个`i++`自增的易错问题，面试考核经常出现在笔试题。

（1）南哥第一问：以下代码执行的结果是什么？

```java
int i = 0;
i = i++ + i; 
```

```shell
程序执行结果：
1
```

（2）南哥第二问：以下代码执行的结果是什么？

```java
int i = 0;
i = i++;
System.out.println(i);
```

```shell
程序执行结果：
0
```

### 2.3 String对象

我们创建一个String对象，JVM在背后实际上做了很多功夫，String对象在常量池、堆内存都有可能存在。我们具体问题来具体分析下。

（1）以下代码段**不包含**引用类型，只是单纯的**字面量拼接**，所以只会创建一个对象存在于常量池中。

```java
String s = "JavaProGuide" + "南哥" + 666;
```

（2）以下代码段**包含**了引用类型，一共创建了3个对象，猜对了吗？

```java
String s = "Hello";
s = s + " world!"
```

"Hello"、" world!"都属于**字面量**，所以它们都会被加入到Java字符串常量池中。

而`s + " world!"`这么一个代码段涉及了**引用类型**，所以它在内存里创建了一个新的String对象，并不存在于常量池，而是存在于堆内存里。

（3）以下代码段一共创建了两个对象，分别存在于常量池、堆内存。

首先new对象会把该String对象放到堆内存里，而过程中会先检查常量池是否存在JavaProGuide

```java
String str = new String("JavaProGuide");
```
# 熟悉Kafka组成模块、Kafka消息提交的方式及优缺点
## 1. Kafka概念

### 1.1 Kafka组成模块

> ***面试官：你先说说Kafka由什么模块组成？***

Kafka其实是一款基于**发布与订阅模式**的消息系统，如果按常理来设计，大家是不是把消息发送者的消息直接发送给消息消费者？但Kafka并不是这么设计的，Kafka消息的生产者会对消息进行分类，再发送给中间的消息服务系统，而消息消费者通过订阅某分类的消息去接受特定类型的消息。

其实这么设计的目的也是为了满足大量业务消息的接入，要是单一的消息发送和接收，那开个进程的**管道通信**就可以了。另外如果大家对设计模式的**发布/订阅模式**熟悉的话，对Kafka的设计理念会更容易理解。

总的来说，Kafka由五大模块组成，大家要理解好这些模块的功能作用：消息生产者、消息消费者、`Broker`、主题`Topic`、分区`Partition`。

（1）消息生产者

消息生产者是消息的创造者，每发送一条消息都会发送到特定的主题上去。

（2）消息消费者

消息生产者和消费者都是Kafka的客户端，消息消费者顾名思义作为消息的读取者、消费者。同时Kafka很灵活的一点是，一个消费者可以订阅多个主题，而且一个主题消息也可被不同消息分组的多个消费者处理。这就给我们变化多端的业务设计带来了众多可能性了，方便大家自由发挥。

（3）`Broker`

孤零零部署在Linux的Kafka服务器被称为`Broker`，也就是我上文提到的`中间的消息服务系统`，大家不要小瞧他，单台Broker可以轻松处理**每秒百万级**的消息量。Broker日常工作内容就是接收消息生产者的消息，为每条消息设置偏移量，最后提交到磁盘进行持久化保存。

（4）主题`Topic`

上文我们知道Kafka的消息是有分类的，而分类的标识就是主题`Topic`。大家可以看下具体代码落地会更容易理解，消息生产者`Producer`发送给`clock-topic`主题，消息消费者监听消费`clock-topic`主题下的消息。

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

每一个主题下的消息都需要提交到Broker的磁盘里，假如我们搭建了三个Broker节点组成的Kafka集群，一般情况下同一个主题下的消息会被分到三个分区进行存储。说到这，由于顺序发送的消息是存储在不同分区中，我们无法保证消息被按**顺序消费**，只能保证**同一个分区**下的消息被顺序消费.

### 1.2 分区

> ***面试官：那分区有什么作用？***

消费分区的作用主要就是为了提高Kafka处理消息的**吞吐量**，谁叫Kafka设计之初就是作为一款高吞吐量、高可用、可扩展的应用程序。

假如一个topic下有N个分区、N个消费者，每个分区会发送消息给对应的一个消费者，这样N个消费者就可以**负载均衡**地处理消息。

同时消息生产者会发送消息给不同分区，每个分区又是属于不同的Broker，这让Broker集群**平坦压力**，大大提高了Kafka的吞吐量。

大家还需要注意一点，如果一个主题下消费者的数量超过分区的数量，超过数量的消费者是会被**闲置**的，一般N个分区最多搭配N个消费者。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/d5253c915efc4a50aa13696321c77edc.png#pic_center)

### 1.3 异步回调

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

## 2. 消费者消息提交

### 2.1 提交消息的方式

> ***面试官：你说说消费者手动提交和自动提交有什么区别？***

手动提交和自动提交是Kafka两种客户端的偏移量提交方式，提交方式的配置选项是`enable.auto.commit`，默认情况下该选项为ture。

偏移量提交是什么？大家可以理解为消费者通知当前最新的**读取位置**给到分区，也就是告诉分区哪些消息已消费了。

如果`enable.auto.commit`为true代表提交方式为自动提交，默认为5秒的提交时间间隔。每过**5秒**，消费者客户端就会自动提交最大偏移量。

如果`enable.auto.commit`为false代表提交方式为手动提交，我们需要让消费者客户端消费**程序执行后**提交当前的最大偏移量。

### 2.2 提交方式的优缺点

> ***面试官：那它们都有什么优、缺点？***

（1）自动提交

自动提交比较方便，我们甚至都不需要配置提交方式，不过可能会导致消息丢失或重复消费。

如果刚好到了5秒的时间间隔自动**提交了**最大偏移量，此时正在执行消息程序的消费者客户端崩溃了，就会导致**消息丢失**。

如果成功消费了消息，下一秒消费者应该自动提交，但如果此时消费者客户端奔溃，就会导致其他分区的消费者**重复消费**。

（1）手动提交

手动提交需要消费者客户端在消费消息后手动提交消息，手动提交的方式又分为同步提交、异步提交。

手动提交是**同步提交**的话，在Broker对请求做出回应之前，客户端会一直阻塞，这样的话限制应用程序的**吞吐量**。

手动提交是**异步提交**的话，不会有吞吐量的问题。不过消费者客户端发送给Broker偏移量之后，**不会管**Broker有没有收到消息。这种情况就要采用上文我提到的消息生产者**异步回调**来进行日志记录，有了日志记录方便后续bug排查，工作效率妥妥的高😏。
# 熟悉Spring MVC工作流程，掌握Spring MVC常见注解
## 1. Spring MVC概况

### 1.1 如何理解Spring MVC

大家都知道Spring MVC很强大，南哥问大家一个问题，Spring MVC为什么会出现？一项技术的出现必定是为了解决旧技术考虑不全所积累的软件熵。《程序员修炼之道》在`软件的熵`一节中对熵的解释很有冲击力，作者是这么说的，大家有没什么触动。

> 虽然软件开发不受绝大多数物理法则的约束，但我们无法躲避来自熵的增加的重击。熵是一个物理学术语，它定义了一个系统的“无序”总量。不幸的是，热力学法则决定了宇宙中的熵会趋向最大化。当软件中的无序化增加时，程序员会说“软件在腐烂”。有些人可能会用更乐观的术语来称呼它，即“技术债”，潜台词是说他们总有一天会偿还的——恐怕不会还了。

在没有出现Spring MVC之前，老一代的开发者会在`Servlet`中编写业务逻辑和控制代码，甚至属于后端的业务逻辑也会耦合在了`jSP`页面。在当时互联网不流行，业务都比较简单的年代，这样写问题不会太大，但随着时间的累积、互联网的爆发，业务复杂度也爆发式上升，这叫新来的实习生程序员怎么上手呢。缺乏统一和清晰的架构模式，会导致应用程序的可扩展性和可维护性降低。

我们先不讲Spring MVC，把**MVC**拆解出来。MVC（Model View Controller）实践上是一种软件架构思想，这个思想指导把应用程序分为了三个模块，用于编写业务逻辑的模型、用于数据呈现的视图、用于协调前两者的控制器。

在我们Java程序员第一次接触企业框架时，我们最开始一般用SSM来练练手。如果是SSM框架，充当`Model`的是编写业务逻辑Java类，充当`View`的是JSP页面，而充当`Controller`的则是Servlet。总的来说，MVC明确划分了各个模块的责任，不是你负责的东西不允许越线，这明显维护起来好看多了。

好久好久之前南哥练手的第一个项目是坦克大战，和现在一般企业业务把一个Java对象看出是需求的抽象不同，我当时的坦克大战是把一个Java对象看成是一只坦克的载体。大家第一个Java练手项目有什么故事吗？

## 2. Spring MVC技术要点

### 2.1 Spring MVC工作流程

Spring MVC工作流程涉及**五大组件**，大家先预览一遍：DispatcherServlet、HandleMapping、Controller、ModelAndView、ViewResolver。

第一步用户触发浏览器时将请求发送给前端控制器DispatcherServlet，DispatcherServlet就相当于上文MVC架构的**C**，Spring源码对DispatcherServlet解释为`HTTP请求处理程序/ 控制器的中央调度程序`。有了中央调度程序大脑，下一步就可以联调其他组件了。

```java
// DispatcherServlet类
package org.springframework.web.servlet;
public class DispatcherServlet extends FrameworkServlet { }
```

第二步，DispatcherServlet调用处理器映射器HandleMapping，根据用户请求的URL找到对应的业务控制器Contorller。

```java
// HandlerMapping类
package org.springframework.web.reactive;
public interface HandlerMapping { }
```

第三步，DispatcherServlet请求处理器适配器HandlerAdapter执行Controller，获得**业务结果**后返回一个模型视图对象ModelAndView给到DispatcherServlet。

```java
// ModelAndView类
package org.springframework.web.servlet;
public class ModelAndView {
```

```java
// HandlerAdapter类
package org.springframework.web.servlet;
public interface HandlerAdapter { }
```

第四步，DispatcherServlet把ModelAndView返回给视图解析器ViewResolver，将ModelAndView解析为视图对象View。

```java
// ViewResolver类
package org.springframework.web.servlet;
public interface ViewResolver {
```

最后一步，View会负责渲染，同时把结果返回给浏览器。

### 2.2 Spring MVC搭配Tomcat容器

大家有搭过Spring Web MVC框架的话就有印象，我们要在本机安装单独的一个Tomcat服务器，Tomcat搭配Spring框架才能让我们的Web应用程序跑起来。会不会觉得很麻烦，南哥觉得好麻烦。。

SpringBoot框架则不需要我们单独去部署一个Tomcat服务器，大家甚至在https://start.spring.io/官网下载包后，本地启动就可以把Web程序跑起来，方便吧。

这是为什么？SpringBoot内置了一个**Servlet容器**，而上文南哥所说的Tomcat容器本质也是一个Servlet容器，SpringBoot默认为我们配置的是Tomcat。要是对Tomcat不满意，你也可以用其他Servlet容器，比如Jetty、Undertow。

Tomcat容器为我们的Spring MVC做了很多脏活，例如底层Socket连接这种麻烦工作。而上文我提到的Spring MVC五大组件本质上都是调用**Servlet API**，而Servlet API的实现也是由Tomcat容器为我们完成的。

在Spring Web MVC框架里，如果大家要单独部署Servlet容器，切记注意下Spring框架和Servlet 容器的兼容性。在[Spring官方文档](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions)中，Spring Framework 5.3.x 支持的最后一个Servlet规范版本4.0，从 Spring Framework 6.0 开始，Servlet最低版本为Servlet 5.0。

### 2.3 Spring MVC常见注解

（1）@Controller和@RestController

把某一个Java类申明为后端接口，我们一般使用@Controller修饰该类，使用@RestController也可以，两者的差异在于后者是@Controller和@ResponseBody的组合，后端接口返回的数据格式会是ResponseBody格式的数据。

大家看下两者的源码解释，南哥把英文注释翻译为了中文。

```java
// 基本 Controller 接口，表示接收HttpServletRequest和HttpServletResponse实例的组件，就像HttpServlet一样，但能够参与 MVC 工作流。
@FunctionalInterface
public interface Controller {
}
```

```java
// 便捷注释本身带有@Controller和@ResponseBody注释。
// 带有此注释的类型被视为控制器，其中@RequestMapping方法默认采用@ResponseBody语义。
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
}
```

（2）@RequestMapping

这个注解的作用是把请求映射到控制器方法，例如getPerson方法，前端同学请求`/persons/{id}`就可以控制该方法执行。

```java
@RestController
@RequestMapping("/persons")
class PersonController {

	@GetMapping("/{id}")
	public Person getPerson(@PathVariable Long id) {
		// ...
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void add(@RequestBody Person person) {
		// ...
	}
}
```

HTTP方法有多种请求类型，Spring框架也提供了五种Mapping注解。

- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PatchMapping`

（3）@RequestParam和、@PathVariable

有个Spring MVC注解相关的小细节，当我们编写有入参的后端接口时，很多同学弄不清楚入参对应的注解要使用什么。

南哥整理了下，如果请求URL类似于`localhost:8080/test/?id=6`，使用的入参注解是@RequestParam。

```java
  @PostMapping("/test")
  public CommonResult publishCourse(@RequestParam String id) {
  }
```

如果请求URL类似于`localhost:8080/test/6`，使用的入参注解是@PathVariable。

```java
  @PostMapping("/test/{id}")
  public CommonResult publishCourse(@PathVariable String id) {
  }
```
# 面试官没想到一个ArrayList，我都能跟他扯半小时
## 1. List集合

### 1.1 集合概述

> ***面试官：List集合都知道哪些对象？***

作为四大集合之一的List，在业务开发中我们比较常见的是以下 3 种：ArrayList、Vector、LinkedList，业务开发我们接触最多就是容器类库了，容器类库可以说是面向对象语言最重要的类库。大家看看在工作里你比较熟悉的是哪个？

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/e58fd12731e74a0ea08249642dcdae83.png)


这篇文章南哥打算专注于List集合，后面四大集合之Map、Queue、Set后续再来填坑，比心心♥。

### 1.2 ArrayList

> ***面试官：ArrayList为什么线程不安全？***

普通的数组类型，我们是这么创建的`int[] arr = new int[66]`。数组可以创建固定长度的容量，不会过度浪费资源，但有优点也有缺点。如果长度设置过小，你就会看到一个大大的`ArrayIndexOutOfBoundsException`。

```shell
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 6
	at org.codeman.Test.main(MsgExtractor.java:11)
```

也别把数组说得那么不堪，起码数组是线程安全的，ArrayList却是线程不安全的。另外ArrayList底层的存储容器实际上也是一个Object数组，大家看看以下源码。

```java
    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access
```

那ArrayList为什么线程不安全？原因就在下面源码这个size。

```java
    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
```

ArrayList底层是根据当前size的值来作为新添加元素的下标，南哥假设目前size为0，有线程A、线程B都想要添加一个元素。

线程A在下标0插入A元素，当添加成功后还没有对size进行++。此时CPU调度让线程B运行，线程B也在下标0插入B元素，覆盖了A元素。线程A、B执行到程序末尾对size进行++，此时就有问题了，大家发现了没？

size进行了两次加法变成了2，但却只有一个B元素添加到了下标0位置，后面再添加其他元素下标1也会是空的。

### 1.3 AarrayList面试小tip

另外ArrayList有些小知识点大家也需要记一记，面试官可能照着公司给的面试题稿子问你：

（1）ArrayList初始容量为10。

（2）ArrayList负载因子为1，也代表ArrayList底层数组满了才会扩容。而数组扩容长度为原始长度的**1.5倍**。

（3）ArrayList的扩容时间复杂度为O(n)。



### 1.4 Vector

> ***面试官：知道线程安全的List集合吗？***

Vector和ArrayList的源码说明很相似，都是告诉你它们相比数组来说是一个可调整大小的数组实现，大家看看以下源码注释。

```java
// Resizable-array implementation of the <tt>List</tt> interface.
// List接口的可调整大小数组实现。
```

```java
// The {@code Vector} class implements a growable array of objects.
// Vector类实现了一个可增长的对象数组。
```

那Vector和ArrayList有什么区别？南哥给大家贴下get和set方法的源码就一清二楚，Vector的元素操作都是线程安全性的，每个方法都有`synchronized`进行修饰，而ArrayLiset却是一个线程不安全的List集合。

```java
    // Vector源码
    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    public synchronized E set(int index, E element) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }
```

两者除了线程安全性的区别，在效率上，ArrayList相比Vector来说效率更高。Vector虽然线程安全了，但每个操作方法是同步的，也意味着增加了额外的开销。

一般我们在业务开发也很少使用到Vector，至少南哥还没有在开发中使用过Vector，小伙伴有写过的吗？如果是需要保证线程安全的场景，我一般是在集合的外部方法加上锁机制，或者使用线程安全的List集合，我更多使用的是`CopyOnWriteArrayList`而不是Vector。

### 1.5 Vector面试小tip

（1）Vector初始容量为10。

（2）Vector负载因子为1，也代表Vector底层数组满了才会扩容。而数组扩容长度为原始长度的**2倍**。

（3）Vector的扩容时间复杂度为O(n)。

### 1.6 LinkedList

> ***面试官：双向链表你说说？***

LinkedList是JDK提供的一个双向链表实现，我们来看看官方源码的介绍。

> List和Deque接口的双向链表实现。实现所有可选的列表操作，并允许所有元素（包括null ）。
> 所有操作的执行方式都符合双向链表的预期。索引到列表中的操作将从列表的开头或结尾（以更接近指定索引为准）遍历列表
>
> Doubly-linked list implementation of the {@code List} and {@code Deque} interfaces.  Implements all optional list operations, and permits all elements (including {@code null}).

我们来看看LinkedList的数据结构，节点类型分为头节点和尾节点。

```java
    transient Node<E> first;

    transient Node<E> last;
```

同时每个节点有指向上一个节点的指针和下一个节点的指针。

```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

LinkedList比较重要的知识点是为什么它也是一个线程不安全的List集合实现？这一点和上文介绍的`头节点first`有关。

LinkedList对元素的操作并没有使用`synchronized`进行同步控制，如果现在有两个线程A、B同时要使用addFist添加第一个头节点。当A线程把A元素设置为头节点后，此时的头节点还没有和旧链表建立连接。而线程B执行时又把B元素设置为了头节点，注意！此时A元素被覆盖了。

以上两个线程的两个添加操作最终却只添加了一个元素。

```java
    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    public void addFirst(E e) {
        linkFirst(e);
    }
```
