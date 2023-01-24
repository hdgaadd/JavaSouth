package org.codeman;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author hdgaadd
 * created on 2023/01/24
 */
public class UseCallable {
    public static void main(String[] args) {
        // 有throws Exception，zero异常会抛给上级，则下面的try/catch可以捕获到
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int i = 3 / 0;
                return 1;
            }
        };
        try {
            FutureTask<Integer> task = new FutureTask<>(callable);
            task.run();
            task.get();

            // 使用new Thead，callable的异常不会捕获到
            // new Thread(new FutureTask<>(callable)).start();
        } catch (Exception e) {
            System.out.println(e);
        }

        // 没有throws Exception，zero异常直接在当前级别报错，则下面的try/catch不可以捕获到
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = 3 / 0;
            }
        };
        try {
            new Thread(runnable).start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
