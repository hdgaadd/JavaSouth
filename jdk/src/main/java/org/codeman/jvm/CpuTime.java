package org.codeman.jvm;

import java.time.Duration;
import java.time.Instant;

/**
 * @author hdgaadd
 * created on 2023/01/30
 *
 * description:
 *  - 64M的数组，不同的步长导致的相差4倍的循环次数，处理时间相差无几
 *  - 乘法不消耗CPU时间
 */
public class CpuTime {
    public static void main(String[] args) {
        int LEN = 64 * 1024 * 1024;

        Instant start0 = Instant.now();
        int [] arr0 = new int[LEN];
        int count0 = 0;
        for (int i = 0; i < LEN; i += 2) {
            count0++;
            arr0[i] *= i;
        }
        Instant end0 = Instant.now();
        System.out.println(count0 + " - " + Duration.between(start0, end0).toMillis());

        Instant start1 = Instant.now();
        int [] arr1 = new int[LEN];
        int count1 = 0;
        for (int i = 0; i < LEN; i += 8) {
            count1++;
            arr1[i] *= i;
        }
        Instant end1 = Instant.now();
        System.out.println(count1 + " - " + Duration.between(start1, end1).toMillis());
    }
}
