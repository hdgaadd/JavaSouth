package org.codeman.function;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author hdgaadd
 * created on 2022/12/20
 */
public class FunctionDemo {
    public static void main(String[] args) {
        // 定义规则：将String转换为ASCII码数组
        Function<String, int[]> function  = s -> s.chars().distinct().sorted().toArray();

        String str = "abab";
        System.out.println(Arrays.toString(function.apply(str)));
    }
}
