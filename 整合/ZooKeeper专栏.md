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
