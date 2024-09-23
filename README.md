# Java学习/进阶/面试指南

> South译为南部，这是一份南友们学习进阶Java的《Java学习/进阶/面试指南》
>
> 该指南涵盖了在大厂工作的Javaer都不会不懂的核心知识、面试重点。相信一定对你学习Java、成为更好的Java选手有所帮助，愿南友们在Java成长路上不迷茫。

GitHub个人主页的⭐️Star栏有**收藏夹**的功能，点击⭐️Star收藏起来方便找得到~

南哥正在一步步完善中，一个人力量难免有限，大家发现有什么错误提个PR吧❤

# 💰JavaSouth学习路线

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/d5cfd3a590874bb3bca8b33243fa4e1e.png#pic_center)

# ① Java技术体系

## ☕ Java

- ### 基础
  - [ArrayList是考的最多的，熟悉List集合的常见类：ArrayList、Vector、LinkedList](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/四大集合之List/面试官没想到一个ArrayList，我都能跟他扯半小时.md)
  - [掌握Set集合使用及原理：HashSet、LinkedHashSet、TreeSet](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/四大集合之Set/四大集合之Set.md)
  - [掌握HashMap底层原理、HashMap为什么线程不安全](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/四大集合之HashMap/掌握HashMap底层原理、HashMap为什么线程不安全.md)
  - [掌握Queue集合常用类：LinkedList、ArrayDeque、PriorityQueue](https://github.com/hdgaadd/JavaGetOffer/blob/master/Java专栏/四大集合之Queue/四大集合之Queue.md)
  - [笔试题经常出现，掌握接口和抽象类的区别：静态分派、访问修饰符](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/接口和多态/接口和多态.md)
  - [笔试题出现频率高，三元运算符、自动类型提升、byte溢出、i++自增问题](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/数据类型和程序运算/数据类型和程序运算.md)
  
- ### IO流
  - [可能是最漂亮的Java IO流详解](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/Java%20IO流/可能是最漂亮的Java%20IO流详解.md)
  - [掌握NIO是什么？零拷贝的概念](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/Java%20NIO/掌握NIO是什么？零拷贝的概念.md)
  
- ### 多线程
  - [并发编程面试必备：如何创建线程池、线程池拒绝策略](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/线程池/并发编程面试必备：如何创建线程池、线程池拒绝策略.md)
  - [并发编程面试必备：指令重排序、volatile可见性原理及局限性](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/volatile与重排序/并发编程面试必备：指令重排序、volatile可见性原理及局限性.md)
  - [并发编程面试必备：synchronized原理、锁升级](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/synchronized实现原理/并发编程面试必备：synchronized原理、锁升级.md)
  - [并发编程面试必备：ThreadLocal作用、线程生命周期](https://github.com/hdgaadd/JavaSouth/blob/master/Java专栏/线程通信/并发编程面试必备：ThreadLocal作用、线程生命周期.md)
  
- ### 网络编程
  - [了解掌握WebSocket相比传统长轮询的优点](https://github.com/hdgaadd/JavaSouth/blob/master/WebSocket专栏/WebSocket技术/了解掌握WebSocket相比传统长轮询的优点.md)
  - [Nginx必备知识点：反向代理、正向代理、负载均衡](https://github.com/hdgaadd/JavaSouth/blob/master/Nginx%E4%B8%93%E6%A0%8F/Nginx.md)
  
- ### 设计模式
  - [用 Java 实现的设计模式项目，在GitHub上拥有88k⭐️Star⭐️](https://github.com/iluwatar/java-design-patterns)


## 💾MySQL

- ### 面试必问
  - [MySQL面试必问：索引的类型、Explain分析SQL、索引失效](https://github.com/hdgaadd/JavaSouth/blob/master/MySQL专栏/MySQL索引/MySQL面试必问：索引的类型、Explain分析SQL、索引失效.md)
  - [MySQL面试必问：SQL如何优化、索引如何设计](https://github.com/hdgaadd/JavaSouth/blob/master/MySQL专栏/SQL语句优化/MySQL面试必问：SQL如何优化、索引如何设计.md)
  - [MySQL面试必问：MySQL事务四大特性、事务隔离级别](https://github.com/hdgaadd/JavaSouth/blob/master/MySQL专栏/MySQL事务/MySQL面试必问：MySQL事务四大特性、事务隔离级别.md)

- ### 进阶知识点
  - [MySQL高阶知识：主从复制步骤、三种二进制日志格式等](https://github.com/hdgaadd/JavaSouth/blob/master/MySQL专栏/MySQL主从复制/MySQL高阶知识：主从复制步骤、三种二进制日志格式等.md)
  - [掌握MySQL高级特性：分区表、视图、全文索引](https://github.com/hdgaadd/JavaSouth/blob/master/MySQL专栏/MySQL高级特性/掌握MySQL高级特性：分区表、视图、全文索引.md)
  - [MySQL官方文档，十分详细](https://dev.mysql.com/doc/)


## 🎈Redis

- ### 重要知识点
  - [Redis面试必问：Redis为什么快？Redis五大基本数据类型](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis数据类型/Redis面试必问：Redis为什么快？Redis五大基本数据类型.md)
  - [Redis面试必备：Redis两种内存回收策略，Redis键空间、过期字典等](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis数据库与内存回收策略/Redis面试必备：Redis两种内存回收策略，Redis键空间、过期字典等.md)
  - [掌握Redis持久化：RDB文件、AOF文件、AOF重写](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis持久化/掌握Redis持久化：RDB文件、AOF文件、AOF重写.md)
  - [Redis重要知识点：哨兵是什么哨兵如何选择Redis主服务器](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis哨兵/Redis重要知识点：哨兵是什么？哨兵如何选择Redis主服务器.md)
  - [Redis官网](https://redis.io/)、[Redis创始人GitHub](https://github.com/antirez)
  
- ### Redis多机
  
  - [掌握Redis集群概念、集群分片、重新分片，集群如何选举主节点](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis集群/了解Redis集群概念，集群如何选举主节点.md)
  - [Redis主从数据同步过程：命令传播、部分重同步、复制偏移量等](https://github.com/hdgaadd/JavaSouth/blob/master/Redis专栏/Redis主从/Redis主从数据同步过程：命令传播、部分重同步、复制偏移量等.md)


## ✏️主流框架

- ### 面试必备
  - [Spring面试必备：Spring IOC和AOP的理解、如何解决Spring循环依赖](https://github.com/hdgaadd/JavaSouth/blob/master/主流框架/Spring%20IOC和Spring%20AOP/Spring面试必备：Spring%20IOC和AOP的理解、如何解决Spring循环依赖.md)
  - [熟悉Spring MVC工作流程，掌握Spring MVC常见注解](https://github.com/hdgaadd/JavaSouth/blob/master/主流框架/SpringMVC技术要点/熟悉Spring%20MVC工作流程，掌握Spring%20MVC常见注解.md)
  - [MyBatis面试必问： Mybatis一、二级缓存及其优缺点](https://github.com/hdgaadd/JavaSouth/blob/master/主流框架/MyBatis技术要点/MyBatis面试必问：%20Mybatis一、二级缓存及其优缺点.md)
  
- ### 源码与文档
  - [Spring Framework源码](https://github.com/spring-projects/spring-framework)、[Spring Boot源码](https://github.com/spring-projects/spring-boot)
  - [Spring Framework文档](https://spring.io/projects/spring-framework#learn)、[Spring Boot文档](https://spring.io/projects/spring-boot#learn)
  - [Spring Initializr快速搭建Spring项目](https://start.spring.io/)
  - [Mybatis 3源码](https://github.com/mybatis/mybatis-3)、[MyBatis 3文档](https://mybatis.org/mybatis-3/)

## 💻JVM

- [你需要了解掌握的 7 种垃圾回收器与 4 种垃圾回收算法](https://github.com/hdgaadd/JavaSouth/blob/master/JVM专栏/JVM垃圾回收/掌握垃圾回收器与四种垃圾回收算法.md)
- [掌握JVM内存区域：堆内存、本地方法栈、虚拟机栈、方法区、程序计数器](https://github.com/hdgaadd/JavaGetOffer/blob/master/JVM专栏/JVM内存区域/JVM内存区域.md)

## 🕶Kafka

- [Kafka高水位面试经常问，Kafka如何保证消息可靠性](https://github.com/hdgaadd/JavaSouth/blob/master/Kafka专栏/Kafka消息可靠性/掌握Kafka高水位，Kafka如何保证消息可靠性.md)
- [熟悉Kafka组成模块、Kafka消息提交的方式及优缺点](https://github.com/hdgaadd/JavaSouth/blob/master/Kafka专栏/Kafka组成模块/熟悉Kafka组成模块、Kafka消息提交的方式及优缺点.md)
- [Kafka事务一般在什么场景下使用呢](https://github.com/hdgaadd/JavaSouth/blob/master/Kafka专栏/Kafka事务/Kafka事务一般在什么场景下使用呢.md)

## ⛳ZooKeeper

- [ZooKeeper面试必备：ZooKeeper4种数据节点类型、了解事务ID](https://github.com/hdgaadd/JavaSouth/blob/master/ZooKeeper专栏/ZooKeeper系统模型/ZooKeeper面试必备：ZooKeeper4种数据节点类型、了解事务ID.md)
- [掌握ZooKeeper的业务使用场景，ZooKeeper如何实现分布式锁](https://github.com/hdgaadd/JavaSouth/blob/master/ZooKeeper专栏/ZooKeeper应用场景/掌握ZooKeeper的业务使用场景，ZooKeeper如何实现分布式锁.md)
- [掌握ZooKeeper的二阶段提交及其优缺点](https://github.com/hdgaadd/JavaSouth/blob/master/ZooKeeper专栏/ZooKeeper的ZAB协议/掌握ZooKeeper的二阶段提交及其优缺点.md)


## 📂 数据结构与算法

- [收集真实的面试算法题，大家尽量都做完，多多了解企业喜欢考哪类算法题](https://github.com/hdgaadd/JavaSouth/blob/master/面试必备/收集真实的面试算法题，尽量都做完了解企业喜欢考哪类算法题.md)

## 🎨其他

- [南哥推荐的书籍，也是我读过的（目前一共41本经典书籍，精通Java编程、程序员内核修养、熟悉主流框架、算法、MySQL、Redis、Linux、中间件原理、学会用产品思维去思考需求、架构师阅读清单）](https://github.com/hdgaadd/JavaSouth/blob/master/其他/个人阅读书籍清单（目前一共41本）.md)
- [Linux创始人Linus Torvalds](https://github.com/torvalds)、[孵化一众顶级开源产品的Apache社区](https://github.com/apache)



# ② 项目亮点

1. ### 直播弹幕设计

   [设计了简单高效的弹幕系统！老板直接加薪](https://juejin.cn/post/7415911487608324136)

2. ### 直播礼物系统设计

   [🔥产品：直播送礼延迟这么大，你就不能快点吗](https://juejin.cn/post/7415912231840546851)

# ③ 其他

- ### ⭐互联网公司真实面经

  1. [好好好，总结了：2024腾讯音乐面经汇总（超全）](https://www.nowcoder.com/discuss/607258148675518464)
  2. [淘天一面](https://www.nowcoder.com/feed/main/detail/5e413749999840b68dce6d456fb47fb0)
    3. [4.2 美团暑期实习后端二面](https://www.nowcoder.com/feed/main/detail/4e640dcd7e464417aee8f718daa3a30a)
    4. [腾讯一面挂](https://www.nowcoder.com/feed/main/detail/d5bea18667734e96b11c9cc239594068)
    5. [字节跳动 后端 二面](https://www.nowcoder.com/feed/main/detail/9ff15de3b3594166afcd543ddae89149)
    6. [字节三面全面经](https://www.nowcoder.com/feed/main/detail/8d5b0fca1a974aa298f74fb13f8cf0e5)
    7. [快手一面凉经](https://www.nowcoder.com/feed/main/detail/72321cd30d1c4df5b5d1fa3760ac80d3)
    8. [字节抖音电商 后端开发 一面面经](https://www.nowcoder.com/discuss/642293651615285248)
    9. [5000字说透简历和面试核心要点](https://www.nowcoder.com/discuss/631096158550581248)
    10. [【暑期实习总结】 无法去当腾孝子了TAT](https://www.nowcoder.com/discuss/610443613394219008)
    11. [没必要焦虑，其实吧，最终都能找到工作的！](https://www.nowcoder.com/discuss/607593679364034560)
    12. [科大讯飞-一面-Java开发工程师](https://www.nowcoder.com/feed/main/detail/78d1e147353b4d01ac771e91002d666b)
    13. [美团一二面](https://www.nowcoder.com/feed/main/detail/9924692f8d5643a58854f2bda208c06f)
    14. [腾讯科技 后端软件开发 一面 纯八股拷打](https://www.nowcoder.com/discuss/643740561152700416)
    15. [美团到店研发平台-秋招一面-Java](https://www.nowcoder.com/discuss/656419072464191488)
    16. [腾讯 Java 一面 寄](https://www.nowcoder.com/discuss/603636072093872128)
    17. [美团一面](https://www.nowcoder.com/discuss/623303735359315968)
    18. [25届非科班游戏客户端春招实习总结](https://www.nowcoder.com/discuss/625743834659430400)
    19. [瑞幸java校招二面（史诗级80min）](https://www.nowcoder.com/feed/main/detail/82c45b288d474ad88e14e02d6fca4d8d)
    20. [美团 到家 一面面经](https://www.nowcoder.com/discuss/604833796063522816)
    21. [豆包后端面经](https://www.nowcoder.com/discuss/655339242951880704)
    22. [诺瓦嵌入式一面](https://www.nowcoder.com/feed/main/detail/6182dc91133643b99a4e0b7645c6f123)
    23. [百度提前批java一面](https://www.nowcoder.com/feed/main/detail/b4695d1f0aec49319c057476093ed25d)
    24. [腾讯WXG后端一面凉经](https://www.nowcoder.com/discuss/600458830874435584)
    25. [北森一面凉经](https://www.nowcoder.com/feed/main/detail/9a15cd0a1ae749168ae654e63f316458)
    26. [美团一二面经分享](https://www.nowcoder.com/feed/main/detail/a392ea54aea448ddad7957745d184d6c)
    27. [字节抖音电商后端日常实习一二三面已oc](https://www.nowcoder.com/discuss/619573767051800576)
    28. [蚂蚁集团 3.25 Java 一面（电话面）](https://www.nowcoder.com/discuss/601794045181997056)
    29. [字节跳动 国际电商 秋招一面](https://www.nowcoder.com/discuss/655162445983461376)
    30. [滴滴二面OC](https://www.nowcoder.com/discuss/642397876919762944)
    31. [腾讯ieg面经](https://www.nowcoder.com/feed/main/detail/db89fc192fe6496e9488ff67923f18b1)
    32. [小红书 电商技术 java一面（凉）](https://www.nowcoder.com/feed/main/detail/26dfce0f1c1944bc9842cd92987dbd5a)
    33. [4.07美团一面](https://www.nowcoder.com/discuss/606620335219150848)
    34. [美团4.9二面面经 已oc](https://www.nowcoder.com/discuss/609746146499584000)
    35. [美团暑期实习一面-软件开发工程师-后端](https://www.nowcoder.com/feed/main/detail/f8e273b76c6a481c98b261bffa280906)
    36. [乐鑫面试凉经-嵌入式软开--25届热乎的](https://www.nowcoder.com/feed/main/detail/a470ad0f940b41dabde4528193ec3342)
    37. [腾讯音乐一面秒挂 2024-04-09](https://www.nowcoder.com/discuss/607253161652269056)
    38. [美团前端一面 —— 面经（已过）](https://www.nowcoder.com/feed/main/detail/21c41c24b831491187d51aecab1b0d7c)
    39. [美团一面4.1(已回人才库)](https://www.nowcoder.com/discuss/604715032043089920)
    40. [bilibili服务端开发实习一面](https://www.nowcoder.com/feed/main/detail/4d867463fa584878bb0e6c1106bed9d8)
    41. [美团嵌入式暑期实习面试经历（已接offer）](https://www.nowcoder.com/discuss/604706683394215936)
    42. [美团一面03.21](https://www.nowcoder.com/feed/main/detail/c1c4769edc12432497e5d0b4cfd32bb6)
    43. [2024届实习+秋招（面试疲惫的时候就懒得记录了）](https://www.nowcoder.com/discuss/637232740932304896)
    44. [菜狗的国企银行运营商面试经验分享 \|国企篇](https://www.nowcoder.com/discuss/615230488470478848)
    45. [美团到家二面](https://www.nowcoder.com/feed/main/detail/5fc00a75ee864e53970238d3d3f25ce5)
    46. [4.24华为暑期实习技术面+主管面](https://www.nowcoder.com/feed/main/detail/603271cbbb60415995e80d2ffae0b80a)
    47. [5.7 暑期实习求职总结](https://www.nowcoder.com/discuss/617325425362067456)
    48. [淘天4.7一面](https://www.nowcoder.com/discuss/606822686634815488)
    49. [淘天一面 3/26   part1](https://www.nowcoder.com/feed/main/detail/f2cde810bb2b4307829c2cfe473557cc)
    50. [字节飞书后端实习面经](https://www.nowcoder.com/feed/main/detail/d0d5d07b391f4a44b15489844f2bf09e)
    51. [美团实习一二面面经](https://www.nowcoder.com/discuss/602820259673899008)
    52. [仿写百度网盘 \| 面试问题总结](https://www.nowcoder.com/feed/main/detail/cfd5531ff5c14b68b5473ab2c455db4c)
    53. [双非本 腾讯WXG暑期已offer \| 附面经](https://www.nowcoder.com/discuss/622040823068176384)
    54. [菜狗的国企银行运营商面试经验分享 \|银行篇](https://www.nowcoder.com/discuss/615225291790639104)
    55. [蚂蚁后端 一面](https://www.nowcoder.com/feed/main/detail/ba3d5255e903421198a84d78be4a67de)
    56. [C++八股文面经总结，收藏起来慢慢背！](https://www.nowcoder.com/discuss/622444716537229312)
    57. [百度-Java研发-提前批一面面经](https://www.nowcoder.com/feed/main/detail/2f7c2c7ff6c84f7680a4d6326d8ae0d7)
    58. [Java面经大三实习生](https://www.nowcoder.com/feed/main/detail/8641faf6243e48e9a0f2141ee245cdba)
    59. [百度二面java已oc](https://www.nowcoder.com/feed/main/detail/24fdda8926444325acbdc9bf0eda9629)
    60. [OPPO秋招 应用开发 一面二面HR面经](https://www.nowcoder.com/feed/main/detail/7812f024c7a24a3ca8c15d225df96cfa)
    61. [快手java春招二面面经](https://www.nowcoder.com/feed/main/detail/6569e64ca40343dab6ababdb8fa6e12b)
    62. [[更新：已OC][发面经攒人品] cv/算法 实习面经](https://www.nowcoder.com/discuss/623651948142620672)
    63. [腾讯二面](https://www.nowcoder.com/discuss/603685607834939392)
    64. [百度提前批一面](https://www.nowcoder.com/feed/main/detail/0feb883030814d5badf90ab7b15736f4)
    65. [腾讯-后台开发-暑期实习面经](https://www.nowcoder.com/discuss/603209336625590272)
    66. [美团二面凉经](https://www.nowcoder.com/feed/main/detail/61c1ec9807d64f32a1d6e97f92f87c2c)
    67. [字节国际电商123面 秋招 已意向](https://www.nowcoder.com/feed/main/detail/aa1ef70a219a493dbb6b357caf37d32a)
    68. [小米java开发一面](https://www.nowcoder.com/feed/main/detail/58613f9375ec4e4ea4de36e9a7a0c2a9)
    69. [滴滴Java日常实习一面凉经](https://www.nowcoder.com/feed/main/detail/2bdc394c8a7845ff9aa4b168fdd69440)
    70. [百度C++开发一面 55分钟](https://www.nowcoder.com/feed/main/detail/2e95f9af07de439ab668f4bcf49afc02)
    71. [诺瓦星云24秋招提前批 一面](https://www.nowcoder.com/feed/main/detail/04e8438379374d26aa3c91a8024f7eb9)
    72. [暑期实习求职过程以及面经分享，回馈牛友~](https://www.nowcoder.com/discuss/619539886093189120)
    73. [腾讯一面凉经 5.16](https://www.nowcoder.com/feed/main/detail/6720cdde74d04f69802ac776088132e0)
    74. [字节前端一二三面经-已意向](https://www.nowcoder.com/discuss/661351242735550464)
    75. [腾讯天美实习一面凉经](https://www.nowcoder.com/feed/main/detail/7fe9d72cb63d489d950a129679645eb7)
    76. [游戏客户端开发日常实习面经](https://www.nowcoder.com/discuss/632369695965929472)
    77. [美团到家一面凉经](https://www.nowcoder.com/feed/main/detail/ceb3dbe1cad14575a92c2f9b3a103671)
    78. [菜狗的国企银行运营商面试经验分享 \|运营商篇](https://www.nowcoder.com/discuss/615236271937478656)
    79. [南京小米-java日常实习 oc](https://www.nowcoder.com/discuss/649223255953264640)
    80. [（意向挂）米哈游游测暑期实习全流程面经](https://www.nowcoder.com/discuss/611179197226024960)
    81. [网易 java开发实习 一面](https://www.nowcoder.com/feed/main/detail/d94acc1939864dfaa82e6c7954e8311b)
    82. [「前端」字节前端一面](https://www.nowcoder.com/discuss/657276315850010624)
    83. [美团 暑假实习 Java后端 已offer](https://www.nowcoder.com/discuss/600645854843670528)
    84. [腾讯光子 游戏客户端（游戏引擎开发方向）面经](https://www.nowcoder.com/discuss/613002354509570048)
    85. [58同城面经](https://www.nowcoder.com/feed/main/detail/cdf49ad951c84e8d94261e95fb1acb09)
    86. [招商银行后端一面](https://www.nowcoder.com/feed/main/detail/b945b162da7d4476b155f4a387da6a02)
    87. [速腾聚创-嵌入式软件一面(1h)](https://www.nowcoder.com/discuss/630367123340009472)
    88. [腾讯云一面面经 3.22](https://www.nowcoder.com/feed/main/detail/9a0619fbffd94ca78be63683720561b1)
    89. [诺瓦星云 嵌入式面经](https://www.nowcoder.com/feed/main/detail/850f9dd903374049bfde82072c2d6108)
    90. [3.21美团暑期拌凉面1h](https://www.nowcoder.com/feed/main/detail/81101dc123db4d2f991d6df4891e330e)

- ### 聊聊实习、求职

  - [写给25届和26届—关于实习我想说的一切_牛客网 (nowcoder.com)](https://www.nowcoder.com/discuss/606310357551820800)
  - [【校招经验】聊聊实习转正的事_牛客网 (nowcoder.com)](https://www.nowcoder.com/discuss/647203588481069056)
  - [（全时间段）暑期租房攻略来啦！全是干货！](https://www.nowcoder.com/discuss/612292410919383040)
  - [找工作要想清楚。。_牛客网 (nowcoder.com)](https://www.nowcoder.com/discuss/656099972554752000)
  - [【经验篇】从入土到入职｜实习秋招的准备建议_牛客网 (nowcoder.com)](https://www.nowcoder.com/discuss/603420182228320256)
  - [国/央企投递全流程经验分享](https://www.nowcoder.com/discuss/633977329722654720)

- ### 👨‍👦普通学历进大厂的真实故事

  1. [二本毕业的我，靠努力终于进入了大厂](https://juejin.cn/post/7395934404261019660)
  2. [无实习无论文无竞赛，靠努力拿到了农行西研的Offer](https://www.nowcoder.com/discuss/577295381487165440)
  3. [非科班二本进大厂的心路历程和总结（腾讯、头条、阿里、京东）](https://juejin.cn/post/6844904111150727181)

- ### 🛒程序员简历

  [程序员要怎么写出一篇高质量的简历](https://github.com/Snailclimb/JavaGuide/blob/2099ea63abf44b9d090e22686aac6d45865f8d1b/面试必备/手把手教你用Markdown写一份高质量的简历.md)

- ### 😻简历辅导 | 面试模拟

  [南哥一对一给南友们简历辅导、面试模拟](https://github.com/hdgaadd/JavaSouth/blob/master/其他/简历辅导、面试模拟.md)

- 🤑程序员接单 | 副业

  > 关注下方南哥的公众号领取吧

- ### 26届实习交流群

  > 群二维码失效的话，可以在下方公众号获取~

  ![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/e9276305b395494bba9c9f8385fc69f6.jpeg#pic_center)

# ✨该开源文档的说明

### 介绍

该开源文档涉及的主要内容包括Java、Redis、MySQL、多线程、Kafka、JVM、ZooKeeper等等Java程序员学习与进阶路上所需掌握的核心知识、面试重点，是Java学习与进阶的一条**学习路线**。

我会持续更新完善。相信能帮助到大家在Java成长路上不迷茫，同时顺利通关面试、拿到理想Offer。

**希望大家能指正存在的一些错误，让文档不断完善，帮助更多的后来者！**

### 关于转载

如果有引用本仓库的文章，请注明转载地址，支持下原创作者的辛苦劳动！

### 最后

本人会利用业余时间持续更新完善这个文档，修正一些错误。

Java学习、进阶之路道阻且长，我们一起抱团努力✊！度过这冰冷的互联网寒冬。

**Never forget， there must be an echo。欢迎关注南哥的公众号：Java进阶指南针**

**公众号里，南哥给南友们争取到有以下福利：**

**（1）公众号里有南哥珍藏整理的大量优秀pdf书籍，南友们回复"书籍"领取吧**

**（2）加入南哥私密的Java学习进阶圈子，向南哥咨询简历、面试、大厂技术、程序员副业等等相关问题**

**（3）公众号持续发表文章，文章关于南友们如何进更好的公司、进大厂需要的技术与技能**

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/15c2e337b56e4cecba23a1f26c4eb47d.jpeg#pic_center)
