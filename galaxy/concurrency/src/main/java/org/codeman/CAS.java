package org.codeman;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author hdgaadd
 * created on 2022/10/12
 *
 * CAS原理
 *
 * compare and swap，无锁解决线程冲突的理论原理，表现形式为乐观锁
 *
 * - 在多线程共享同一变量的场景下，写数据时，对于内存中某一个变量，提供了预期值，当该变量和预期值相等时，把新值写入内存值
 *   因为CAS操作的比较和替换是原子操作，则不怕多线程干扰
 *       do{
 *       	V = B;
 *       } while(V == A)
 * - probleams
 *   因为是乐观锁，大量失败后会占用系统的过多资源
 */
@Slf4j
public class CAS implements Runnable {

    private volatile int val = 0;

    private synchronized void compareSwap(int expectVal, int operateVal) {
        if (val == expectVal) {
            val = operateVal;
        }
        log.info("current thread name: {} , the value of val: {}", Thread.currentThread().getName(), val);
    }

    @Override
    public void run() {
        compareSwap(0, 1);
    }

    public static void main(String[] args) throws InterruptedException {
        CAS cas0 = new CAS();
        CAS cas1 = new CAS();

        int luckyIndex = new Random().nextInt(2);
        log.info("luckyIndex is {}", luckyIndex);
        if (luckyIndex > 0) {
            Thread thread0 = new Thread(cas0, "cas0");
            Thread thread1 = new Thread(cas1, "cas1");

            thread0.start();
            thread1.start();
            thread0.join();
            thread1.join();
        } else {
            Thread thread1 = new Thread(cas1, "cas1");
            Thread thread0 = new Thread(cas0, "cas0");

            thread1.start();
            thread0.start();
            thread1.join();
            thread0.join();
        }
    }
}
