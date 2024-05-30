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

### 2.2 可见性原理

> ***面试官：那volatile可见性的原理是什么？***

内存一共分为两种，线程的本地内存和线程外的主内存。对于一个volatile修饰的变量，任何线程对该变量的修改都会同步到**主内存**。而当读一个volatile修饰的变量时，JMM（Java Memory Model）会把该线程对应的**本地内存**置为无效，从而线程读取变量时读取的是主内存。

线程每次读操作都是读取主内存中最新的数据，所以volatile能够实现**可见性**的特性。

### 2.3 volatile局限性

> ***面试官：volatile有什么缺点吗？***

企业生产上还是比较少用到volatile的，对于加锁操作会使用的更多些。

1. synchronized加锁操作虽然开销比volatile大，但却适合复杂的业务场景。而volatile只适用于**状态独立**的场景，例如上文对flag变量的读写。
2. volatile编写的代码是比较难以理解的，不清楚整个流程和原理很难维护代码。
3. volatile不具有原子性的特质，无法保证高并发写操作下的原子性。

volatile的使用具有一定的局限性。如果大家要了解synchronized加锁相关的核心知识、面试重点，可以翻阅我GitHub开源的[JavaGetOffer项目：https://github.com/hdgaadd/JavaGetOffer](https://github.com/hdgaadd/JavaGetOffer)，里面有往期的synchronized文章。
