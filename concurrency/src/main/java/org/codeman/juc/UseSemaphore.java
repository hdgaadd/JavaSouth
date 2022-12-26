package org.codeman.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * description: Semaphore限制获取某资源的线程数量
 */
@Slf4j
public class UseSemaphore {

    // 默认非公平
    private static final Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {
        for (int i = 0; i < 15; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();

                    log.info(String.format("%s obtained permission", Thread.currentThread().getName()));
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
