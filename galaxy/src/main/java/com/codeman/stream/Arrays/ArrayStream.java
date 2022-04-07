package com.codeman.stream.Arrays;

import java.util.Arrays;

/**
 * @author hdgaadd
 * Created on 2022/04/07
 */
public class ArrayStream {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        // 转换为Integer[]
        Integer[] arr = Arrays.stream(nums).boxed().toArray(Integer[]::new);

        // forEach
        Arrays.stream(nums).forEach(o -> System.out.println(o));

        System.out.println(Arrays.toString(arr));
    }
}
