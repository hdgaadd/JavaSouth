package org.codeman.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hdgaadd
 * created on 2022/12/20
 */
public class UseFunction {
    public static void main(String[] args) {
        // 定义规则：将String转换为ASCII码数组
        Function<String, int[]> function  = s -> s.chars().distinct().sorted().toArray();

        // 处理单个字符串
        System.out.println(Arrays.toString(function.apply("aab")));

        // 处理stream流里的每个字符串
        List<int[]> list0 = new ArrayList<String>(){{ add("aab"); }}.stream().map(function).collect(Collectors.toList());
        List<int[]> list1 = Stream.of("aab").map(function).collect(Collectors.toList());
        list0.forEach(o -> System.out.println(Arrays.toString(o)));
        list1.forEach(o -> System.out.println(Arrays.toString(o)));
    }
}
