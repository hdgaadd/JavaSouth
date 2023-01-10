package org.codeman.stream.array;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/04/07
 */
public class ArrayStream {

    private final static int[] ARR = {1, 2, 3};

    private final static List<Integer> LIST = new ArrayList<Integer>() {{
        add(1);
        add(2);
        add(3);
    }};

    public static void main(String[] args) {

        // 转换为Integer[]
        Integer[] arr0 = Arrays.stream(ARR).boxed().toArray(Integer[]::new);

        // forEach
        Arrays.stream(ARR).forEach(System.out::println);

        // 将字符串句子自定义排序
        System.out.println(
                Arrays.stream("is2 sentence4 This1 a3".split(" "))
                        .sorted(Comparator.comparing(o -> o.charAt(o.length() - 1)))
                        .map(o -> o.substring(0, o.length() - 1))
                        .collect(Collectors.joining(" "))
        );
    }
}
