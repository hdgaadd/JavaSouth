package org.codeman;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/11/20
 *
 * descirption: while循环时，代码逻辑使用try catch，会使中断失效
 */
@Slf4j
public class InterruptThread implements Runnable {

//    // error
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(2000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    // method1
//    @SneakyThrows
//    @Override
//    public void run() {
//        while (true) {
//            Thread.sleep(2000);
//        }
//    }

    // method2
    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // 中断状态被catch捕获，需要重新设置中断状态，后面才可break
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptThread());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
