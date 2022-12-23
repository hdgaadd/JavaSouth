package org.codeman.oom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hdgaadd
 * created on 2022/12/23
 *
 * description: 本地方法区溢出，线程池都保存在本地方法区
 */
public class NativeMethodOOM {

    public static void main(String[] args) {
        while (true) {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            executor.submit(() ->{});
        }
    }
}
