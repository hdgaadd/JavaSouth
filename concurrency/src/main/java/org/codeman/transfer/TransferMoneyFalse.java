package org.codeman.transfer;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author hdgaadd
 * created on 2022/10/13
 */
@AllArgsConstructor
@Slf4j
public class TransferMoneyFalse implements Runnable {

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
        synchronized (from) {
            Thread.sleep(1);
            synchronized (to) {
                new Helper().transfer();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TransferMoneyFalse r1 = new TransferMoneyFalse(1);
        TransferMoneyFalse r2 = new TransferMoneyFalse(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        // 检查是否死锁
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        long[] ids = mxBean.findDeadlockedThreads();
        for (long id : ids) {
            ThreadInfo info = mxBean.getThreadInfo(id);
            log.warn("find deadlock called " + info.getThreadName());
        }
    }
}
