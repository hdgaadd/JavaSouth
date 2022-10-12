package org.codeman;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/10/12
 */
@Slf4j
public class CAS implements Runnable {

    private int val = 0;

    private synchronized void swap(int expectVal, int operateVal) {
        if (val == expectVal) {
            val = operateVal;
        }
        log.info("current thread name: {}, the value of val: {}", Thread.currentThread().getName(), val);
    }
    @Override
    public void run() {
        swap(0, 1);
    }

    public static void main(String[] args) throws InterruptedException {
        CAS cas0 = new CAS();
        CAS cas1 = new CAS();

        Thread thread0 = new Thread(cas0, "cas0");
        Thread thread1 = new Thread(cas1, "cas1");

        thread0.start();
        thread1.start();
        thread0.join();
        thread1.join();
    }
}
