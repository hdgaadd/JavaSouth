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
