package org.codeman.business;

import lombok.extern.slf4j.Slf4j;

import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hdgaadd
 * created on 2022/12/21
 *
 * description: 采用生产者消费者场景，生产者不用等待消费者消费完成，1、2步生产，3、4步消费
 */
@Slf4j
public class UseCyclicBarrier {

    private static final Vector<String> orderQueue = new Vector<>();

    private static final Vector<String> stockQueue = new Vector<>();

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final ExecutorService check_executor = Executors.newFixedThreadPool(1);

    private static final CyclicBarrier barrier = new CyclicBarrier(2, () -> check_executor.execute(() -> {
        orderQueue.remove(0);
        stockQueue.remove(0);

        log.info("3.校对数据并返回结果");
        log.info("4.将校对结果保存到数据库表中");
    }));

    public static void main(String[] args) {
        while (checkIsExist()) {
            executor.execute(() -> {
                log.info("1.查询未校对的订单");
                orderQueue.add("order");

                // barrier.await()把计数器 - 1，当两个线程把计数器减为0时，该两个线程才会继续循环执行
                // 同时当计数器减为0时，会通知CyclicBarrier的回调函数执行、计数器重置为2
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.execute(() -> {
                log.info("2.查询未校对的库存");
                stockQueue.add("stock");

                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            break;
        }
    }

    private static boolean checkIsExist() {
        log.info("0.检查是否存在未校对订单");
        return true;
    }
}
