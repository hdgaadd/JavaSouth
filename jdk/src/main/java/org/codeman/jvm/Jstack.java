package org.codeman.jvm;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2023/01/25
 */
/*
C:\>jps
7600 Main
6916 KotlinCompileDaemon
10908 RemoteMavenServer
11660 Jps
8572 Launcher
9388
9420 Jstack

C:\>jstack 9420
2023-01-25 18:33:31
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.311-b11 mixed mode):

"DestroyJavaVM" #13 prio=5 os_prio=0 tid=0x0000025908f72800 nid=0x201c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread-1" #12 prio=5 os_prio=0 tid=0x00000259256f9000 nid=0x13ec waiting for monitor entry [0x0000008eaf4fe000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at org.codeman.jvm.Jstack.transferMoneyFalse(Jstack.java:64)
        - waiting to lock <0x000000076c27c340> (a org.codeman.jvm.Jstack$Account)
        - locked <0x000000076c27c350> (a org.codeman.jvm.Jstack$Account)
        at org.codeman.jvm.Jstack.run(Jstack.java:42)
        at java.lang.Thread.run(Thread.java:748)

"Thread-0" #11 prio=5 os_prio=0 tid=0x00000259256f7000 nid=0x2d9c waiting for monitor entry [0x0000008eaf3fe000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at org.codeman.jvm.Jstack.transferMoneyFalse(Jstack.java:64)
        - waiting to lock <0x000000076c27c350> (a org.codeman.jvm.Jstack$Account)
        - locked <0x000000076c27c340> (a org.codeman.jvm.Jstack$Account)
        at org.codeman.jvm.Jstack.run(Jstack.java:35)
        at java.lang.Thread.run(Thread.java:748)

 */
@AllArgsConstructor
@Slf4j
public class Jstack implements Runnable {

    private static final List<String> BIG_LIST = new ArrayList<>();

    private final int flag;

    private static final Account A = new Account(500);

    private static final Account B = new Account(500);

    @AllArgsConstructor
    @ToString
    static class Account {
        int balance;
    }

    @Override
    public void run() {
        if (flag == 1) {
            try {
                transferMoneyFalse(A, B, 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (flag == 2) {
            try {
                transferMoneyFalse(B, A, 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transferMoneyFalse(Account from, Account to, int amount) throws InterruptedException {
        class Helper {
            private void transfer() {
                if (from.balance - amount < 0) {
                    System.out.println("余额不足，转账失败");
                }
                from.balance -= amount;
                to.balance += amount;
                System.out.println("成功转账" + amount + "元");
            }
        }
        // 双重synchronized适用于在多线程环境下对多个共享资源的修改
        synchronized (from) {
            Thread.sleep(1);
            synchronized (to) {
                new Helper().transfer();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1024 * 1024 * 10; i++) {
            BIG_LIST.add(1 + "");
        }
        Jstack r1 = new Jstack(1);
        Jstack r2 = new Jstack(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        // 检查是否死锁
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        long[] ids = mxBean.findDeadlockedThreads();
        log.warn(String.format("find the number of deadlock is %d", ids.length));
        for (long id : ids) {
            ThreadInfo info = mxBean.getThreadInfo(id);
            log.warn("find deadlock called " + info.getThreadName());
        }
    }
}
