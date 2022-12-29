package org.codeman.basic;

import lombok.SneakyThrows;

/**
 * @author hdgaadd
 * Created on 2022/08/03
 *
 * descirption: 方法返回，不用等方法中的线程执行完
 */
public class MethodReturn {

    public static void main(String[] args) {
        System.out.println(new MethodReturn().returnSting());
    }

    private String returnSting() {
        for (int i = 0; i < 6; i++) {
            Test2 test2 = new Test2();
            test2.start();
        }
        return "----------------main end----------------";
    }

    private static class Test2 extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            super.run();
            Thread.sleep(700L);
            System.out.println("thread is waking up");
        }
    }

}
