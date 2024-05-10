> **《Java面指Offer》：🌱作为一份Java学习与面试指南，以【面试官面试】形式覆盖Java程序员所需掌握的Java核心知识、面试重点，目前正在一步步完善中。大家的 ⭐️ Star ⭐️支持，是我创作的最大动力。**

| Ⅰ  | Ⅱ | Ⅲ | Ⅳ | Ⅴ | Ⅵ |
| :--------: | :----------: | :-----------: | :---------: | :---------: | :---------:|
| [Java专栏]() | [Redis专栏]() | [MySQL专栏]() |[Kafka专栏]() | [ZooKeeper专栏]() | [JVM专栏]() |



## Java专栏
1. [HashMap底层原理](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/HashMap底层原理/HashMap底层原理.md)
   - 面试官：你说下HashMap的内部结构？
   - 面试官：那一个键值是怎么存储到HashMap的？
   - 面试官：HashMap链表还会转换成什么？
   - 面试官：HashMap为什么线程不安全？
   - 面试官：有线程安全的Map吗？
   - 面试官：HashTable和ConcurrentHashMap有什么区别吗？
2. [IO流](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/IO流/IO流.md)
   - 面试官：你说下对Java IO的理解？
   - 面试官：那要怎么读取字节流？
   - 面试官：你说的这些不是实例，我要的是能真正读取的？
   - 面试官：为什么加一层缓存流就能提高读取效率？
   - 面试官：读取之后呢，我怎么知道文件读取到末尾了？
   - 面试官：字符流读取呢？
   - 面试官：输出流你也讲一讲？
   - 面试官：那字节流和字符流有什么区别？
   - 面试官：你刚刚提到转换流把字节输入流转换成字符输入流，可不可以倒过来？
4. [NIO](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/NIO/NIO.md)
   - 面试官：了解过NIO吗？
   - 面试官：那NIO为什么速度快？
   - 面试官：还有吗？
   - 面试官：你刚刚说输入/输出流是处理字节？字符流不是处理字符吗？
   - 面试官：你具体介绍下Buffer？
   - 面试官：Channel呢？
   - 面试官：知道NIO零拷贝吗？
5. [synchronized实现原理](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/synchronized实现原理/synchronized实现原理.md)
   - 面试官：知道可重入锁有哪些吗?
   - 面试官：你先说说synchronized的实现原理?
   - 面试官：那synchronized有什么缺点？
   - 面试官：为什么上下文切换要保存当前线程状态？
   - 面试官：可以怎么解决synchronized资源消耗吗？
   - 面试官：那轻量级锁没有缺点吗？
6. [线程池](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/线程池/线程池.md)
   - 面试官：你说下线程池的大小要怎么配置？
   - 面试官：那怎么创建线程池？
   - 面试官：你说的这些预配置线程池会有什么问题？
   - 面试官：你们项目线程池用的这种创建方式？
   - 面试官：线程池请求队列满了，有新的请求进来怎么办？
   - 面试官：线程池的入参ThreadFactory有什么作用？

## JVM专栏
1. [JVM垃圾回收](https://github.com/hdgaadd/JavaGetOffer/blob/master/JVM专栏/JVM垃圾回收/JVM垃圾回收.md)
   - 面试官：知道垃圾回收为什么不采用引用计数法吗？
   - 面试官：说到引用，java有什么引用类型？
   - 面试官：垃圾回收的是新生代、老年代对吧？那永久代还会进行回收吗？
   - 面试官：垃圾回收器回收的是哪个区域的垃圾？
   - 面试官：你说说都有哪些垃圾回收器？
   - 面试官：高吞吐量和停顿时间短有什么好处吗？
   - 面试官：刚刚你说CMS为什么有较短的停顿呢？
   - 面试官：那它有什么缺点？
   - 面试官：你说G1是CMS的升级版，为什么？
   - 面试官：知道Minor GC和Full GC有什么区别吗？
   - 面试官：复制算法可以优化吗？

## Kafka专栏
1. [Kafka事务](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka事务/Kafka事务.md)
   - 面试官：生产者重试机制导致Kafka重复消息，知道怎么处理吗？
   - 面试官：Kafka事务，应该知道吧？
   - 面试官：你说的这个场景，不使用事务会有什么问题吗？
   - 面试官：那Kafka事务一般在什么场景下使用呢？
2. [Kafka消息可靠性](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka消息可靠性/Kafka消息可靠性.md)
   - 面试官：知道Kafka高水位吗？
   - 面试官：你说说Kafka是怎么保证消息可靠性的？
   - 面试官：还有吗，比如生产者消费者呢？
   - 面试官：那要是Kafka消费堆积了怎么办？
   - 面试官：emmmm，你知道Kafka控制器吧？
3. [Kafka生产者消费者](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka生产者消费者/Kafka生产者消费者.md)
   - 面试官：你先说说Kafka由什么模块组成吧？
   - 面试官：那我们先讲讲生产者、消费者？
   - 面试官：消息生产者的异步回调，知道吧？
   - 面试官：消费者分区呢？
   - 面试官：你说说消费者手动提交和自动提交，有什么区别？
   - 面试官：那它们都有什么优、缺点？

## MySQL专栏
1. [MySQL主从复制](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL主从复制/MySQL主从复制.md)
   - 面试官：MySQL主从复制了解吧？
   - 面试官：那这个日志格式是怎样的，有没听说过有好几种？
   - 面试官：知道哪种二进制格式比较好吗？
   - 面试官：那MySQL主从模式有什么好处吗？
   - 面试官：如果把二进制文件丢给从库，从库是不是复制整个文件？
2. [MySQL事务](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL事务/MySQL事务.md)
   - 面试官：事务的特性你说一说？
   - 面试官：隔离性有多种隔离级别，这个知道吧？
   - 面试官：幻读是什么问题？还有其他事务问题吗？
   - 面试官：那幻读要怎么解决？
   - 面试官：事务加锁会导致死锁，要怎么处理？
   - 面试官：有去看看你们数据库用的什么隔离级别吗？
3. [MySQL索引](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL索引/MySQL索引.md)
   - 面试官：知道索引有什么类型吗？
   - 面试官：B树索引说一下？
   - 面试官：你说值都存储在叶子节点，那有什么好处？
   - 面试官：知道为什么主流数据库引擎不采用哈希索引吗？
   - 面试官：聚簇索引和二级索引有什么关联？
   - 面试官：那我一条SQL，我怎么知道它有没使用到索引？
   - 面试官：有没索引失效的情况呢？
4. [MySQL高级特性](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL高级特性/MySQL高级特性.md)
   - 面试官：你先说说知道哪些MySQL的高级特性吧?
   - 面试官：你挑一个讲一讲你对他的理解？
   - 面试官：那分区表是银弹？不会有什么问题吗？
   - 面试官：视图你也讲一下？
   - 面试官：剩下还有那两个什么什么，你也讲一讲
   - 面试官：有没听说过全文索引？
5. [SQL语句优化](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/SQL语句优化/SQL语句优化.md)

## Redis专栏
1. [Redis五大数据类型](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis五大数据类型/Redis五大数据类型.md)
2. [Redis哨兵](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis哨兵/Redis哨兵.md)
   - 面试官：Redis哨兵知道吧？
   - 面试官：嗯然后呢？
   - 面试官：你说说是怎么检测Redis主从服务器的下线状态的？
   - 面试官：有没有A哨兵判断Redis实例下线，但B哨兵判断Redis实例仍然存活的情况呢？
   - 面试官：领头哨兵怎么选举出来的？
   - 面试官：选举出来之后呢，它有什么作用吗？
   - 面试官：知道怎么选举新的Redis主服务器吗？
3. [Redis底层数据结构](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis底层数据结构/Redis底层数据结构.md)
   - 面试官：你说说Redis有什么底层数据结构支持？
   - 面试官：先讲讲你对字典的理解？
   - 面试官：那字典和Redis的哈希对象不是没什么区别？
   - 面试官：跳跃表呢？
   - 面试官：那有序集合为什么要同时使用字典和跳跃表来实现？
   - 面试官：Redis为了节约内存采用了什么数据结构知道吗？
4. [Redis持久化](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis持久化/Redis持久化.md)
   - 面试官：知道Redis持久化吗？
   - 面试官：那你说一说Redis生成RDB文件的命令是什么？
   - 面试官：是你的话，你会在什么场景使用什么命令？
   - 面试官：AOF文件生成呢？
   - 面试官：知道AOF文件重写吗？
   - 面试官：那照你这么说，只会保存创建命令，那每个键的创建只有一条命令对吧？
   - 面试官：那你说说AOF重写过程中，有新的创建请求进来怎么办？
5. [Redis数据同步](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis数据同步/Redis数据同步.md)
   - 面试官：我看你们项目用的Redis主从，数据同步了解吗？
   - 面试官：按你这么说，数据同步后主服务器某个键删除了，数据又不同步了怎么办？
   - 面试官：如果主从服务器断线呢？还是用的RDB来同步吗？
   - 面试官：考你点深入些的，主服务器怎么知道断线期间执行了哪些命令呢？
   - 面试官：你知道服务器运行ID吗？
   - 面试官：Redis心跳检测知道吧？
7. [Redis数据库与内存回收策略](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis数据库与内存回收策略/Redis数据库与内存回收策略.md)
   - 面试官：Redis的数据库知道吧？
   - 面试官：那数据库的键空间呢？
   - 面试官：一个键要怎么设置过期时间？
   - 面试官：那键的过期时间知道用什么存储吗？
   - 面试官：键的过期删除策略是什么？
   - 面试官：Redis还有什么策略可以释放内存？
8. [Redis集群](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis集群/Redis集群.md)
   - 面试官：Redis多机数据库有什么部署方式？
   - 面试官：那他们有什么区别？
   - 面试官：那Redis集群怎么实现负载均衡的？
   - 面试官：要是热点数据都是某个Redis节点的槽，负载均衡不是没用了？
   - 面试官：还有其他方法吗?
   - 面试官：集群里没有哨兵，那他们怎么选举主节点的？
   - 面试官：最后问你一个，集群里节点怎么进行故障检测的？

## ZooKeeper专栏
1. [ZAB协议](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZAB协议/ZAB协议.md)
   - 面试官：知道ZAB协议吗？
   - 面试官：消息广播的二阶段提交你讲一讲？
   - 面试官：那二阶段提交有什么缺点吗？
   - 面试官：既然怎么多缺点，ZooKeeper为什么还采用ZAB协议？
   - 面试官：那崩溃模式怎么解决这些问题的？
   - 面试官：对了，你刚刚提到的事务中断逻辑是什么？
2. [ZooKeeper应用场景](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZooKeeper应用场景/ZooKeeper应用场景.md)
   - 面试官：知道ZooKeeper有什么应用场景吗?
   - 面试官：你挑一个你比较熟悉的场景讲讲?
   - 面试官：你说的是排他锁，共享锁呢？
   - 面试官：emmmm有没听说过共享锁的羊群效应？
   - 面试官：那怎么解决呢？
   - 面试官：Kafka应用场景呢，知道Kafka是怎么利用ZooKeeper吗？
   - 面试官：你刚刚说到Kafka生产者负载均衡，那消费者负载均衡知道吗？
3. [ZooKeeper系统模型](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZooKeeper系统模型/ZooKeeper系统模型.md)
   - 面试官：你说说ZooKeeper数据模型？
   - 面试官：那ZooKeeper数据节点有几种类型？
   - 面试官：数据节点版本知道吧？
   - 面试官：ZooKeeper事务ID呢？
   - 面试官：ZooKeeper数据变更通知使用什么对象？

## 主流框架
1. [Spring AOP](https://github.com/hdgaadd/JavaGetOffer/blob/master/主流框架/Spring AOP/Spring AOP.md)
2. [Spring IOC](https://github.com/hdgaadd/JavaGetOffer/blob/master/主流框架/Spring IOC/Spring IOC.md)


## 未完待续。。。



# ✉️关于文档的说明

### ✨介绍

该开源文档把系统后端知识编写为面试官刁钻考核的形式。

文档涉及的主要内容包括：Redis、MySQL、Java、主流框架、计算机网络、操作系统等面试必备知识。相信无论是哪一门后端语言的选手都能在这份文档学到不少知识。

### 📖我为什么要做这个开源项目

目前网络上分享的各种面经包含的知识点是非常零散分散的，而面试官面试真正想考量你的是体系的知识。

例如大多数大厂面试官问上一个问题，是为下一个问题作准备，问完一个接着问：还有吗？还有吗？然后呢？其实这么问的主要目的就是考核你对这个体系知识的掌握程度。

举个【Redis持久化】的例子：

> 面试官：知道Redis持久化吗？
>
> 面试官：那你说说Redis生成RDB文件的命令是什么？
>
> 面试官：是你的话，你会在什么场景使用什么命令？
>
> 面试官：AOF文件重写也知道吧？
>
> 面试官：那照你这么说，只会保存创建命令，那每个键的创建只有一条命令对吧？
>
> 面试官：那你说说AOF重写过程中，有新的创建请求进来怎么办？

所以靠记住零散的面试题对我们的技术提升并不大，也回答不了面试官对某一个模块刁钻的考核。

这个开源文档准备把每个模块系统的知识都编写为面试官刁钻考核的形式，希望能帮助到大家学习系统后端知识，顺利通关面试。

### ❤最后

本人会利用业余时间完善这个文档，修正一些错误，添加新的文章。

大家的 ⭐️ Star ⭐️支持，是我创作的最大动力。
