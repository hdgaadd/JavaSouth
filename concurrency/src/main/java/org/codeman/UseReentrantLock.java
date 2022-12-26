package org.codeman;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * description:
 *  - Condition的await阻塞当前线程，同时得到唤醒与获取锁资源
 *  - 执行唤醒某线程的signal，必须在该线程也争夺同一lock的情况下
 */
@Slf4j
public class UseReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Condition condition0 = lock.newCondition();

    private static final Condition condition1 = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                lock.lock();

                for (int i = 1; i < 27; i++) {
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

                for (int i = 65; i < 91; i++) {
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
