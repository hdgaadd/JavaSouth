package org.codeman;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * description: 指定阻塞、唤醒某线程，相对于ReentrantLock，可以在不用争夺同一锁资源的情况下，唤醒某线程
 */
@Slf4j
public class UseLockSupport {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            log.info(String.format("%s is running", Thread.currentThread().getName()));
            LockSupport.park();

            log.info(String.format("%s end", Thread.currentThread().getName()));
        });
        thread.start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.unpark(thread);
        }).start();
    }

}
