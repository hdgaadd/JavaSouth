package org.codeman.basic;

/**
 * @author hdgaadd
 * created on 2022/11/12
 *
 * description: b线程对共享变量的修改，a线程没有感知
 *
 * knowledge: 1. 若变量为int类型，本例没有参考意义
 *            2. 因为println的线程安全的，底层实现是sychronized，而sychronized在释放锁时，会清除工作内存、同步主内存变量最新值
 */
public class Visibility {

    static /*volatile*/ boolean isRun = true;

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            while (isRun);
        });
        a.start();

        Thread b = new Thread(() -> {
            // 让线程a先run
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isRun = false;
        });
        b.start();
    }
}
