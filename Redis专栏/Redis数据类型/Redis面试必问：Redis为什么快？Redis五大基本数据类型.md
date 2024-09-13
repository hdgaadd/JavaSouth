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