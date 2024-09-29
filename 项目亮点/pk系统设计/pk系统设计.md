## 1. 直播pk功能设计

### 1.1 pk玩法

直播pk的功能，要设计出来看起来容易，实则一点都不简单。直播pk玩法在抖音、虎牙、斗鱼各大平台都有出现，能帮互联网公司、主播赚不少钱。

南哥先说说pk的玩法是如何如何？它的流程是这样，主播点击申请pk按钮，匹配其他同时申请pk的主播，粉丝通过送礼给心爱的主播提高pk进度条，pk结束后失败的一方主播接受惩罚。但惩罚又有何妨呢，失败的主播也赚到收益了。

看看直播pk的大概界面。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/22b2b76ec783435baeae32840664a44d.png#pic_center)


### 1.2 pk进度条

pk进度条数据我们打算存储到高性能内存数据库Redis，这里使用Redis的Map结构，存储两个pk主播的进度条数据。

```
# Map的k-v结构
pk:progress:pk_id = [{主播A : 100}, {主播B : 90}]
```

但进度条数据主要是提供给在pk开始后才进来直播间的观众，这类人进行直播间后，客户端调用pk进度的查询接口，获取最新的pk进度条。

```java
// 查询pk进度条接口
public Map<Object, Object> getPKProgress(String pkId) {
    String pkProgressKey = "pk:progress:" + pkId;
    return redisTemplate.opsForHash().entries(pkProgressKey);
}
```

而处于直播间的用户的**进度条增加**，我们给他设计为WebSocket数据实时推送，只要主播的进度有增加，把增加的数值推送到所有在pk直播间的用户。

但有个问题，如果刚进来的观众第一次进来直播间后，他获取了最新的pk进度。此时刚好某个主播的pk进度增加，但由于是新进来的观众，WebSocket数据推送不到这个最新用户，**怎么办**？

这涉及到数据一致性的问题！我们可以在用户进入直播间后，每隔一段时间调用以上的接口，获取pk最新进度条，进行**数据纠正**。

同时，在pk结束后，仍然要调用一次查询接口，确保不会出现这个情况：欸，主播你的分数明明比她高，怎么输了呢？这个情况还是数据不一致的问题。

### 1.3 pk匹配

主播点击pk申请按钮，我们把主播id与直播间信息加入到pk匹配池。

这个pk池子我们依然利用Redis，采用Redis五大基本数据类型之一：Zset。Zset的元素存储主播id与直播间id，元素的score存储主播的pk积分。那Zset会根据主播的积分进行顺序排序。

后面就是匹配算法的设计了，通过匹配算法 + Zset主播的积分，挑选出积分相近的两个pk主播进行匹配。

```
# Zset结构：
pk:matching_pool = [{anchor_id_1_room_id_1 : 100}, {anchor_id_2_room_id_2 : 110}]
```

南哥上面这几个关键数据结构都存储在Redis，我们要保证Redis的高可用性。那用Redis集群可以吗？

如果采用这种Redis架构，因为Redis集群把键值分为**16384**个槽给到各个集群节点，建议给集群里每个节点配上从节点，即`集群架构搭配主从模型`。防止某个集群节点失效了导致数据全部丢失。

### 1.4 pk倒计时

每场pk都有倒计时，这里我们在pk匹配成功时就在Redis里设置一个倒计时键值对，该键值对的初始值是本场pk的总pk时间。

```java
// 设置pk倒计时
public void setPKCountdown(String pkId, int totalTime) {
    String pkCountdownKey = "pk:countdown:" + pkId;
    
    // 在 Redis 中设置倒计时
    redisTemplate.opsForValue().set(pkCountdownKey, totalTime, totalTime, TimeUnit.SECONDS);
}
```



### 1.5 pk流程设计

总结上文，清晰地梳理下整个pk流程的设计。

> 主播发送pk申请 -> 匹配 -> 成功则WebSockett推送成功通知、倒计时信息 -> 创建监控线程 ->  pk中 -> pk结算

首先两个主播在客户端点击pk申请按钮，申请请求到达后端，客户端告知主播：`pk匹配中`。

主播申请后，后端服务把主播加入pk匹配池。而专门用于配对pk主播的微服务持续处理pk池子中请求，合适则把两个主播进行pk配对，同时把两个主播**踢出**pk匹配池。

当然匹配成功后还有后续流程需要处理，配对成功后使用WebSocket服务端主动推送技术，实时告知主播包括直播间用户：pk已配对成功。

同时，在Redis创建上文`1.3节`的pk倒计时，同步也推送给主播包括观众。

在后台，我们还需要创建一个**监控线程**，来去监控pk是否结束，当结束时进行pk结算，告知观众与主播究竟哪一方获胜。

监控线程取自监控线程池子，方便**线程复用**，线程池的最大好处就是减少了系统频繁创建、销毁线程带来的资源消耗。

```java
// 从监控线程池获取一个线程
public void monitorPKProgress(String pkId) {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    
    scheduler.scheduleAtFixedRate(() -> {
        // 检查倒计时是否结束
        String countdownKey = "pk:countdown:" + pkId;
        Integer countdown = (Integer) redisTemplate.opsForValue().get(countdownKey);
        if (countdown != null && countdown <= 0) {
            System.out.println("PK结束，开始结算...");
            // 调用结算逻辑
        }
    }, 0, 1, TimeUnit.SECONDS);  // 每秒监控一次
}
```

### 1.6 WebSocket长连接问题

pk匹配成功通知、pk进度条增加等，都需要WebSocket技术去实时推送数据。

但一个直播间成千上万个观众，大多数观众的客户端都长连接着不同的WebSocket服务器。要推送数据时，怎么知道要从哪些WebSocket服务器进行推送？？

（1）集中式连接状态管理

有一些公司WebSocket服务器只有固定一台，推送数据时绑定这台服务器的**ip**即可，也不需要处理我们讨论的问题。

我们把用户的连接信息，包括用户id、长连接的WebSocket服务器地址，都存储在Redis中进行**集中式的状态管理**。当要推送数据时，获取用户所在WebSocket服务器地址即可。

（2）广播推送

进行数据推送时，对所有WebSocket服务器进行消息广播。接收到广播消息后，服务器检查本地是否有该用户的连接信息，如果有则进行消息推送。

（3）WebSocket集群框架

如果WebSocket框架使用的是[Socket.IO](https://github.com/socketio/socket.io)的话，以上的问题已经有很好的集群解决方案了。[Socket.IO Redis adapter](https://github.com/socketio/socket.io-redis-adapter)适配器可以将事件广播到多个单独的 socket.io 服务器节点，用于在多台WebSocket服务器共享连接状态。