package org.codeman.basic;

/**
 * @author hdgaadd
 * created on 2022/11/12
 *
 * description: b线程对共享变量的修改，a线程没有感知
 *
 * knowledge:
 * - 若变量为int类型，本例没有参考意义
 * - 因为println的线程安全的，底层实现是sychronized，而sychronized在释放锁时，会清除工作内存、同步主内存变量最新值
 * - volatile实现
 *      - volatile读使工作内存同步主内存，且任何写操作无法越过volatile读指令，进行重排序到其前面 (读完才可以写)
 *      - volatile写时将工作内存同步到主内存，且任何读操作无法越过当前volatile写指令，进行重排序到其后面 (读完再写)
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
