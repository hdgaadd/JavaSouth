# 🧭Java进阶指南

### ✨介绍

**作为一份Java学习、面试指南，致力帮助大家理清Java进阶的学习路线。该指南涵盖了你成为更好的Java选手、拿下Java Offer所需掌握的核心知识、面试重点。大家的 ⭐️ Star ⭐️支持，是我创作的最大动力。**

该开源文档涉及的主要内容包括Java系列、Redis系列、MySQL系列、多线程系列、Kafka系列、JVM系列、ZooKeeper系列等等面试必备知识，会持续更新完善。相信能帮助到大家学习Java核心知识，顺利通关面试、拿到理想Offer。

**期待大家的参与，让项目不断完善，帮助更多的后来者！**

### 🎁最后

本人会利用业余时间更新这个文档，修正一些错误。

大家的 ⭐️ Star ⭐️支持，是我创作的最大动力。

# ❤赞赏支持

开源制作不易，如果本系列专栏对你有帮助的话，可以鼓励支持作者下。

# 📚指南明细

## Java专栏
1. [HashMap为什么线程不安全](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/HashMap底层原理/HashMap为什么线程不安全？.md)
   - 面试官：你说下HashMap的内部结构？
   - 面试官：那一个键值是怎么存储到HashMap的？
   - 面试官：HashMap链表还会转换成什么？
   - 面试官：HashMap为什么线程不安全？
   - 面试官：有线程安全的Map吗？
   - 面试官：HashTable和ConcurrentHashMap有什么区别吗？
2. [Java字节流和字符流有什么区别](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/Java%20IO流/Java字节流和字符流有什么区别？.md)
   - 面试官：你说下对Java IO的理解？
   - 面试官：那要怎么读取字节流？
   - 面试官：你说的这些不是实例，我要的是能真正读取的？
   - 面试官：为什么加一层缓存流就能提高读取效率？
   - 面试官：读取之后呢，我怎么知道文件读取到末尾了？
   - 面试官：字符流读取呢？
   - 面试官：输出流你也讲一讲？
   - 面试官：那字节流和字符流有什么区别？
   - 面试官：你刚刚提到转换流把字节输入流转换成字符输入流，可不可以倒过来？
3. [那Java NIO为什么速度快](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/Java%20NIO/那Java%20NIO为什么速度快？.md)
   - 面试官：了解过NIO吗？
   - 面试官：那NIO为什么速度快？
   - 面试官：还有吗？
   - 面试官：你刚刚说输入/输出流是处理字节？字符流不是处理字符吗？
   - 面试官：你具体介绍下Buffer？
   - 面试官：Channel呢？
   - 面试官：知道NIO零拷贝吗？
4. [你先说说synchronized的实现原理](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/synchronized实现原理/你先说说synchronized的实现原理.md)
   - 面试官：知道可重入锁有哪些吗?
   - 面试官：你先说说synchronized的实现原理?
   - 面试官：那synchronized有什么缺点？
   - 面试官：为什么上下文切换要保存当前线程状态？
   - 面试官：可以怎么解决synchronized资源消耗吗？
   - 面试官：那它们都有什么优缺点？
5. [volatile有什么缺点吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/volatile与重排序/volatile有什么缺点吗？.md)
   - 面试官：重排序知道吧？
   - 面试官：那重排序不会有什么问题吗？
   - 面试官：有什么办法可以解决？
   - 面试官：那volatile可见性的原理是什么？
   - 面试官：volatile有什么缺点吗？
6. [线程池请求队列满了，有新的请求进来怎么办](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/线程池/线程池请求队列满了，有新的请求进来怎么办？.md)
   - 面试官：你说下线程池的大小要怎么配置？
   - 面试官：那怎么创建线程池？
   - 面试官：你说的这些预配置线程池会有什么问题？
   - 面试官：你们项目线程池用的这种创建方式？
   - 面试官：线程池请求队列满了，有新的请求进来怎么办？
   - 面试官：线程池的入参ThreadFactory有什么作用？
7. [ThreadLocal知道吧](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/线程通信/ThreadLocal知道吧？.md)
   - 面试官：Java线程的等待/通知机制知道吧？
   - 面试官：还有没有其他线程通信方式？
   - 面试官：你说的Lock对象说下你的理解？
   - 面试官：ThreadLocal知道吧？
   - 面试官：那线程生命周期都有什么？
   
## MySQL专栏
1. [MySQL主从复制了解吧](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL主从复制/MySQL主从复制了解吧？.md)
   - 面试官：MySQL主从复制了解吧？
   - 面试官：那Binary Log日志格式知道有哪些？
   - 面试官：知道哪种二进制格式比较好吗？
   - 面试官：那MySQL主从模式有什么好处？
   - 面试官：如果把二进制文件丢给从库，从库是不是复制整个文件？
2. [事务的特性你说一说](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL事务/事务的特性你说一说？.md)
   - 面试官：事务的特性你说一说？
   - 面试官：隔离性有多种隔离级别，这个知道吧？
   - 面试官：幻读是什么问题？还有其他事务问题吗？
   - 面试官：那幻读要怎么解决？
   - 面试官：事务加锁会导致死锁，要怎么处理？
   - 面试官：有去看看你们数据库用的什么隔离级别吗？
3. [一条SQL，我怎么知道它有没使用到索引](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL索引/一条SQL，我怎么知道它有没使用到索引？.md)
   - 面试官：索引有什么用？
   - 面试官：B树索引说一下？
   - 面试官：你说值都存储在叶子节点，那有什么好处？
   - 面试官：知道为什么主流数据库引擎不采用哈希索引吗？
   - 面试官：聚簇索引和二级索引有什么关联？
   - 面试官：那我一条SQL，我怎么知道它有没使用到索引？
   - 面试官：有没索引失效的情况呢？
4. [你先说说知道哪些MySQL的高级特性](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/MySQL高级特性/你先说说知道哪些MySQL的高级特性.md)
   - 面试官：你先说说知道哪些MySQL的高级特性吧?
   - 面试官：你挑一个讲一讲你对他的理解？
   - 面试官：那分区表是银弹？不会有什么问题吗？
   - 面试官：视图你也讲一下？
   - 面试官：剩下还有那两个什么什么，你也讲一讲
   - 面试官：有没听说过全文索引？
5. [知道MySQL慢查询吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/MySQL专栏/SQL语句优化/知道MySQL慢查询吗？.md)
   - 面试官：知道MySQL慢查询吗？
   - 面试官：在工作中你怎么优化SQL的？
   - 面试官：遵循第二范式就一定最优？
   - 面试官：还有呢？
   - 面试官：在工作中，表索引你怎么设计的？
   - 面试官：那索引建立越多，查询效率就越高吗？

## Redis专栏
1. [精通Redis主从数据同步懂吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis主从/精通Redis？主从数据同步懂吗.md)
   - 面试官：我看你们项目用的Redis主从，数据同步了解吗？
   - 面试官：按你这么说，数据同步后主服务器某个键删除了，数据又不同步了怎么办？
   - 面试官：如果主从服务器断线呢？还是用的RDB来同步吗？
   - 面试官：那主服务器怎么知道断线期间执行了哪些命令？
   - 面试官：知道服务器运行ID吗？
   - 面试官：Redis心跳检测知道吧？
2. [听说你精通Redis来说说Redis哨兵](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis哨兵/听说你精通Redis？来说说Redis哨兵.md)
   - 面试官：Redis哨兵知道吧？
   - 面试官：嗯然后呢？
   - 面试官：你说说是怎么检测Redis主从服务器的下线状态的？
   - 面试官：有没有A哨兵判断Redis实例下线，但B哨兵判断Redis实例仍然存活的情况呢？
   - 面试官：领头哨兵怎么选举出来的？
   - 面试官：选举出来之后呢，它有什么作用吗？
   - 面试官：知道怎么选举新的Redis主服务器吗？
3. [你说精通Redis来讲讲Redis持久化](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis持久化/你说精通Redis？来讲讲Redis持久化.md)
   - 面试官：知道Redis持久化吗？
   - 面试官：那你说一说Redis生成RDB文件的命令是什么？
   - 面试官：是你的话，你会在什么场景使用什么命令？
   - 面试官：AOF文件生成呢？
   - 面试官：知道AOF文件重写吗？
   - 面试官：那照你这么说，只会保存创建命令，那每个键的创建只有一条命令对吧？
   - 面试官：那你说说AOF重写过程中，有新的创建请求进来怎么办？
4. [Redis过期键删除策略是什么](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis数据库与内存回收策略/Redis过期键删除策略是什么？.md)
   - 面试官：Redis的数据库知道吧？
   - 面试官：那数据库的键空间呢？
   - 面试官：一个键要怎么设置过期时间？
   - 面试官：那键的过期时间知道用什么存储吗？
   - 面试官：键的过期删除策略是什么？
   - 面试官：Redis还有什么策略可以释放内存？
5. [你说说Redis五大基本数据类型](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis数据类型/你说说Redis五大基本数据类型？.md)
   - 面试官：Redis什么这么快？
   - 面试官：你说说Redis五大基本数据类型？
   - 面试官：有利用过有序集合开发过什么功能吗？
   - 面试官：有序集合用什么数据结构来实现？
   - 面试官：那有序集合为什么要使用字典和跳跃表？
   - 面试官：压缩列表呢？
6. [知道Redis集群和Redis主从有什么区别吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/Redis专栏/Redis集群/知道Redis集群和Redis主从有什么区别吗.md)
   - 面试官：Redis多机数据库有什么部署方式？
   - 面试官：那他们有什么区别？
   - 面试官：那Redis集群怎么实现负载均衡的？
   - 面试官：要是热点数据都是某个Redis节点的槽，负载均衡不是没用了？
   - 面试官：还有其他方法吗?
   - 面试官：集群里没有哨兵，那他们怎么选举主节点的？
   - 面试官：最后问你一个，集群里节点怎么进行故障检测的？

## JVM专栏
1. [你说说都有哪些垃圾回收器](https://github.com/hdgaadd/JavaGetOffer/blob/master/JVM专栏/JVM垃圾回收/你说说都有哪些垃圾回收器.md)
   - 面试官：JVM为什么不采用引用计数法？
   - 面试官：那你讲讲可达性分析算法？
   - 面试官：垃圾回收器回收的是哪个区域？
   - 面试官：那永久代不会进行垃圾回收对吧？
   - 面试官：你说说都有哪些垃圾回收器？
   - 面试官：你说的CMS为什么有较短的停顿？
   - 面试官：那它有什么缺点？
   - 面试官：你说G1是CMS的升级版，为什么？
   - 面试官：垃圾回收算法都有什么？
   - 面试官：复制算法可以怎么优化吗？

## WebSocket专栏
1. [WebSocket技术](https://github.com/hdgaadd/JavaGetOffer/blob/master/WebSocket专栏/WebSocket技术/WebSocket技术.md)
   - 面试官：有了解过WebSocket吗？
   - 面试官：为什么WebSocket可以减少资源消耗？
   - 面试官：有没动手实践过WebSocket？
   - 面试官：那客户端怎么发送消息给服务器？



## Kafka专栏

1. [Kafka事务一般在什么场景下使用呢](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka事务/Kafka事务一般在什么场景下使用呢.md)
   - 面试官：生产者重试机制导致Kafka重复消息，知道怎么处理吗？
   - 面试官：Kafka事务，应该知道吧？
   - 面试官：你说的这个场景，不使用事务会有什么问题吗？
   - 面试官：那Kafka事务一般在什么场景下使用呢？
2. [你说说Kafka是怎么保证消息可靠性的](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka消息可靠性/你说说Kafka是怎么保证消息可靠性的？.md)
   - 面试官：知道Kafka高水位吗？
   - 面试官：你说说Kafka是怎么保证消息可靠性的？
   - 面试官：还有呢？
   - 面试官：那要是Kafka消费堆积了你怎么处理？
   - 面试官：知道Kafka控制器吧？
4. [你说说消费者手动提交和自动提交有什么区别](https://github.com/hdgaadd/JavaGetOffer/blob/master/Kafka专栏/Kafka组成模块/你说说消费者手动提交和自动提交有什么区别？.md)
   - 面试官：你先说说Kafka由什么模块组成？
   - 面试官：那分区有什么作用？
   - 面试官：消息生产者的异步回调，知道吧？
   - 面试官：你说说消费者手动提交和自动提交有什么区别？
   - 面试官：那它们都有什么优、缺点？

## ZooKeeper专栏
1. [知道ZooKeeper有什么应用场景吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZooKeeper应用场景/知道ZooKeeper有什么应用场景吗.md)
   - 面试官：知道ZooKeeper有什么应用场景吗?
   - 面试官：你挑一个你比较熟悉的场景讲讲?
   - 面试官：你说的是排他锁，共享锁呢？
   - 面试官：emmmm有没听说过共享锁的羊群效应？
   - 面试官：那怎么解决呢？
   - 面试官：Kafka应用场景呢，知道Kafka是怎么利用ZooKeeper吗？
   - 面试官：你刚刚说到Kafka生产者负载均衡，那消费者负载均衡知道吗？
2. [ZooKeeper为什么还采用ZAB协议](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZooKeeper的ZAB协议/ZooKeeper为什么还采用ZAB协议.md)
   - 面试官：知道ZAB协议吗？
   - 面试官：消息广播的二阶段提交你讲一讲？
   - 面试官：那二阶段提交有什么缺点吗？
   - 面试官：既然怎么多缺点，ZooKeeper为什么还采用ZAB协议？
   - 面试官：那崩溃模式怎么解决这些问题的？
   - 面试官：对了，你刚刚提到的事务中断逻辑是什么？
3. [那ZooKeeper事务ID呢](https://github.com/hdgaadd/JavaGetOffer/blob/master/ZooKeeper专栏/ZooKeeper系统模型/那ZooKeeper事务ID呢？.md)
   - 面试官：你说说ZooKeeper数据模型？
   - 面试官：那ZooKeeper数据节点有几种类型？
   - 面试官：数据节点版本知道吧？
   - 面试官：ZooKeeper事务ID呢？
   - 面试官：ZooKeeper数据变更通知使用什么对象？

## 主流框架
1. [Mybatis缓存有什么问题吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/主流框架/MyBatis技术要点/Mybatis缓存有什么问题吗？.md)
   - 面试官：你说下对MyBatis的理解？
   - 面试官：那SqlSession知道吧？
   - 面试官：Mybatis的缓存有哪几种？
   - 面试官：那Mybatis缓存有什么问题吗？
   - 面试官：Mybatis分页插件是怎么实现的？
2. [知道怎么解决Spring循环依赖吗](https://github.com/hdgaadd/JavaGetOffer/blob/master/主流框架/Spring%20IOC和Spring%20AOP/知道怎么解决Spring循环依赖吗？.md)
   - 面试官：你说下对Spring IOC的理解？
   - 面试官：AOP呢？
   - 面试官：那AOP的原理是什么？
   - 面试官：JDK和CGCLIB动态代理哪个更快？
   - 面试官：知道怎么解决Spring循环依赖吗？
