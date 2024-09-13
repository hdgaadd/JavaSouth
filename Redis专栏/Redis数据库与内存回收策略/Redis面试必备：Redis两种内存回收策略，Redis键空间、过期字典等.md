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