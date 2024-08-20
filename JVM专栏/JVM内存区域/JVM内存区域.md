## 1. JVM内存布局

### 1.1 堆内存

我们Java程序员相对C语言老哥来说，南友们不需要写内存管理这些东西。具体什么东西呢？不需要为每个对象去写繁琐的**释放内存**代码。

以下是一个C语言示例，C语言需要显式地使用`free`函数来释放内存。

```c
#include <stdio.h>
#include <stdlib.h>

int main() {
    // 分配内存以存储一个整数
    int *ptr = malloc(sizeof(int));
    if (ptr == NULL) {
        printf("内存分配失败\n");
        return 1;
    }
    // 使用分配的内存
    *ptr = 123;
    printf("存储的整数是: %d\n", *ptr);

    // 完成使用后释放内存
    free(ptr);
    return 0;
}
```

我们把重要的内存管理最高权力交给了JVM虚拟机，总得多多了解JVM虚拟机是如何处理内存管理的、包括JVM内存区域包含了什么，否则线上出了什么故障，不了解原理连解决的思路都没有。

JVM内存布局包含了五部分，分别是堆内存、本地方法栈、虚拟机栈、方法区、程序计数器。南哥画画图，给你加深理解。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/0e5456fa5a3e42c6a5b0fd57231aeb10.png#pic_center)

堆内存的作用很方便记忆，它的唯一目的就是存放对象实例。**成员变量**的变量值无论是基本类型、还是引用类型都存储在堆内存中，而**局部变量**的变量值如果是引用类型则存储在堆内存中。这点下文南哥会继续讲到。

```java
public class JavaSouth {

    // 成员变量：无论是基本类型、还是引用类型都存储在堆内存中
    private int memberInt = 10;
    // 成员变量：无论是基本类型、还是引用类型都存储在堆内存中
    private String memberString = "Hello, World!";

    public void displayInfo() {
        // 局部变量：如果是引用类型则存储在堆内存中
        String localString = new String("Local String");

        System.out.println("Member int: " + memberInt);
        System.out.println("Member String: " + memberString);
        System.out.println("Local String: " + localString);
    }
}
```

JVM的堆内存，在国内也被称为GC堆。说到GC回收，目前主流垃圾回收器都使用了分代收集算法，GC堆被分为了新生代、老年代。

新生代、老年代又使用了不同的垃圾回收算法，如新生代的对象特点就是存活时间短，更适合把内存一分为二的复制算法；而老年代的对象存活时间就相对较长了，各种大对象、小对象也比较复杂，可以使用标记清除算法、标记整理算法。这些南哥在JVM垃圾回收章节有提到过。

### 1.2 虚拟机栈

虚拟机栈是和Java中的**方法**相关的，因为每个方法在被一个线程执行时，都会去创建一个**栈帧**，因此虚拟机栈的生命周期也和线程相同，虚拟机栈也属于线程私有。

虚拟机栈的栈帧包含了这么些东西：局部变量表、操作数栈、动态链接、方法返回地址。难记吧？南哥是这么觉得。

在[Oracle官方文档](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6)中，我们可以了解到虚拟机栈一共会报出`StackOverflowError`、`OutOfMemoryError`两个异常。

> - If the computation in a thread requires a larger Java Virtual Machine stack than is permitted, the Java Virtual Machine throws a `StackOverflowError`.
> - If Java Virtual Machine stacks can be dynamically expanded, and expansion is attempted but insufficient memory can be made available to effect the expansion, or if insufficient memory can be made available to create the initial Java Virtual Machine stack for a new thread, the Java Virtual Machine throws an `OutOfMemoryError`.

翻译过来。

- 如果线程中的计算需要比允许值更大的 Java 虚拟机堆栈，则 Java 虚拟机将抛出`StackOverflowError`，也就是堆栈溢出。
- 如果 Java 虚拟机堆栈可以动态扩展，并且尝试扩展但没有足够的内存来实现扩展，或者没有足够的内存来为新线程创建新的初始 Java 虚拟机堆栈，则 Java 虚拟机将抛出`OutOfMemoryError`，也就是内存溢出。

### 1.3 本地方法栈

本地方法栈和虚拟机栈的作用相差不大，都是为方法的运行提供一个栈帧。众所周知Java很多关于数学计算、系统调用等操作，都利用了C语言的本地方法，这些本地方法也叫Native方法。

南哥给一段由C语言实现的Native方法代码。

下面是String类的intern方法，该方法使用的便是本地方法。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /**
     * 返回字符串对象的规范表示。
     */
    public native String intern();
}
```



### 1.4 方法区

上文跟着南哥我们知道虚拟机栈、本地方法栈提供栈帧，而堆内存提供内存区域。其实方法区也起到提供一个内存区域的作用，方法区存放了类相关的数据：类结构信息、常量、静态变量等。

在[Oracle官方文档](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6)中，我们可以知道方法区会出现`OutOfMemoryError`异常。

> If memory in the method area cannot be made available to satisfy an allocation request, the Java Virtual Machine throws an `OutOfMemoryError`.

如果方法区中的内存不足以满足分配请求，则 Java 虚拟机将抛出`OutOfMemoryError`。

### 1.5 程序计数器

程序计数器的主要作用是存储指向**当前线程**正在执行的JVM指令的地址。

而程序计数器在整个JVM内存布局中，是唯一一个不会出现`OutOfMemoryError`的区域。



### 1.6 变量存储位置

南哥在上文有提到堆内存、方法区具体存放了什么内容，现在我们整理整理Java各种变量的变量名、变量值所存储的位置。

这一点，面试官考得细的话会考到。

1. **成员变量**
   - 变量名作为类的一部分，其结构定义存储在**方法区**。
   - 而变量值无论是基本数据类型还是引用类型，都是存储在**堆内存**中的对象实例内。
2. **类变量**
   - 变量名作为类的一部分，其结构定义也存储在**方法区**。
   - 变量值无论是基本数据类型还是引用类型，都存储在**方法区**中，因为它们属于类级别的数据。
3. **局部变量**
   - 局部变量是存在于方法中的变量，变量名存储在虚拟机栈的**栈帧**中。
   - 而变量值如果是基本数据类型，存储在虚拟机栈的栈帧中；如果是引用类型，变量值存储在**栈**中，但引用所指向的对象本身存储在**堆内存**中。