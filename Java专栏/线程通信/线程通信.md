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

在线程同步上，JDK的Lock接口提供了多个实现子类，如下所示。下面我按面试官**面试频率高**的ReentrantLock来讲解。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/24790b034f624de08632b38f79cc8f1f.png#pic_center)


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