## 1. ZooKeeper数据模型
### 1.1 ZooKeeper数据节点

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

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/8638d0e764324b1dab306e5a87ab2bd4.png#pic_center)


另外ZooKepper这种`斜杠/`作为路径分隔符正好和**Windows相反**，Windows使用的是`反斜杠\`。

```shell
# Windows路径示例
C:\Java\jdk1.8.0_311
```

### 1.2 数据节点类型

ZooKeeper一共有四种节点类型，但从整体来看主要是持久节点类型、临时节点类型这两种，另外两种类型只是在以上两种节点类型基础上增加了顺序的特性。大家这样理解会更方便记忆~

1. **持久节点**：这种数据节点一旦背创建后，就会一直存在于ZooKeeper服务器上，除非对该数据节点执行删除操作。
2. 持久顺序节点：刚刚和大家说了，该节点类型就是在持久节点基础上增加了顺序的特性。如果在持久顺序节点类型的父节点创建子节点，ZooKeeper会为该子节点名加上一个数字后缀来维护子节点的顺序。
3. **临时节点**：临时节点比较特殊，它的生命周期是和**客户端会话**绑定在一起的。这个客户端可以是连接ZooKeeper的某一个终端命令窗口，也可以是连接ZooKeeper的某一个Spring服务线程。如果客户端会话失效了，那这个临时节点就会被自动清除。
4. 临时顺序节点：在临时节点的基础上添加了顺序特性。

另外大家记住一点，**临时节点只能作为叶子节点**，是不能在临时节点下面创建任何子节点的。原因大概是临时节点子节点没有存在的意义，创建子节点的场景大多是基于持久节点的场景，这种设计也可以防止对临时节点的误用。

### 1.3 数据节点的版本

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
```

```shell
控制台打印：
[ INFO ]the thread called cas0 get val is 0
[ INFO ]the thread called cas1 get val is 0
[ INFO ]the thread called cas0 operated
[ INFO ]the thread called cas1 get val is 1
[ INFO ]the thread called cas1 operated
[ INFO ]current thread name: main , the value of val: 2
```

### 1.4 事务ID

我们熟悉的数据库事务一般是包含**对数据库状态的读写操作**，数据库事务具有ACID特性：原子性、一致性、持久性、隔离性。数据库事务这块大家有不懂的可以看我的往期文章。

但ZooKeeper的事务和数据库事务大相径庭。ZooKeeper事务一般是包括对**数据节点**的创建、删除、更新，也包括客户端会话创建、失效情况对**临时节点**的影响。

每一个事务请求，ZooKeeper都会为其分配一个全局唯一的事务ID，我们称它为**ZXID**。所以ZXID也可以间接反映出对数据节点操作的全局顺序，这个全局顺序在Follower服务器对Leader服务器的数据复制上相当重要，可以用来保证数据的一致性。

## 2. Watcher机制

ZooKeeper拥有分布式通知的功能，这个功能是基于**Watcher机制**来实现的。一个Watcher对象就像一个订阅者，当订阅的主题状态发生变化，就会通知Watcher订阅者作出一定动作。

Watcher机制的工作流程，首先是客户端向ZooKeeper服务器注册Watcher通知，接着会将Watcher对象存储在客户端本身的**WatchManager**中。当ZooKeeper服务器触发Watcher事件后会向客户端发起通知，客户端就从本身的WatchManager取出对应的Watcher对象来执行**回调操作**。

Watcher机制的大致流程大家可以参考下图：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/59561ea4ca5b496a96604a276320ff6a.png#pic_center)
