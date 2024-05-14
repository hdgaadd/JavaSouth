> 你说说Redis五大基本数据类型？





## 1. Redis底层数据结构

> ***面试官：你说说Redis有什么底层数据结构支持？***

好的，我了解的主要有：

1. 字典
2. 跳跃表
3. 链表，Redis采用了有前置后置节点的**双端链表**，**列表键List**就是采用这种结构。

### 1.1 字典

> ***面试官：先讲讲你对字典的理解？***

好的，字典其实是一个集合里包含了多个键值对，类似于Java的**HashMap**。

它的底层包含了**两个**哈希表，一个平常使用，一个在迁移扩展哈希表**rehash**时使用。

迁移完成后，原先日常使用的旧哈希表会被**清空**，新的哈希表变成日常使用的。

### 1.2 字典和哈希对象

> ***面试官：那字典和Redis的哈希对象不是没什么区别？***

有区别的，**面向对象**不同。

字典是Redis**内部**的底层数据结构支持，而Redis的哈希对象是**对外**提供的一种对象。

## 2. 跳跃表

> ***面试官：跳跃表呢？***

它的底层结构类似于一个值 + 保存了**指向其他节点的level数组（层）**，而这个level数组就是用来**加快访问**其他节点的速度。

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
    // 成员对象
    robj *obj;
} zskiplistNode;
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/401f80f32ee942a69066d3beeedbd3ef.png#pic_center)

### 2.1 有序集合数据结构

> ***面试官：那有序集合为什么要同时使用字典和跳跃表来实现？***

这个设计主要是考虑了**性能**因素。

1. 如果单纯使用字典，**查询时**的效率很高是O(1)，但执行类似ZRANGE、ZRNK时，**排序性能低**。每次排序需要在内存上对字典进行排序一次，同时消耗了额外的O(n)内存空间
2. 如果单纯使用跳跃表，**查询性能**又会从O(1)上升到了O(logN)

所以Redis集合了两种数据结构，同时这两种数据结构通过**指针来共享变量**也不会浪费内存。

```c
typedef struct zset { // 有序集合
    zskiplist *zsl; // 跳跃表
    dict *dict; // 字典
} zset;
```

## 3. 整数集合和压缩列表

> ***面试官：Redis为了节约内存采用了什么数据结构知道吗？***

噢噢知道的。我了解的有两种。

1. 当**列表键**只有**少数几个**，**且都是**整数型的话，Redis会改用**整数集合**进行存储。
2. 当列表键只有少数几个，且都是整数型或长度短的字符型的话，Redis会改用**压缩列表**进行存储。

```shell
# 可以看到创建了列表键类型，但实际存储类型是ziplist

redis＞ RPUSH lst 1 3 5 10086 "hello" "world"
(integer)6
redis＞ OBJECT ENCODING lst
"ziplist"
```
