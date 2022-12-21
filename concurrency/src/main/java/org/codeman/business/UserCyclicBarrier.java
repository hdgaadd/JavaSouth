package org.codeman.business;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hdgaadd
 * created on 2022/12/21
 * <p>
 * description:
 *  - 以下1、2步的执行没有先后顺序之分，可以使用多线程并行执行，提高性能
 *  - CountDownLatch可以在线程池场景下，让主线程等待线程池的线程执行完成
 */
@Slf4j
public class UserCyclicBarrier {

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        while (checkIsExist()) {
            CountDownLatch countDownLatch = new CountDownLatch(2);
            executor.execute(() -> {
                log.info("1.查询未校对的订单");
                countDownLatch.countDown();
            });
            executor.execute(() -> {
                log.info("2.查询未校对的库存");
                countDownLatch.countDown();
            });

            // wait 1、2 finish
            countDownLatch.await();

            log.info("3.校对数据并返回结果");
            log.info("4.将校对结果保存到数据库表中");

            break;
        }
    }

    private static boolean checkIsExist() {
        log.info("0.检查是否存在未校对订单");
        return true;
    }
}
