package org.codeman;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hdgaadd
 * created on 2022/12/16
 *
 * descirpiton: 在for循环插入安全点，使GC可以不用等到for循环结束才进行，实现主线程main不用等待其他用户线程的执行完成
 *
 * reference: https://juejin.cn/post/7139741080597037063
 */
@Slf4j
public class SavePoint {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger number = new AtomicInteger(0);
        Runnable runnable = () -> {
            for (int i = 0; i < 100000000; i++) {
                number.getAndAdd(1);
            }
            log.info(String.format("%s is end", Thread.currentThread().getName()));
        };

//        // int的for循环，把int修改为long，可以变为不可数循环，而不可数循环的每次循环都有插入安全点
//        Runnable runnable = () -> {
//            for (long i = 0; i < 100000000; i++) {
//                number.getAndAdd(1);
//            }
//            log.info(String.format("%s is end", Thread.currentThread().getName()));
//        };

//        // Thread.sleep(0)可以使线程进入安全点
//        Runnable runnable = () -> {
//            for (int i = 0; i < 100000000; i++) {
//                if (i % 1000 == 0) {
//                    try {
//                        Thread.sleep(0);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                number.getAndAdd(1);
//            }
//            log.info(String.format("%s is end", Thread.currentThread().getName()));
//        };

        Thread thread0 = new Thread(runnable);
        Thread thread1 = new Thread(runnable);
        thread0.start();
        thread1.start();

        // Thread.sleep(1000)是native方法，执行完会进行GC，若以上线程没有进入安全点，会导致主线程等待以上线程执行完再执行
        Thread.sleep(1000);
        log.info(String.format("the number is %s", number.get()));
    }
}
