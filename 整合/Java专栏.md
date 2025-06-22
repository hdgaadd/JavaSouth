

# 面试官没想到一个ArrayList，我都能跟他扯半小时

## 1. List集合

### 1.1 集合概述

> ***面试官：List集合都知道哪些对象？***

作为四大集合之一的List，在业务开发中我们比较常见的是以下 3 种：ArrayList、Vector、LinkedList，业务开发我们接触最多就是容器类库了，容器类库可以说是面向对象语言最重要的类库。大家看看在工作里你比较熟悉的是哪个？

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/e58fd12731e74a0ea08249642dcdae83.png)


这篇文章南哥打算专注于List集合，后面四大集合之Map、Queue、Set后续再来填坑，比心心♥。

### 1.2 ArrayList

> ***面试官：ArrayList为什么线程不安全？***

普通的数组类型，我们是这么创建的`int[] arr = new int[66]`。数组可以创建固定长度的容量，不会过度浪费资源，但有优点也有缺点。如果长度设置过小，你就会看到一个大大的`ArrayIndexOutOfBoundsException`。

```shell
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 6
	at org.codeman.Test.main(MsgExtractor.java:11)
```

也别把数组说得那么不堪，起码数组是线程安全的，ArrayList却是线程不安全的。另外ArrayList底层的存储容器实际上也是一个Object数组，大家看看以下源码。

```java
    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access
```

那ArrayList为什么线程不安全？原因就在下面源码这个size。

```java
    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
```

ArrayList底层是根据当前size的值来作为新添加元素的下标，南哥假设目前size为0，有线程A、线程B都想要添加一个元素。

线程A在下标0插入A元素，当添加成功后还没有对size进行++。此时CPU调度让线程B运行，线程B也在下标0插入B元素，覆盖了A元素。线程A、B执行到程序末尾对size进行++，此时就有问题了，大家发现了没？

size进行了两次加法变成了2，但却只有一个B元素添加到了下标0位置，后面再添加其他元素下标1也会是空的。

### 1.3 AarrayList面试小tip

另外ArrayList有些小知识点大家也需要记一记，面试官可能照着公司给的面试题稿子问你：

（1）ArrayList初始容量为10。

（2）ArrayList负载因子为1，也代表ArrayList底层数组满了才会扩容。而数组扩容长度为原始长度的**1.5倍**。

（3）ArrayList的扩容时间复杂度为O(n)。



### 1.4 Vector

> ***面试官：知道线程安全的List集合吗？***

Vector和ArrayList的源码说明很相似，都是告诉你它们相比数组来说是一个可调整大小的数组实现，大家看看以下源码注释。

```java
// Resizable-array implementation of the <tt>List</tt> interface.
// List接口的可调整大小数组实现。
```

```java
// The {@code Vector} class implements a growable array of objects.
// Vector类实现了一个可增长的对象数组。
```

那Vector和ArrayList有什么区别？南哥给大家贴下get和set方法的源码就一清二楚，Vector的元素操作都是线程安全性的，每个方法都有`synchronized`进行修饰，而ArrayLiset却是一个线程不安全的List集合。

```java
    // Vector源码
    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    public synchronized E set(int index, E element) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }
```

两者除了线程安全性的区别，在效率上，ArrayList相比Vector来说效率更高。Vector虽然线程安全了，但每个操作方法是同步的，也意味着增加了额外的开销。

一般我们在业务开发也很少使用到Vector，至少南哥还没有在开发中使用过Vector，小伙伴有写过的吗？如果是需要保证线程安全的场景，我一般是在集合的外部方法加上锁机制，或者使用线程安全的List集合，我更多使用的是`CopyOnWriteArrayList`而不是Vector。

### 1.5 Vector面试小tip

（1）Vector初始容量为10。

（2）Vector负载因子为1，也代表Vector底层数组满了才会扩容。而数组扩容长度为原始长度的**2倍**。

（3）Vector的扩容时间复杂度为O(n)。

### 1.6 LinkedList

> ***面试官：双向链表你说说？***

LinkedList是JDK提供的一个双向链表实现，我们来看看官方源码的介绍。

> List和Deque接口的双向链表实现。实现所有可选的列表操作，并允许所有元素（包括null ）。
> 所有操作的执行方式都符合双向链表的预期。索引到列表中的操作将从列表的开头或结尾（以更接近指定索引为准）遍历列表
>
> Doubly-linked list implementation of the {@code List} and {@code Deque} interfaces.  Implements all optional list operations, and permits all elements (including {@code null}).

我们来看看LinkedList的数据结构，节点类型分为头节点和尾节点。

```java
    transient Node<E> first;

    transient Node<E> last;
```

同时每个节点有指向上一个节点的指针和下一个节点的指针。

```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

LinkedList比较重要的知识点是为什么它也是一个线程不安全的List集合实现？这一点和上文介绍的`头节点first`有关。

LinkedList对元素的操作并没有使用`synchronized`进行同步控制，如果现在有两个线程A、B同时要使用addFist添加第一个头节点。当A线程把A元素设置为头节点后，此时的头节点还没有和旧链表建立连接。而线程B执行时又把B元素设置为了头节点，注意！此时A元素被覆盖了。

以上两个线程的两个添加操作最终却只添加了一个元素。

```java
    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    public void addFirst(E e) {
        linkFirst(e);
    }
```



# 掌握HashMap底层原理、HashMap为什么线程不安全

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

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d628abadbd05b72922c24f36f26026ab.png)


若该索引位置存在元素，则使用**synchronized**对该索引位置的**头节点**进行加锁操作，保证**整条链表**同一时刻只有一个线程在进行操作。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/850b5e88feabd1b1de40fbf7874a3147.png)


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

# 

# 四大集合之Queue

## 1. Queue集合

### 1.1 Queue集合概述

> ***面试官：你说说Queue集合都有什么常用类？***

JDK源码对Queue集合是这么解释的，大家看看。

> A collection designed for holding elements prior to processing. 
>
> 专为在处理之前保存元素而设计的集合。

南哥是这么理解的，List集合用于存储常用元素、Map集合用于存储具有映射关系的元素、Set集合用于存储唯一性的元素。Queue集合呢？所有的数据结构都是为了解决业务问题而生，而Queue集合这种数据结构能够存储具有先后时间关系的元素，很适用于在业务高峰期，需要缓存当前任务的业务场景。像Kafka、RabbitMQ、RocketMQ都是队列任务这种思想。

Queue集合底层接口提供的方法很简单，一共有 6 个。

```java
    // 添加元素。没有可用元素则简单返回false
    boolean offer(E e);
```

```java
    // 添加元素。没有可用元素则抛出IllegalStateException
    boolean add(E e);
```

```java
    // 移除队列的头部元素。如果此队列为空则返回null 。
    E poll();
```

```java
    // 移除队列的头部元素。该方法和poll不同之处在于，如果此队列为空，则抛出异常。
    E remove();
```

```java
    // 检索但不移除此队列的头部。如果此队列为空，则返回null 。
    E peek();
```

```java
    // 检索但不移除此队列的头部。该方法和peek唯一不同之处在于，如果此队列为空，则抛出异常
    E element();
```

Queue集合常用的实现类如下，我会一一讲来。包括双端队列的两个实现类：LinkedList、ArrayDeque，优先级队列：PriorityQueue，线程安全相关的Queue实现类：LinkedBlockingQueue、ArrayBlockingQueue、ConcurrentLinkedQueue。

![](https://i-blog.csdnimg.cn/direct/dacab50f96c34c67b94b394164882591.png)


### 1.2 双端队列

> ***面试官：双端队列的两个实现类知道吧？***

双端队列是Queue集合的一个子接口，顾名思义相比普通队列来说，双端队列可以往前、也可以往后顺序插入元素。比如我下面给出一段队列的初始化。

```java
        Queue<Integer> queue = new LinkedList<>();
        Deque<Integer> deque = new LinkedList<>();

        queue.add(1);
        deque.addLast(1);
        deque.addFirst(1);
```

同样是`new LinkedList<>()`来实例化队列，如果使用双端队列Deque接口，那这个队列就可以使用`addFirst`、`addLast`等双端队列特有的方法。

有南友就会问：那ArrayQueue呢？这两者都是双端队列Deque的底层实现，但底层数据结构不同，LinkedList底层数据结构是一个双向链表，看看它有前指针`next`、后指针`prev`。

```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

而ArrayDeque底层数据结构是一个Object类型的数组。

```java
    transient Object[] elements;
```

为什么要这么设计呢？其实这两种不同的设计就可以高效适用于不同的业务场景。双向链表实现的Deque随机查找的性能差，但插入、删除元素时性能非常出色，适用于修改操作更频繁的业务场景。

而数组实现的Deque，也就是ArrayDeque，它的插入、删除操作性能不如前者，但随机查找的性能非常出色，更适用于查询操作更频繁的业务场景。

### 1.3 优先级队列

> ***面试官：优先级队列有什么作用？***

优先级队列的实现类叫PriorityQueue，PriorityQueue虽然属于队列的一份子，不过它违背了队列最基本的原则：FIFO先进先出原则。它背叛了组织！

PriorityQueue的特性是它并不按常规队列一样顺序存储，而是根据元素的自然顺序进行排序，使用出队列的方法也是输出当前优先级最高的元素。例如以下代码输出的一样。

```java
    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();
        queue.offer(6);
        queue.offer(1);
        queue.offer(3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
```

```shell
# 执行结果
1
3
6
```

但如果我们直接打印PriorityQueue的所有元素，发现他其实并不是按元素的自然顺序进行存储。

```java
    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();
        queue.offer(6);
        queue.offer(1);
        queue.offer(3);

        System.out.println(queue);
    }
```

```shell
# 执行结果
[1, 6, 3]
```

why？其实PriorityQueue底层数据结构是一个**平衡二叉堆**：`transient Object[] queue`，如果你直接打印，打印的是堆里面的存储元素。

对于PriorityQueue来说，它只保证你使用`poll()`操作时，返回的是队列中最小、或最大的元素。

### 1.4 阻塞队列

> ***面试官：阻塞队列呢？***

JDK提供的阻塞队列接口为BlockingQueue，南哥先说说BlockingQueue的子类实现之一：ArrayBlockingQueue。

阻塞队列的特别之处在于当生产者线程会往队列放入元素时，如果队列已满，则生产者线程会进入阻塞状态；而当消费者线程往队列取出元素时，如果队列空了，则消费者线程会进入阻塞状态。

但队列的状态从满变为不满，消费者线程会唤醒生产者线程继续生产；队列的状态从空变为不空，生产者线程会唤醒消费者线程继续消费。

所以ArrayBlockingQueue很适合用于实现生产者、消费者场景。大家看看这个Demo。

```java
public class Test {
    public static void main(String[] args) {
        // 创建一个容量为 3 的ArrayBlockingQueue
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        // 创建并启动生产者线程
        Thread producerThread = new Thread(new Producer(queue));
        Thread consumerThread = new Thread(new Consumer(queue));
        producerThread.start();
        consumerThread.start();
    }
}

// 生产者类
class Producer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 6; i++) {
                System.out.println("生产者生产了: " + i);
                queue.put(i);
                Thread.sleep(150); // 模拟生产过程中的延迟
            }
            queue.put(-1); // 使用特殊值表示结束生产
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take();
                if (item == -1) {
                    break; // 遇到特殊值，退出循环
                }
                System.out.println("消费者消费了: " + item);
                Thread.sleep(100); // 模拟消费过程中的延迟
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

```shell
# 执行结果
生产者生产了: 1
消费者消费了: 1
生产者生产了: 2
消费者消费了: 2
生产者生产了: 3
消费者消费了: 3
生产者生产了: 4
消费者消费了: 4
生产者生产了: 5
消费者消费了: 5
生产者生产了: 6
消费者消费了: 6
```

LinkedBlockingQueue也是阻塞队列的实现之一，不过它和上面的ArrayBlockingQueue区别在于底层数据结构是由`双向链表`进行实现。

```java
    // LinkedBlockingQueue源码双向链表
    transient Node<E> head;
    private transient Node<E> last;
```
# 四大集合之Set

## 1. Set集合

### 1.1 HashSet

> ***面试官：你说说对HashSet的理解？***

Set集合区别于其他三大集合的重要特性就是元素具有唯一性，南友们记不住这个特性的话，有个易记的方法。Set集合为什么要叫Set呢？因为Set集合的命名取自于我们小学数学里的集合论（Set Theory），数学集合一个很重要的概念就是每个元素的值都互不相同。

Set集合常见的有实例有：HashSet、LinkedHashSet、TreeSet，南哥先缕一缕HashSet。

```java
// HashSet类源码
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {...}
```

HashSet底层实现其实是基于HashMap，HashMap的特点就是`Key`具有唯一性，这一点被HashSet利用了起来，每一个HashMap的`Key`对应的就是HashSet的`元素值`。来看看官方源码的解释。

> 此类实现Set接口，由哈希表（实际上是HashMap实例）支持。它不保证集合的迭代顺序；特别是，它不保证顺序随时间保持不变。此类允许null元素。

我们创建一个HashSet对象，实际上底层创建了一个HashMap对象。

```java
    // HashSet构造方法源码
    public HashSet() {
        map = new HashMap<>();
    }
```

HashSet一共提供了以下常用方法，不得不说HahSet在业务开发中还是用的没那么多的，南哥在框架源码上看HashSet用的就比较多，比如由Java语言实现的zookeeper框架源码。

（1）添加元素

```java
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```

我们看上面add方法的源码，是不是调用了HashMap的put方法呢？而put方法添加的Key是HashSet的值，Val则是一个空的Object对象。`PRESENT`是这么定义的。

```java
    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
```

（2）判断元素是否存在

```java
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
```

HashSet的contains方法同样是调用HashMap判断Key是否存在的方法：`containsKey`。

（3）移除元素

```java
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }
```

### 1.2 LinkedHashSet

> ***面试官：LinkedHashSet呢？***

接着轮到LinkedHashSet，同为Set集合之一，它和上文的HashSet有什么区别？南哥卖个关子。

源码对`LinkedHashSet`的解释。

> Hash table and linked list implementation of the Set interface, with predictable iteration order. This implementation differs from HashSet in that it maintains a doubly-linked list running through all of its entries. This linked list defines the iteration ordering, which is the order in which elements were inserted into the set (insertion-order). 

源码的大概意思就是：Set接口的哈希表和链表实现，具有可预测的迭代顺序。此实现与HashSet的不同之处在于，它维护一个贯穿其所有条目的**双向链表**。此链表定义迭代顺序，即**元素插入集合的顺序 **(插入顺序)。

底层数据结构是一条**双向链表**，每个元素通过指针进行相连，也就有了按插入顺序排序的功能。

知道了LinkedHashSet的特性，看看他的构造方法。

```java
    /**
     * 构造一个新的、空的链接哈希集，具有默认初始容量（16）和负载因子（0.75）。
     */
    public LinkedHashSet() {
        super(16, .75f, true);
    }
```

这个`super`方法向上调用了底层C语言源码实现的LinedHashMap的构造方法。LinkedHashMap的特点就是元素的排序是根据插入的顺序进行排序，那LinkedHashSet也就继承了这个特性。

```java
    // C语言源码
    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }
```

LinkedHashSet的常见方法和HashSet一样，同样是add()、contains()、remove()，这里我写个简单的Demo。

```java
    public static void main(String[] args) throws IOException {
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        set.add(1);
        System.out.println(set.contains(1));
        set.remove(1);
        System.out.println(set.contains(1));
    }
```

```shell
# 运行结果
true
false
```



### 1.3 TreeSet

> ***面试官：TreeSet和它们比有什么特性？***

轮到你了，TreeSet。我们南友们很好奇为什么他叫TreeSet？

因为他是基于TreeMap实现的。。。

但根本原因不是，TreeMap的底层是通过**红-黑树**数据结构来实现自然排序，那TreeSet也就继承了这个特性。

官方源码对TreeSet的解释：

> 基于TreeMap的NavigableSet实现。元素使用其**自然顺序**进行排序，或者根据使用的构造函数，使用创建集合时提供的Comparator进行排序。

源码解释告诉我们，TreeSet和HashSet、LinkedHashSet不同的特性在于，元素既不像HashSet一样无序，也不是像LinkedHashSet一样是以插入顺序来排序，它是根据元素的自然顺序来进行排序。

`b、c、a`这三个元素插入到TreeSet中，自然顺序就和字母表顺序一样是：`a、b、c`。

```java
    public static void main(String[] args) throws IOException {
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("b");
        treeSet.add("c");
        treeSet.add("a");
        System.out.println(treeSet);
    }
```

```shell
# 运行结果
[a, b, c]
```

TreeSet除了拥有以下的add()、contains()、remove()方法。

```java
    // 如果指定元素尚不存在，则将其添加到此集合中。
    public boolean add(E e) {
        return m.put(e, PRESENT)==null;
    }
```

```java
    // 如果此集合包含指定元素，则返回true 
    public boolean contains(Object o) {
        return m.containsKey(o);
    }
```

```java
    // 如果存在指定元素，则从此集合中移除该元素。
    public boolean remove(Object o) {
        return m.remove(o)==PRESENT;
    }
```

值得提出来的是，TreeSet还拥有first()、last()，可以方便我们提取出第一个、最后一个元素。

```java
    // 返回集合中的第一个元素。
    public E first() {
        return m.firstKey();
    }
```

```java
    // 返回集合中的最后一个元素。
    public E last() {
        return m.lastKey();
    }
```

### 1.4 TreeSet自定义排序

> ***面试官：那TreeSet要怎么定制排序？***

TreeSet的自定义排序我们要利用`Comparator`接口，通过向TreeSet传入自定义排序规则的`Comparator`来实现。

官方源码是这么解释的，南友们看一看。

```java
    // 构造一个新的空树集，根据指定的比较器进行排序。
    // 插入到集合中的所有元素都必须能够通过指定的比较器相互比较： comparator. compare(e1, e2)不得对集合中的任何元素e1和e2抛出ClassCastException 。
    // 如果用户尝试向集合中添加违反此约束的元素，则add调用将抛出ClassCastException 
    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }
```

传入`Comparator`接口时，我们还需要定义`compare`方法的游戏规则：如果`compare`方法比较两个元素的大小，返回正整数代表第一个元素 > 第二个元素、返回负整数代表第一个元素 < 第二个元素、返回0代表第一个元素 = 第二个元素。

下面我写了一个Demo，Comparator接口的规则是这样：人的岁数越小，那么他排名越靠前。

```java
public class JavaProGuideTest {
    public static void main(String[] args) {
        TreeSet set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                Person p1 = (Person)o1;
                Person p2 = (Person)o2;
                return (p1.age > p2.age) ? 1 : (p1.age < p2.age) ? -1 : 0;
            }
        });

        set.add(new Person(5));
        set.add(new Person(3));
        set.add(new Person(6));
        System.out.println(set);
    }

    @Data
    @AllArgsConstructor
    private static class Person {
        int age;
    }
}
```

```shell
# 执行结果
[JavaProGuideTest.Person(age=3), JavaProGuideTest.Person(age=5), JavaProGuideTest.Person(age=6)]
```
# 并发编程面试必备：synchronized原理、锁升级

## 1. 可重入锁

> ***面试官：知道可重入锁有哪些吗?***

可重入意味着获取锁的粒度是**线程**而不是**调用**，如果大家知道这个概念，会更容易理解可重入锁的作用。

既然获取锁的粒度是线程，意味着线程自己是可以获取自己的内部锁的，而如果获取锁的粒度是调用则每次经过同步代码块都需要重新获取锁。

举个例子。线程A获取了某个对象锁，但在线程代码的流程中仍需再次获取该对象锁，此时线程A可以继续执行不需要重新再获取该对象锁。另外线程如果要使用**父类**的同步方法，由于可重入锁也无需再次获取锁。

在Java中，可重入锁主要有ReentrantLock、**synchronized**。

## 2. synchronized实现原理

> ***面试官：你先说说synchronized的实现原理?***

synchronized的实现是基于monitor的。任何对象都有一个**monitor**与之关联，当monitor被持有后，对象就会处于**锁定状态**。而在同步代码块的**开始位置**，在编译期间会被插入**monitorenter指令**。

当线程执行到monitorenter指令时，就会尝试获取**monitor**的所有权，如果获取得到则代表获得锁资源。

### 2.1 synchronized缺点

> ***面试官：那synchronized有什么缺点？***

在Java SE 1.6还没有对synchronized进行了各种优化前，很多人都会称synchronized为**重量级锁**，因为它对资源消耗是比较大的。

1. synchronized需要频繁的**获得锁、释放锁**，这会带来了不少性能消耗。

2. 另外没有获得锁的线程会被**操作系统**进行挂起阻塞、唤醒。而**唤醒操作**需要保存当前线程状态，切换到下一个线程，也就是进行**上下文切换**。上下文切换是很耗费资源的一种操作。


### 2.2 保存线程状态

> ***面试官：为什么上下文切换要保存当前线程状态？***

这就跟读英文课文时查字典一样，我们要先记住课文里的**页数**，查完字典好根据页数翻到英文课文原来的位置。

同理，CPU要保证可以**切换**到上一个线程的状态，就需要保存当前线程的状态。

### 2.3 锁升级

> ***面试官：可以怎么解决synchronized资源消耗吗？***

上文我有提到Java SE 1.6对synchronized进行了各种优化，具体的实现是给synchronized引入了**锁升级**的概念。synchronized锁一共有四种状态，级别从低到高依次是无锁、偏向锁、轻量级锁、重量级锁。

大家思考下，其实多线程环境有着各种不同的场景，同一个锁状态并不能够适应所有的业务场景。而这四种锁状态就是为了**适应各种不同场景**来使得线程并发的效率最高。

1. 没有任何线程访问同步代码块，此时synchronized是**无锁**状态。

2. 只有**一个线程**访问同步代码块的场景的话，会进入**偏向锁**状态。偏向锁顾名思义会**偏向访问它的线程**，使其加锁、解锁不需要额外的消耗。

3. 有**少量线程竞争**的场景的话，偏向锁会升级为**轻量级锁**。而轻量级采用**CAS操作**来获得锁，CAS操作不需要获得锁、释放锁，减少了像synchronized重量级锁带来的**上下文切换**资源消耗。

4. 轻量级锁通过CAS自旋来获得锁，如果自旋10次失败，为了减少CPU的消耗则锁会膨胀为**重量级锁**。此时synchronized重量级锁就回归到了悲观锁的状态，其他获取不到锁的都会进入阻塞状态。

### 2.4 锁升级优缺点

> ***面试官：它们都有什么优缺点呢？***

由于每个锁状态都有其不同的优缺点，也意味着有其不同的适应场景。

1. **偏向锁**的优点是加锁和解锁操作不需要额外的消耗；缺点是如果线程之间存在锁竞争，偏向锁会撤销，这也带来额外的撤销消耗；所以偏向锁适用的是只有一个线程的业务场景。
2. **轻量级锁**状态下，优点是线程不会阻塞，提高了程序执行效率；但如果始终获取不到锁的线程会进行**自旋**，而自旋动作是需要消耗**CPU**的；所以轻量级锁适用的是追求响应时间、同时同步代码块执行速度快的业务场景。
3. **重量级锁**的优点是不需要自旋消耗CPU；但缺点很明显，线程会阻塞、响应时间也慢；重量级锁更适用在同步代码块执行速度较长的业务场景。
# 并发编程面试必备：ThreadLocal作用、线程生命周期

## 1. 线程通信

### 1.1 线程的等待/通知机制

> ***面试官：Java线程的等待/通知机制知道吧？***

Java线程的等待/通知机制指的是：线程A获得了synchronized同步方法、同步方法块的锁资源后，调用了锁对象的wait()方法，释放锁的同时进入**等待状态**；而线程B获得锁资源后，再通过锁对象的notify()或notifyAll()方法来**通知**线程A恢复执行逻辑。

其实Java的所有对象都拥有等待/通知机制的本领，大家可以在JDK源码package java.lang`下找到Java.lang.Object里提供的五个与等待/通知机制相关的方法。

一、等待。

（1）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法。

```java
    public final void wait() throws InterruptedException {
        wait(0);
```

（2）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法，或者指定的毫秒timeout过去。

```java
    public final native void wait(long timeout) throws InterruptedException;
```

（3）使当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法，或者指定的毫秒timeout过去，另外nanos是额外时间，以纳秒为单位。

```java
    public final void wait(long timeout, int nanos) throws InterruptedException {
    }
```

所以其实wait()、watit(0)、watit(0, 0)执行后都是**同样的效果**。

二、通知。

（1）唤醒在此对象监视器上等待的单个线程。

```java
    public final native void notify();
```

（2）唤醒在此对象监视器上等待的所有线程。

```java
    public final native void notifyAll();
```

大家有没听说过**消费者生产者问题**呢？消费者生产者之间要无限循环生产和消费物品，解决之道就是两者形成完美的等待、通知机制。而这套机制就可以通过上文的wait、notify方法来实现。

### 1.2 线程通信方式

> ***面试官：还有没有其他线程通信方式？***

（1）利用Condition进行线程通信。

如果大家的程序直接采用的是**Lock对象**来同步，则没有了上文synchronized锁带来的隐式同步器，也就无法使用wait()、notify()方法。

此时的线程可以使用Condition对象来进行通信。例如下文的示例代码： condition0的await()阻塞当前线程，同时释放、等待获取锁资源；接着等待其他线程调用condition0的signal()来通知其获取锁资源继续执行。

```java
@Slf4j
public class UseReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Condition condition0 = lock.newCondition();

    private static final Condition condition1 = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                lock.lock();

                for (int i = 1; i < 4; i++) {
                    log.info(i + "");

                    condition1.signal();
                    condition0.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.lock();

                for (int i = 65; i < 68; i++) {
                    log.info((char) i + "");

                    condition0.signal();
                    condition1.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
```

```sh
# 程序执行结果
2024-05-30 10:30:30[ INFO ]1
2024-05-30 10:30:30[ INFO ]A
2024-05-30 10:30:30[ INFO ]2
2024-05-30 10:30:30[ INFO ]B
2024-05-30 10:30:30[ INFO ]3
2024-05-30 10:30:30[ INFO ]C
```

（2）Thread采用join方法进行通信。

线程Thread对象还提供了join方法，也是一种通信的方式。当某个程序的执行流调用了某个thread对象的join方法，调用线程将会被阻塞，等到thread对象终止后才通知调用线程继续执行。

```java
    public final void join() throws InterruptedException {
        join(0);
    }
```

（3）volatile共享变量。

volatile的出现，大家是不是有些意外呢？虽然volatile适用的多线程场景不多，但它也是线程通信的一种方式。被volatile修饰的变量如果更新了值，则会通过**主内存**这条消息总线通知所有使用该变量的线程，让其把主内存同步到**工作内存**里，则所有线程都会获取共享变量最新值。

### 1.3 更加灵活的ReentrantLock

> ***面试官：你说的Lock对象说下你的理解？***

在线程同步上，JDK的Lock接口提供了多个实现子类，如下所示。下面我按面试官**面试频率高**的`ReentrantLock`来讲解。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ee4a152a01fb2e135010c76999881edd.png)


ReentrantLock相比synchronized来说使用锁更加灵活，可以自由进行加锁、释放锁。ReentrantLock类提供了lock()、unlock()来实现以上操作。具体实操代码可以看上一个面试官问题关于Condition的示例代码。

```java
// ReentrantLock源码
package java.util.concurrent.locks;
public class ReentrantLock implements Lock, java.io.Serializable {
    
    // 获取锁
    public void lock() {
        sync.lock();
    }
    
    // 尝试释放此锁
    public void unlock() {
        sync.release(1);
    }
}
```

另外ReentrantLock和synchronized都是**可重入锁**，即线程获取锁资源后，下一步如果进入**相同锁资源**的同步代码块，不需要再获取锁。

ReentrantLock也可以实现**公平锁**，即成功获取锁的顺序与申请锁资源的顺序一致。我们在创建对象时进行初始化设置就可以设置为公平锁。

```java
 ReentrantLock lock = new ReentrantLock(true);
```

## 2. ThreadLocal作用

> ***面试官：ThreadLocal知道吧？***

上文我们讨论的都是在多个线程**对共享资源进行通信**的业务场景上，例如商城业务秒杀的库存要保证数据安全性。而如果在多个线程**对共享资源进行线程隔离**的业务场景上，则可以使用ThreadLoccal来解决。

ThreadLocal可以保存当前线程的副本值，提供了set、get方法，通过set方法可以把指定值设置到当前线程副本；而通过get方法可以返回此当前线程副本中的值。

例如要实现一个功能，每个线程打印`当前局部变量:局部变量 + 10`，我们就可以利用ThreadLocal保存共享变量i，来避免对变量i的共享冲突。

```java
public class UseThreadLocal {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 3; i++) {
            int number = i;
            es.execute(() -> System.out.println(number + ":" + new intUtil().addTen(number)));
        }
    }

    private static class intUtil {
        public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>(); // 使用threadLocal保存线程保存的当前共享变量num

        public static int addTen(int number) {
            threadLocal.set(number);

            try { // 休息1秒
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return threadLocal.get() + 10;
        }
    }
}
```

```sh
# 程序执行结果
0:10
2:12
1:11
```

## 3. 线程生命周期

> ***面试官：那线程生命周期都有什么？***

在校招笔试、或面试中，这道面试题还是比较常见的，大家简单记忆下就可以。

1. 初始状态。创建了线程对象还没有调用start()。

2. 就绪或运行状态。执行了start()可能运行，也可能进入就绪状态在等待CPU资源。

3. 阻塞状态 。一直没有获得锁。

4. 等待状态。等待其他线程的通知唤醒。

5. 超时状态。

6. 终止状态。
# 并发编程面试必备：如何创建线程池、线程池拒绝策略

## 1. 线程池使用

### 1.1 如何配置线程池大小

> ***面试官：你说下线程池的大小要怎么配置？***

这个问题要看业务系统执行的任务更多的是计算密集型任务，还是I/O密集型任务。大家可以从这**两个方面**来回答面试官。

（1）如果是**计算密集型任务**，通常情况下，CPU个数为N，设置N + 1个线程数量能够实现最优的资源利用率。因为N + 1个线程能保证**至少有N个**线程在利用CPU，提高了CPU利用率；同时不设置过多的线程也能减少线程状态切换所带来的上下文切换消耗。

（2）如果是**I/O密集型任务**，线程的主要等待时间是花在等待I/O操作上，另外就是计算所花费的时间。一般可以根据这个公式得出线程池合适的大小配置。
$$
线程池大小 = CPU数量 * CPU期望的利用率 * (1 + IO操作等待时间/CPU计算时间)
$$

### 1.2 创建线程池

> ***面试官：那线程池怎么创建？***

可以使用ThreadPoolExecutor自定义创建线程池，这也是创建线程池**推荐**的创建方式。

```java
public ThreadPoolExecutor(int corePoolSize, // 要保留在池中的线程数
                          int maximumPoolSize, // 池中允许的最大线程数
                          long keepAliveTime, // 当线程数大于corePoolSize时，多余的空闲线程在终止之前等待新任务的最长时间
                          TimeUnit unit, // 时间单位
                          BlockingQueue<Runnable> workQueue, // 在执行任务之前用于保存任务的队列
                          ThreadFactory threadFactory) { // 执行程序创建新线程时使用的工厂
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         threadFactory, defaultHandler);
}
```

另外Executors类也提供了一些**静态工厂方法**，可以用来创建一些预配置的线程池。

newFixedThreadPool可以设置线程池的固定线程数量。

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());
}
```

newSingleThreadExecutor可以让线程**按序执行**，适用于需要确保所有任务按序执行的场景。

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```

大家看下以下源码，newCachedThreadPool的线程数**没有上限限制**，同时空闲线程的存活时间是**60秒**。newCachedThreadPool更适合系统负载不太高、线程执行时间短的场景下，因为线程任务不需要经过排队，直接交给空闲线程就可以。

```java
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}
```

newScheduledThreadPool可以安排任务在给定的**延迟**后运行，或者定期执行。

```java
public static ScheduledExecutorService newScheduledThreadPool(
        int corePoolSize, ThreadFactory threadFactory) {
    return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
}
```



### 1.3 预配置线程池弊端

> ***面试官：你说的这些预配置线程池会有什么问题？***

小伙伴要记得上述静态工厂方法在使用过程中可能会出现**OOM内存溢出**的情况。

1. `newFixedThreadPool`、`newSingleThreadExecutor`：因为线程池指定的请求队列类型是链表队列`LinkedBlockingQueue<Runnable>()`，故允许的请求队列长度是无上限的，可能会出现OOM内存溢出。
2. `newCachedThreadPool`、`newScheduledThreadPool`：线程池指定的线程数上限是Integer.MAX_VALUE，故允许创建的线程数量是无上限的Integer.MAX_VALUE，可能会出现OOM内存溢出。

### 1.3 Spring创建线程池

> ***面试官：你们项目线程池用的这种创建方式？***

一般Spring工程创建线程池不直接使用ThreadPoolExecutor。

Spring框架提供了以**Bean形式**来配置线程池的`ThreadPoolTaskExecutor`类，ThreadPoolExecutor类的底层实现还是基于JDK的ThreadPoolExecutor。

```java
# 示例代码
@Bean(name = "testExecutor")
public ThreadPoolTaskExecutor testExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 配置核心线程数
    executor.setCorePoolSize();
    // 配置最大线程数
    executor.setMaxPoolSize();
    // 配置队列大小
    executor.setQueueCapacity();
    executor.initialize();
    return executor;
}
```

## 2. 线程池拒绝策略

> ***面试官：线程池请求队列满了，有新的请求进来怎么办？***

大家如果有看ThreadPoolExecutor源码就知道，ThreadPoolExecutor类实现了`setRejectedExecutionHandler`方法，顾名思义意思是设置拒绝执行处理程序。

```java
# ThreadPoolExecutor源码
/**
* Sets a new handler for unexecutable tasks. // 为无法执行的任务设置新的处理程序
*
* @param handler the new handler
* @throws NullPointerException if handler is null
* @see #getRejectedExecutionHandler
*/
public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
    if (handler == null)
        throw new NullPointerException();
    this.handler = handler;
}
```

该方法可以为线程池设置**拒绝策略**，目前JDK8一共有四种拒绝策略，也对应入参RejectedExecutionHandler的四种子类实现。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/29561a96e3f9705be7eff027876e00fc.png)


1. AbortPolicy：**默认**的拒绝策略，直接抛出RejectedExecutionException异常。
2. DiscardPolicy：直接丢弃被拒绝的任务
3. CallerRunsPolicy：直接在execute方法的**调用线程**中运行被拒绝的任务。。
4. DiscardOldestPolicy：丢弃最旧的未处理请求，然后重试execute 。

另外如果线程池拒绝策略设置为DiscardOldestPolicy，线程池的请求队列类型最好不要设置为优先级队列**PriorityBlockingQueue**。因为该拒绝策略是丢弃**最旧的请求**，也就意味着丢弃**优先级最高的请求**。

## 3. 线程工厂的作用

> ***面试官：线程池的入参ThreadFactory有什么作用吗？***

ThreadFactory定义了创建线程的工厂，回答这个问题我们就要结合实际场景了。

ThreadFactory线程工厂能够为线程池里每个线程**设置名称**、同时设置**自定义异常的处理逻辑**，可以方便我们通过日志来定位bug的位置。

以下是一个代码示例。

```java
@Slf4j
public class CustomGlobalException {
    public static void main(String[] args) {
        ThreadFactory factory = r -> {
            String threadName = "线程名称";
            Thread thread = new Thread(r, threadName);
            thread.setUncaughtExceptionHandler((t, e) -> {
                log.error("{}执行了自定义异常日志", threadName);
            });
            return thread;
        };
        ExecutorService executor = new ThreadPoolExecutor(6,
                6,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(66),
                factory);

        executor.execute(() -> {
            throw new NullPointerException();
        });
        executor.shutdown();
    }
}

控制台打印：2024-04-26 22:04:45[ ERROR ]线程名称执行了自定义异常日志
```
# 并发编程面试必备：指令重排序、volatile可见性原理及局限性

## 1. 指令重排序

### 1.1 重排序是什么

> ***面试官：重排序知道吧？***

指令重排序字面上听起来很高级，但只要理解了并不难掌握。我们先来看看指令重排序究竟有什么作用。

指令重排序的主要作用是可以优化编译器和处理器的执行效率，提高程序性能。例如多条执行顺序不同的指令，可以重排序让**轻耗时的指令**先执行，从而让出CPU流水线资源供其他指令使用。

但如果指令之间存在着**数据依赖**关系，则编译器和处理器不会对相关操作进行指令重排序，避免程序执行结果改变。这个规则也称为`as-if-serial语义`。例如以下代码。

```java
String book = "JavaGetOffer"; // A
String avator = "思考的陈"; // B
String msg = book + abator; // C
```

对于A、B，它们之间并没有依赖关系，谁先执行对程序的结果没有任何影响。但C却依赖于A、B，不能出现类似C -> A -> B或C -> B -> A或A -> C -> B或B -> C -> A之类的指令重排，否则程序执行结果将改变。

### 1.2 重排序的问题

> ***面试官：那重排序不会有什么问题吗？***

在单线程环境下，有`as-if-serial语义`的保护，我们无需担心程序执行结果被改变。但在多线程环境下，指令重排序会出现**数据不一致**的问题。举个多线程的例子方便大家理解。

```java
       int number = 0;
       boolean flag = false;
       public void method1() {
           number = 6;                // A
           flag = true;               // B
       }
       public void method2() {
           if (flag) {               // C
               int i = number * 6;   // D
           }
       }
```

假如现在有两个线程，线程1执行`method1`、线程2执行`method2`。因为`method1`其中的A、B之间没有数据依赖关系，可能出现B -> A的指令重排序，大家注意这个指令重排序会影响到线程2执行的结果。

当B指令执行后A指令还没有执行`number = 6`，此时如果线程2执行`method2`同时给i赋值为`0 * 6`。很明显程序运行结果和我们预期的并不一致。

## 2. volatile

### 2.1 volatile特性

> ***面试官：有什么办法可以解决？***

关于上文的重排序问题，可以使用volatile关键字来解决。volatile一共有以下特性：

1. **可见性**。volatile修饰的变量每次被修改后的值，对于任何线程都是可见的，即任何线程会读取到最后写入的变量值。
2. **原子性**。volatile变量的读写具有原子性。
3. **禁止代码重排序**。对于volatile变量操作的相关代码不允许重排序。

```java
       int number = 0;
       volatile boolean flag = false;
       public void method1() {
           number = 6;                // A
           flag = true;               // B
       }
       public void method2() {
           if (flag) {               // C
               int i = number * 6;   // D
           }
       }
```

由于volatile具有禁止代码重排序的特性，所以不会出现上文的**B -> A的指令重排序**。另外volatile具有可见性，falg的修改对线程2来说是可见的，线程会立刻感知到`flag = ture`从而执行对i的赋值。以上问题可以通过volatile解决，和使用synchronized加锁是一样的效果。

另外大家注意一点，volatile的原子性指的是对volatile的读、写操作的原子性，但类似于`volatile++`这种**复合操作**是没有原子性的。

### 2.2 可见性原理

> ***面试官：那volatile可见性的原理是什么？***

内存一共分为两种，线程的本地内存和线程外的主内存。对于一个volatile修饰的变量，任何线程对该变量的修改都会同步到**主内存**。而当读一个volatile修饰的变量时，JMM（Java Memory Model）会把该线程对应的**本地内存**置为无效，从而线程读取变量时读取的是主内存。

线程每次读操作都是读取主内存中最新的数据，所以volatile能够实现**可见性**的特性。

### 2.3 volatile局限性

> ***面试官：volatile有什么缺点吗？***

企业生产上还是比较少用到volatile的，对于加锁操作会使用的更多些。

1. synchronized加锁操作虽然开销比volatile大，但却适合复杂的业务场景。而volatile只适用于**状态独立**的场景，例如上文对flag变量的读写。
2. volatile编写的代码是比较难以理解的，不清楚整个流程和原理很难维护代码。
3. 类似于`volatile++`这种**复合操作**，volatile不能确保原子性。



# 可能是最漂亮的Java IO流详解

## 1. Java I/O的理解

> ***面试官：你说下对Java IO的理解？***

Java I/O有两个参与对象，一个是**I/O源端**，一个是想要和`I/O`源端通信的各种**接收端**，比如程序控制IDEA控制台输出、读取文件A写入文件B等，我们程序要保证的就是IO流的顺利**读取**和顺利写入。JDK把对Java IO的支持都放在了`package java.io`包下，南哥数了数，一个有86个类和接口。

我们看下`package java.io`包最常用的Reader和Writer接口，他们的作者都是Mark Reinhold。这位老哥是谁？[他](https://mreinhold.org/)是Oracle Java平台组的首席架构师，也是字符流读取器和写入器的首席工程师。这么有来头，看来Java I/O的程序设计不简单，我们可以从中学到不少好用的东西。

```java
/** 
 * @author      Mark Reinhold
 * @since       JDK1.1
 */
public abstract class Reader implements Readable, Closeable { }
public abstract class Writer implements Appendable, Closeable, Flushable { }
```

## 2. 输入流

### 2.1 字节输入流抽象基类

> ***面试官：那要怎么读取字节流？***

我们先讲输入流，后面再讲下输出流。输入流又分为字节流和字符流，顾名思义，字节流按字节来读取，操作的数据单元是8位的字节；而字符流按字符来读取，操作的数据单元是16位的字符。

读取字节的抽象基类是**InputStream**，这个基类提供了3个方法给我们来读取字节流。

1. 从输入流读取下一个数据字节，值字节以0到255范围内的`int`返回。

   ```java
   public abstract int read() throws IOException
   ```

2. 从输入流读取一定数量的字节并将它们存储到缓冲区数组`b`中。

   ```java
   public int read(byte b[]) throws IOException
   ```

3. 从输入流读取最多`len`个字节的数据到字节数组中。

   ```java
   public int read(byte b[], int off, int len) throws IOException
   ```

大家注意以上方法的返回参数都是int类型，当正常读取时，int返回的是读取的字节个数；而当int**返回-1**，就表明输入流到达了末尾。

### 2.2 字节输入流读取

> ***面试官：你说的这些不是实例，我要的是能真正读取的？***

上文的是抽象的接口，本身并不具备实际的功能。真正能够读取文件的是`InputStream`抽象基类的子类实现，例如**文件流FileInputStream**，有了他，我们读取音频、视频、gif等等都不是问题。

```java
// 文件流读取文件
FileInputStream stream = new FileInputStream(SOURCE_PATH);
```

我们还可以在外面加一层**缓存字节流**来提高读取效率，在外层套上BufferedInputStream对象，为什么可以提高读取效率我下文会讲到。

```java
BufferedInputStream stream = new BufferedInputStream(new FileInputStream(SOURCE_PATH));
```

以上通过字节流我们是以n个字节来读取的，如果要用`readLine()`读取某一行这种场景下就不适用了。我们可以把缓存字节流换成**缓存字符流**来承接，使用`InputStreamReader`**转换流**把字节输入流转换成字符输入流。

如下代码所示。

```java
BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_PATH)));
```

### 2.3 提高读取效率

> ***面试官：为什么加一层缓存流就能提高读取效率？***

为什么加一层缓存流就能提高读取效率？因为直接使用 `FileInputStream` 读取文件的话，每次调用 `read()` 都是从**磁盘读取一个字节**，而每次读取都是一次系统调用。**系统调用是操作系统层面的调用**，涉及到用户空间和内核空间之间的上下文切换，这些切换的成本是很昂贵的。

而如果使用缓存流，一次性从文件里读取多个字节到缓存中，减少系统调用同时也减少了磁盘读取，读取的效率明显提高了。

除了Java I/O采用缓存流来提高读取效率，大多应用程序也采用缓存来提升程序性能，例如我们后端在业务开发会使用Redis缓存来减少数据库压力。关于`为什么使用缓存来提高应用程序效率`，大家也可以看看国外[Quora](https://www.quora.com/Why-is-caching-used-to-increase-read-performance/answer/Gaive-Gandhi)的回答，解释得很详细。

### 2.4 字符输入流

> ***面试官：那字符流读取呢？***

字符输入流的抽象基类是`Reader`，同样是提供了3个方法来支持字符流读取。

1. 读取单个字符。

   ```java
   public int read() throws IOException
   ```

2. 将字符读入数组。

   ```java
   public int read(char cbuf[]) throws IOException
   ```

3. 将字符读入数组的一部分。

   ```java
   abstract public int read(char cbuf[], int off, int len) throws IOException
   ```

字符流读取的实例是`FileReader`，同样可以使用缓存字符流提高读取效率。

```java
BufferedReader reader = new BufferedReader(new FileReader(new File(SOURCE_PATH)));
```

我们来具体实操下，读取`C:\\Users\\Desktop\\JavaProGuide\\read`下的所有文件，把他们合并在一起，写入到`C:\\Users\\Desktop\\JavaProGuide\\write`下的`PRODUCT.txt`文件中。

```java
public class Client {

    private static final String PATH = "C:\\Users\\Desktop\\JavaProGuide\\read";

    private static final String FILE_OUT = "C:\\Users\\Desktop\\JavaProGuide\\write\\PRODUCT.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(PATH);
        File[] files = file.listFiles();

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_OUT));

        for (File curFile : files) {
            BufferedReader reader = new BufferedReader(new FileReader(curFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            reader.close();
        }

        writer.close();
    }

}
```

## 3. 输出流

### 3.1 输出流

> ***面试官：输出流你也讲一讲？***

字节输出流的抽象基类是`OutputStream`，字符输出流的抽象基类是`Writer`。他们分别提供了以下方法。

字节输出流`OutputStream`：

1. 将指定字节写入此输出流。

   ```java
   public abstract void write(int b) throws IOException
   ```

2. 将指定字节数组中的`b.length`字节写入此输出流。

   ```java
   public void write(byte b[]) throws IOException 
   ```

3. 将指定字节数组中从偏移量`off`开始的`len`个字节写入此输出流。

   ```java
   public void write(byte b[], int off, int len) throws IOException
   ```

字符输出流`Writer`：

1. 写入单个字符。

   ```java
   public void write(int c) throws IOException
   ```

2. 写入字符数组。

   ```java
   public void write(char cbuf[]) throws IOException
   ```

3. 写入字符数组的一部分。

   ```java
   abstract public void write(char cbuf[], int off, int len) throws IOException
   ```

另外字符输出流是使用**字符**来操作数据，所以可以用**字符串来代替字符数组**，JDK还支持以下**入参**是字符串的方法。

1. 写入一个字符串。

   ```java
   public void write(String str) throws IOException
   ```

2. 写入字符串的一部分。

   ```java
   public void write(String str, int off, int len) throws IOException
   ```

## 4. 字节流和字符流区别

> ***面试官：那字节流和字符流有什么区别？***

字节流和字符流的区别主要是三个方面。

- **基本单位不同**。字节流以字节（8位二进制数）为基本单位来处理数据，字符流以字符为单位处理数据。
- **使用场景不同**。字节流操作可以所有类型的数据，包括文本数据，和**非文本数据**如图片、音频等；而字符流只适用于处理文本数据。
- **关于性能方面**。因为字节流不处理字符编码，所以处理大量文本数据时可能不如字符流高效；而字符流使用到**内存缓冲区**处理文本数据可以优化读写操作。





# 掌握NIO是什么？零拷贝的概念

## 1. Java NIO

### 1.1 NIO介绍

NIO的出现在于提高`IO`的速度，它相比传统的输入/输出流速度更快。NIO通过管道`Channel`和缓冲器`Buffer`来处理数据，可以把管道当成一个矿藏，缓冲器就是矿藏里的卡车。

程序通过管道里的缓冲器进行数据交互，而**不直接处理数据**。程序要么从缓冲器获取数据，要么输入数据到缓冲器。

### 1.2 通道和缓冲器

NIO提供了通道和缓冲器这两个核心对象。

（1）管道`Channel`：

与传统的`IO`流只能只读或只写的**单向流**不同，NIO通道是**双向的**，也就是说读写操作可以同时进行，使得数据的处理效率也更高。

（2）缓冲器`Buffer`：

传统的输入/输出流一次只处理一个字节，而每一次字节读取都是一次系统调用，涉及到用户空间和内核空间之间的上下文切换，通常来说效率不高。

而`NIO`采用内存映射文件方式来处理输入/输出，Channel通过`map()`方法把**一块数据**映射到内存中。程序通过`Buffer`进行数据交互，减少了与原始数据源的直接访问。NIO**面向块**的处理方式使得效率更高。

### 1.3 非阻塞IO模型

传统的输入/输出流是同步阻塞`IO`模型，如果数据源没有数据了，此时程序将进行阻塞。

而`NIO`是I/O多路复用模型，线程可以**询问**通道有没可用的数据，而不需要在没有数据时阻塞掉线程。

### 1.4 字符流处理字符吗

所有数据包括文本数据最终都是以**字节形式**存储的，因为计算机底层只能理解二进制数据。

字符最终也是要转换成字节形式，之所以可以在文本文件看到字符，是因为系统将底层的二进制序列转换成了字符。

## 2. Channel和Buffer使用

### 2.1 Buffer

Buffer里有3个**关键变量**。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/d37341999383489896eeb066b43b3531.jpeg#pic_center)

1. capcity：表示缓冲器`Buffer`的最大数据容量。
2. position：用来指出下一个可以读出/写入`Buffer`的索引位置，也就是记录指针的作用。
3. limit：用来表示在`Buffer`里第一个不能被读出/写入的索引位置。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/206dbd62fd7d4b4592da7eba1477c1e4.png#pic_center)


另外`Buffer`还提供了`get`、`put`方法来供我们操作数据，而使用`get/put`后，position的指针位置也会随之移动。

```java
public abstract byte get();

public abstract ByteBuffer put(byte b);
```

### 2.2 Channel

Channel有常见的3个方法，map()、read()和write()。

```java
// 将通道文件的区域直接映射到字节缓冲区中
public abstract MappedByteBuffer map(MapMode mode, long position, long size)

// 从此Channel通道读取字节序列到给定缓冲区dst
public abstract int read(ByteBuffer dst)
    
// 将给定缓冲区中src的字节序列写入此Channel通道
public abstract int write(ByteBuffer src)
```

以下是`Channel`的简单使用代码。

```java
public class TestFileChannel {
    public static void main(String[] args) {
        File f = new File("D:\\JavaGetOffer\\TestFileChannel.java");
        try {
            FileChannel inChannel = new FileInputStream(f).getChannel();
            FileChannel outChannel = new FileOutputStream("a.txt").getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, f.length());

            outChannel.write(buffer);
            buffer.clear();
            CharBuffer charBuffer = StandardCharsets.UTF_8.newDecoder().decode(buffer);
            System.out.println(charBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

## 3. NIO零拷贝

在`NIO`零拷贝出现之前，一个I/O操作会将同一份数据进行**多次拷贝**。可以看下图，一次I/O操作对数据进行了四次复制，同时来伴随两次内核态和用户态的上下文切换，众所周知上下文切换是很耗费性能的操作。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/b8cfa31e08854d5683514d4e4c9fbd20.png#pic_center)


而零拷贝技术改善了上述的问题。可以对比下图，零拷贝技术**减少**了对一份数据的拷贝次数，不再需要将数据在**内核态和用户态**之间进行拷贝，也意味不再进行上下文切换，让数据传输变得更加高效。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/845e712aeed14342b9c6b901023e1ff7.png#pic_center)
# 接口和多态

## 1. 抽象类

### 1.1 子类调用父类

现在有IDEA集成开发环境，可以给大家实时提醒哪个地方编译错误，但假如要大家用`.txt`文件编写程序呢。南哥问：现在这段代码错在了哪？

```java
class Base {
    public Base(String s) {
        System.out.print("B");
    }
}

public class Derived extends Base {
    public Derived (String s) {
        System.out.print("D");
    }
    public static void main(String[] args) {
        new Derived("C");
    }
}
```

假如父类和子类同时拥有**有参构造方法**，子类的构造方法必须显性地调用父类的构造方法，否则会**编译错误**。所以正常的写法应该是这样。

```java
    public Derived (String s) {
        super(s);  
        System.out.print("D");
    }
```

另外大家还需要注意一点，调用父类的构造方法必须在子类构造方法的第一行，调用父类的构造方法也只能出现在子类的构造方法上，否则也会是编译报错。

### 1.2 子类访问父类

如下代码，一共有两处编译错误。提示：错误在Child类里，能快速找出来吗？

```java
class Parent {
    public static String staticVar = "Static Variable from Parent";
    private static String privateStaticVar = "Private Static Variable from Parent";

    public static void staticMethod() {
        System.out.println(staticVar);
    }

    private static void privateStaticMethod() {
        System.out.println(privateStaticVar);
    }
}

class Child extends Parent {
    public void staticMethod() {
        System.out.println("Static method in Child");
    }

    public void display() {
        System.out.println(staticVar);

        System.out.println(privateStaticVar);
        privateStaticMethod();
        
        staticMethod();
}
```

（1）父类的私有变量、私有方法，子类是有**继承**的，但是不能访问。所以`Child.display()`里的以下调用是编译错误的。

```java
System.out.println(privateStaticVar);
privateStaticMethod();
```

（2）子类可以继承，同时也可以访问父类的static变量、方法。但父类的`static`方法大家需要注意，子类是不能**直接覆盖**的，所以以下代码会编译错误。

```java
    public void staticMethod() {
        System.out.println("Static method in Child");
    }
```

正确的做法是为该方法添加一个static修饰符，代表这是子类的一个新方法。这种写法叫做**方法隐藏**，子类和父类中都有一个相同名称和参数的静态方法时，子类的方法将隐藏父类的方法。

```java
    public static void staticMethod() {
        System.out.println("Static method in Child");
    }
```

另外如果父类的方法使用final修饰，子类也是不能覆盖的。

### 1.3 父类不可访问的方法

紧跟着上文代码的例子，父类的方法同样使用`static`修饰，子类的`privateStaticMethod`方法算不算覆盖父类的方法呢？有没有编译报错？

```java
class Parent {
    public static String staticVar = "Static Variable from Parent";
    private static String privateStaticVar = "Private Static Variable from Parent";

    private static void privateStaticMethod() {
        System.out.println(privateStaticVar);
    }
}

class Child extends Parent {
    public void privateStaticMethod() {
        System.out.println(staticVar);
    }
}
```

答案是编译正常。

父类中不可访问的方法，子类编写相同名称和参数的方法并不算覆盖。父类的方法都不能访问了，也就没有覆盖这一说法了。。。





## 2. 接口

### 2.1 访问修饰符的区别

接口和抽象类有三个方面的区别，分布是类的修饰、方法的修饰、变量的修饰。我们往下看看。

（1）类

接口使用`interface`修饰，而抽象类使用`abstract`修饰。当它们作为**外部类**时，只能使用public、default修饰，不能使用private修饰。

（2）方法

**普通接口方法**只能由`public abstract`、`default`、`static`修饰。

**抽象接口方法**可以由所有修饰符修饰，除了final。

总结下，它们两者也有共同点，就是都不能使用**final**修饰。

（3）变量

**普通接口变量**只能由`public static final`修饰。

**抽象接口变量**可以由**所有**修饰符修饰。

### 2.2 静态分派

这算是一个很偏的知识点了，如下代码有三个名为`getType`的重载方法，它们的返回类型相同、方法名也相同，只有入参类型不同。

南哥问：程序执行结果是什么？

```java
public class Test {
    public static void main(String[] args) {
        for(Collection<?> collection: collections) {
            System.out.println(getType(collection));
        }
    }
    
    public static final Collection<?>[] collections = {new HashSet<String>(), new ArrayList<String>()};

    public static String getType(Collection<?> collection) {
        return "Super:collection";
    }
    public static String getType(List<?> list) {
        return "Super:list";
    }
    public String getType(ArrayList<?> list) {
        return "Super:arrayList";
    }
}
```

南哥给大家这么一行代码：`Collection<?> collection = new ArrayList<Integer>()`，**左边**的`Collection<?>`其实是静态类型，右边的`new ArrayList<Integer>()`其实是动态类型。

而**编译器在处理重载方法**时，是根据参数的**静态类型**作为判断依据，而不是根据动态类型。`collections`数组里面的所有实例的静态类型都是`Collection<?>`，`getType`方法也都是执行上文的第一个重载方法。

```sh
# 程序员执行结果
Super:collection
Super:collection
```
# 数据类型和程序运算

## 1. 数据类型

### 1.1 static修饰的变量

本文所有内容在企业考核的笔试题出现频率很高，而且是易错题大家注意下！

南友们在玩Java时有没发现，下面这样一个对象，我们即使没有给变量赋值，在创建它后这个变量依旧会有**默认值**。

```java
class A {
    int a;
}

System.out.println(new A().a);
```

```java
程序执行结果：
0
```

有时前端同学要求后端给个默认值0，我们甚至不用动手，Java编译器就把活给干完。

这实际上是Java语言的一个特性，对于实例变量即成员变量，如果是基本数据类型都会有一个默认值。不同的基本类型默认值不同，我们看看以下各种基本类型的默认值。

```java
int a; //0
short b; //0
long c; //0
float d; //0.0
double e; //0.0
boolean f; //false
byte g; //0
char h; //空字符
```

### 1.2 自动类型提升

（1）Java中的byte、short、char进行数学计算时都会**提升**为int类型，很容易忽略的基础知识，南哥慢慢道来。

以下代码的运行正常吗？

```java
byte b1 = 1, b2 = 2, b3;
b3 = b1 + b2;
```

答案在你意料之中，就是编译报错。

```shell
# 报错内容
java: 不兼容的类型: 从int转换到byte可能会有损失
```

既然byte、short、char进行数学计算时都会提升为int类型，那我们就需要在运行过程中把结果转换成byte类型。正确的做法如下。

```java
b3 = (byte)(b1 + b2);
```

（2）但假如byte变量是这样的写法，我们给b1和b2都加个final，很神奇，编译不会报错。 

```java
final byte b1 = 1, b2 = 2, b3;
b3 = b1 + b2;
```

这种情况是一个特殊情况，Java编译器会为其进行特殊处理，我们称它为编译时常量表达式的求值。b1、b2、b3都是常量值，b3在编译阶段就会被编译器进行赋值，不会涉及到上面我们提到的数学计算提升为int类型，也就不会编译错误。

（3）但如果是这种情况呢？

```java
final byte b1 = 1; byte b2 = 2, b3;
b3 = b1 + b2;
```

以上两个byte变量，只有一个final修饰，也就是说**对b3赋值运算**不能在编译时进行，那这段代码依旧会报错，我们还是需要把结果转换为byte类型。

正确做法如下。

```java
b3 = (byte)(b1 + b2);
```

### 1.3 byte溢出

byte类型的数据范围在-128 ~ 127，当这个值超过127会转变成 - 128。为什么呢？

```java
byte i = 127;
System.out.println(++i);
```

```shell
程序执行结果：
-128
```

byte类型的最大值127在二进制中表示为`01111111`，当我们对127的值增加1时，每位加1后都会产生进位，导致的结果就是所有的位都会翻转（从`01111111`变成`10000000`），而`10000000`十进制的表示就是-128。

### 1.4 Bollean赋值

业务开发编写最多就是条件语句了，特别在迭代年代比较旧的老项目，一套接一套的if语句。

既然见识了那么多条件语句，那以下代码的执行结果是什么？

```java
Boolean flag = false;
if (flag = true) {
    System.out.println("true");
}
else {
    System.out.println("false");
}
```

在Java里，条件判断是有赋值的功能，try语句同样也有。此时falg在条件判断里被赋值了。

```shell
程序执行结果：
true
```



## 2. 程序运算

### 2.1 三元运算符

三元运算符的坑，相信不少南友遇到过。。。我们来看看三元运算符是什么？

> Java中的三元运算符是一种简洁的条件表达式工具，其语法格式为：条件 ? 表达式1 : 表达式2。
>
> 如果`条件`为真（true），则表达式的结果是`表达式1`；如果为假（false），则结果是`表达式2`。

假如是这种情况呢，南哥问：o1最终的数据类型是什么？

```java
Object o1 = true ? new Integer(1) : new Double(2.0);
```

上面的代码行其实等同于这一行。

```java
Object o1 = true ? new Double(1.0) : new Double(2.0);
```

三元运算符的一个非常关键的细节就是类型的统一化。Double类型的数据范围更大于Interger类型，所以Java编译器会对值类型进行**类型提升**，最终把Integer类型提升为Double类型。

### 2.2 自增问题

下面是南哥编写的两个`i++`自增的易错问题，面试考核经常出现在笔试题。

（1）南哥第一问：以下代码执行的结果是什么？

```java
int i = 0;
i = i++ + i; 
```

```shell
程序执行结果：
1
```

（2）南哥第二问：以下代码执行的结果是什么？

```java
int i = 0;
i = i++;
System.out.println(i);
```

```shell
程序执行结果：
0
```

### 2.3 String对象

我们创建一个String对象，JVM在背后实际上做了很多功夫，String对象在常量池、堆内存都有可能存在。我们具体问题来具体分析下。

（1）以下代码段**不包含**引用类型，只是单纯的**字面量拼接**，所以只会创建一个对象存在于常量池中。

```java
String s = "JavaProGuide" + "南哥" + 666;
```

（2）以下代码段**包含**了引用类型，一共创建了3个对象，猜对了吗？

```java
String s = "Hello";
s = s + " world!"
```

"Hello"、" world!"都属于**字面量**，所以它们都会被加入到Java字符串常量池中。

而`s + " world!"`这么一个代码段涉及了**引用类型**，所以它在内存里创建了一个新的String对象，并不存在于常量池，而是存在于堆内存里。

（3）以下代码段一共创建了两个对象，分别存在于常量池、堆内存。

首先new对象会把该String对象放到堆内存里，而过程中会先检查常量池是否存在JavaProGuide

```java
String str = new String("JavaProGuide");
```
