package org.codeman.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author hdgaadd
 * created on 2022/04/03
 *
 * description: 堆内存溢出
 */
public class HeapMemoryOOM {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        method0();
    }

    private static void method0() {
        StringBuffer buffer = new StringBuffer();
        while (true) {
            buffer.append("hdgaadd");
        }
    }

    private static void method1() throws IllegalAccessException {
        Field declaredField = Unsafe.class.getDeclaredFields()[0]; // [dɪˈkleə(r)]宣布
        declaredField.setAccessible(true);
        Unsafe unsafe = (Unsafe) declaredField.get(null);

        // 分配无限内存，导致内存溢出
        while (true) {
            unsafe.allocateMemory(_1MB); // [ˈæləkeɪt]
        }
    }
}
