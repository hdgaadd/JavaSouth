package org.codeman.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hdgaadd
 * created on 2023/01/07
 *
 * ArrayList可能会导致数据覆盖
 */
public class UseCopyOnWriteList {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new CopyOnWriteArrayList<>();
//        List<Integer> list = new ArrayList<>();
        new Thread(() -> {
            for (int i = 0; i < 2000; i++) list.add(1);
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 2000; i++) list.add(1);
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 2000; i++) list.add(1);
        }).start();

        Thread.sleep(1000);
        System.out.println(list.size());
        System.out.println(list.get(5999));
    }
}
