## 1. Queue集合

### 1.1 Queue集合概述

> ***面试官：你说说Queue集合都有什么常用类？***

JDK源码对Queue集合是这么解释的，大家看看。

> A collection designed for holding elements prior to processing. 
>
> 专为在处理之前保存元素而设计的集合。

南哥是这么理解的，List集合用于存储常用元素、Map集合用于存储具有映射关系的元素、Set集合用于存储唯一性的元素。Queue集合呢？所有的数据结构都是为了解决业务问题而生，而Queue集合这种数据结构能够存储具有**先后时间关系**的元素，很适用于在业务高峰期，需要缓存当前任务的业务场景。像Kafka、RabbitMQ、RocketMQ都是队列任务这种思想。

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

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/dacab50f96c34c67b94b394164882591.png#pic_center)


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