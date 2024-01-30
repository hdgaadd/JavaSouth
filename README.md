# 📚structure

```lua
universe
├── concurrency -- 多线程
     ├── basic
     ├── business
     ├── juc -- juc工具类
     ├── readwritelock
     ├── threadlocal
     └── transfer -- 转账死锁
├── design-patterns -- 设计模式
     ├── adapter -- 适配器
     ├── build -- 建造者
     ├── chain -- 责任链
     ├── command -- 命令
     ├── interceptor -- 拦截过滤器
     ├── singleton -- 单例
     ├── strategy -- 策略
     └── template -- 模板方法
├── jdk -- JDK的Api使用
     ├── automic
     ├── beanMapping -- bean映射
     ├── book
     ├── bytecode
     ├── cache
     ├── callback -- 回调函数
     ├── debug
     ├── empty
     ├── exception
     ├── function
     ├── interesting
     ├── jvm
     ├── log -- 日志
     ├── mysql
     ├── network -- 网络
     ├── oom -- 内存溢出
     ├── optional -- 判空处理
     ├── reagency
     ├── stream -- stream流
     ├── string
     ├── system
     └── time
├── middleware -- 开源中间件使用
     ├── Dubbo -- Dubbo
     ├── Elasticsearch
     ├── Gateway -- 网关
     ├── Knife4j -- swagger增强包
     ├── monitor
     ├── MybatisPlus -- MybatisPlus
     ├── Netty
     ├── okhttp
     ├── Redis -- Redis
     ├── RocketMQ -- RocketMQ
     ├── ShardingSphere
     ├── SpringBoot -- SpringBoot
          ├── extension -- SpringBoot扩展点
          └── starter -- SpringBoot启动器
     ├── SpringBootWebSocket -- WebSocket
     ├── SpringSecurity -- 认证授权
     ├── SpringSecurityOAuth2
     └── WebFlux
├── deploy -- 业务解决方案
     ├── concurrency-method -- 分布式锁方法
     ├── config-switch -- 配置开关
     ├── data-migration -- 数据迁移方案
     ├── preheat-limit
     ├── push-limit -- 消息重推
     ├── refresh-cache -- 通知集群节点刷新缓存
     ├── repeated-submit-intercept -- 防重复提交
     ├── seckill -- 秒杀系统设计
     ├── timing -- 定时系统设计
     └── z-resource
└── z-others -- 其他
     ├── ans -- 暂存箱
     └── resource -- resource
```
