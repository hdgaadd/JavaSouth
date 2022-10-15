package org.codeman.transfer;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author hdgaadd
 * created on 2022/10/13
 *
 * 死锁reason: 线程a占据着线程b需要的资源, 线程b占据着线程a需要的资源
 * 解决之道: 让两者需竞争同一资源, 而不是竞争对方拥有的资源
 */
@AllArgsConstructor
public class TransferMoney implements Runnable {

    private final int flag;

    private static final Account A = new Account(500);

    private static final Account B = new Account(500);

    private static final Object LOCK = new Object();

    @AllArgsConstructor
    @ToString
    static class Account {
        int balance;
    }

    @Override
    public void run() {
        if (flag == 1) {
            try {
                transferMoney(A, B, 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (flag == 2) {
            try {
                transferMoney(B, A, 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transferMoney(Account from, Account to, int amount) throws InterruptedException {
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

        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (fromHash < toHash) {
            synchronized (from) {
                Thread.sleep(1);
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (to) {
                Thread.sleep(1);
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (LOCK) {
                Thread.sleep(1);
                synchronized (to) {
                    synchronized (from) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TransferMoney r1 = new TransferMoney(1);
        TransferMoney r2 = new TransferMoney(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("a的余额" + A.balance);
        System.out.println("b的余额" + B.balance);
    }
}
