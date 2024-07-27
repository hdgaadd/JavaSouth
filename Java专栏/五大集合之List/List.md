> *点赞再看，Java进阶一大半*

南哥在stackoverflow社区看到14年前的这么一个问题：`Java 的 Vector.add() 和 Vector.addElement() 之间的区别`，大家有答案吗？

![微信截图_20240726162623](D:\code\z-mine\JavaGetOffer\Java专栏\五大集合之List\微信截图_20240726162623.png)

它们实际上没有区别！！！当年JDK1.0版本中，JDK就没有提供过系统的集合框架，当时的集合处理老一辈程序员只能使用如Vector、Hashtable。

而随着1998 年 12 月 4 日的JDK 1.2版本发布，Java终于提供了系统的集合框架支持。上面我们提到的Vector则作为List接口的实现之一，`add`方法是List的顶层方法，而Vector自带的`addElement`方法仍然保留，主要是为了向后兼容性。。。

大家好，我是南哥。

一个Java学习与进阶的领路人，相信对你通关面试进入心心念念的公司有所帮助。

本文收录在我开源的《Java学习进阶指南》中，涵盖了在大厂工作的Javaer都不会不懂的核心知识、面试重点。相信能帮助到大家在Java成长路上不迷茫，南哥希望收到大家的 ⭐ Star ⭐支持我完善下去。GitHub地址：[https://github.com/hdgaadd/JavaProGuide](https://github.com/hdgaadd/JavaProGuide)。

### List集合

> List集合都知道哪些对象？

作为四大集合之一的List，在业务开发中我们比较常见的是以下 3 种：ArrayList、Vector、LinkedList，业务开发我们接触最多就是容器类库了，容器类库可以说是面向对象语言最重要的类库。大家看看在工作里你比较熟悉的是哪个？

![微信截图_20240725162032](D:\code\z-mine\JavaGetOffer\Java专栏\五大集合之List\微信截图_20240725162032.png)

这篇文章南哥打算专注于List集合，后面四大集合之Map、Queue、Set后续再来填坑，比心心♥。

> ArrayList为什么线程不安全？

普通的数组类型，我们是这么创建的`int[] arr = new int[66]`。数组可以创建固定长度的容量不会过度浪费资源，但有优点也有缺点。如果长度设置过小，你就会看到一个大大的`ArrayIndexOutOfBoundsException`。

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

### AarrayList面试小tip

另外ArrayList有些小知识点大家也需要记一记，面试官可能照着公司给的面试题稿子问你：

（1）ArrayList初始容量为10。

（2）ArrayList负载因子为1，也代表ArrayList底层数组满了才会扩容。而数组扩容长度为原始长度的**1.5倍**。

（3）ArrayList扩容的复杂度为O(n)。



Vector

LinkedList


[戳这，《JavaProGuide》作为一份涵盖Java程序员所需掌握核心知识、面试重点的Java学习进阶指南。](https://github.com/hdgaadd/JavaProGuide)

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/d21d868a4c5c4f3192d2ce68eab171ff.png#pic_center)

欢迎关注南哥的公众号：Java进阶指南针，公众号里有南哥珍藏整理的大量优秀pdf书籍！

我是南哥，南就南在Get到你的有趣评论➕点赞➕关注。

> **创作不易，不妨点赞、收藏、关注支持一下，各位的支持就是我创作的最大动力**❤️