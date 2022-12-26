package org.codeman.automic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hdgaadd
 * created on 2022/03/18
 */
@Slf4j
public class UseAtomic {

    public static void main(String[] args) {
        // AtomicInteger
        atomicInteger_method_one();
        // AtomicInteger
        atomicInteger_method_two();
        // AtomicReference
        atomicReference();
    }

    private static void atomicInteger_method_one() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

        final AtomicInteger count = new AtomicInteger(0);
        list.forEach(count::getAndAdd);

        int baseCount = count.get(); // 转换为基本数据类型
        log.info(baseCount + "");
    }

    private static void atomicInteger_method_two() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

        final AtomicInteger count = new AtomicInteger(0);
        list.forEach(item -> {
            count.getAndUpdate(c -> c + item);
        });

        int baseCount = count.get(); // 转换为基本数据类型
        log.info(baseCount + "");
    }

    private static void atomicReference() {
        AtomicReference<Double> ar = new AtomicReference<>(1D);
        ar.updateAndGet(v -> v + 6D);

        log.info(ar.toString());
    }

}
