package org.codeman;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/11/20
 *
 * descirption: while循环时，使用try catch，会使中断失效
 */
@Slf4j
public class InterruptThread implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @SneakyThrows
//    @Override
//    public void run() {
//        while (true) {
//            Thread.sleep(2000);
//        }
//    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptThread());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
