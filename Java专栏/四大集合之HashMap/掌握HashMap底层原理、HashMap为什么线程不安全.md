## 1. HashMap内部结构

> ***面试官：你说下HashMap的内部结构？***

好的面试官。

HashMap内部存储数据的对象是一个实现Entry接口的**Node数组**，也称为哈希桶`transient Node<K,V>[] table`，后面我们称Node数组为Entry数组。Entry数组初始的大小是**16**。

Node节点的内部属性key、value分别代表键和值，hash代表key的hash值，而next则是指向下一个链表节点的指针。

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

### 1.1 键值的添加流程

> ***面试官：那一个键值是怎么存储到HashMap的？***

首先会调用hash方法**计算key的hash值**，通过key的hashCode值与key的hashCode高16位进行异或运算，使hash值更加随机与均匀。

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

再通过该hash值与Entry数组的长度**相与**，得到要存储到的索引位置`int index = (table.length - 1) & hash`。如果该索引位置是空的，会把键值直接添加到表头，如果哈希冲突了则会用**链表法**形成一条链表。

数据添加后，会判断当前容量是否到达了threshold阈值，threshold等于负载因子`loadFactor * table.length`。负载因子默认是**0.75**，threshold第一次扩容时为0.75 * 16 = **12**。

如果到达阈值了则会对Entry数组进行扩容，扩容成为原来**两倍容量**的Entry数组。

### 1.2 红黑树

> ***面试官：HashMap链表还会转换成什么？***

当链表长度 >= 8时，会把链表转换为**红黑树**。

是这样的，HashMap的链表元素如果数量过多，查询效率会越来越低，所以需要将链表转换为其他数据结构。而二叉搜索树这种数据结构是绝对的子树平衡，左节点比父节点小，右节点比父节点大，在极端情况会退化为**链表结构**。

而红黑树放弃了绝对的子树平衡，转而追求的是一种大致平衡，在极端情况下的数据查询效率更优。

```java
# 红黑树数据结构
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;
}
```

## 2. 线程安全的Map

### 2.1 线程不安全的HashMap

> ***面试官：HashMap为什么线程不安全？***

一、在多线程环境下，可能会出现**数据覆盖**的问题。

例如前面提到如果索引位置为空则直接添加到表头，如下面源码所示。此时如果有两个线程同时进入if语句，线程A把数据插入到表头，接着线程B把他的数据覆盖到表头，这样就产生了数据覆盖的问题，线程A的数据相当于消失了。

```java
if ((p = tab[i = (n - 1) & hash]) == null)
	tab[i] = newNode(hash, key, value, null);
```

二、另外在多线程环境下，还可能会出现**数据不一致**的问题。

在插入数据后，判断是否需要扩容是以下源码。

```java
if (++size > threshold)
	resize();
```

若两个线程同时进入了++size代码块，对size的修改分为三个步骤：读取、计算、赋值。线程A、线程B同时读取了size是0，两者计算时size都为1，后面赋值时把size = 1赋值给了size两次。

但实际上期望的size应该是2，此时就出现了数据不一致的问题，Entry数组的容量会出现错误。

### 2.2 线程安全的ConcurrentHashMap

> ***面试官：有线程安全的Map吗？***

有的，JDK提供了线程安全的ConcurrentHashMap。

（1）ConcurrentHashMap对于底层Entry数组、size容量都添加了可见性的修饰，保证了其他线程能实时监听到该值的**最新修改**。

```java
transient volatile Node<K,V>[] table;
private transient volatile int sizeCtl;
```

（2）在添加键值的操作，对**元素级别**进行加锁。若该索引位置不存在元素，则使用乐观锁CAS操作来添加值，而CAS是原子操作，不怕多线程的干扰。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d628abadbd05b72922c24f36f26026ab.png#pic_center)


若该索引位置存在元素，则使用**synchronized**对该索引位置的**头节点**进行加锁操作，保证**整条链表**同一时刻只有一个线程在进行操作。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/850b5e88feabd1b1de40fbf7874a3147.png#pic_center)


（3）另外在JDK7版本中ConcurrentHashMap的实现和JDK8不同。

JDK7版本的数据结构是大数组Segment + 小数组HashEntry，其中小数组HashEntry的**每个元素**是一条链表，**一个Segment**是一个HashEntry数组。对每个Segment即每个分段，使用ReentrantLock进行加锁操作。

可以看到JDK8版本相比JDK版本的实现**锁粒度**更小，且JDK8版本的链表还可以升级为查询效率高的红黑树，所以JDK7版本的ConcurrentHashMap目前被JDK8版本的代替了。

### 2.3 HashTable和ConcurrentHashMap区别

> ***面试官：HashTable和ConcurrentHashMap有什么区别吗？***

HashTable也是线程安全的Map，不过它不仅对修改操作添加加锁操作，获取操作也进行了加锁。

```java
public synchronized V put(K key, V value)
public synchronized V get(Object key)
```

而ConcurrentHashMap没有对get进行加锁处理，不适用于**强一致性**的场景。例如要求获取操作必须严格获取到最新的值，这种强一致性场景则更适合使用HashTable。

另外HashTable和HashMap、ConcurrentHashMap还有以下不同。

1. HashTable继承了Dictionary，而HashMap、ConcurrentHashMap继承了AbstractMap
2. HashTable初始容量为11，HashMap、ConcurrentHashMap是16
3. HashTable扩容为原来的**2n+1**，HashMap、ConcurrentHashMap是扩容为原来的**2n**
