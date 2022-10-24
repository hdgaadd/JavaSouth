package org.codeman.stream.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/04/07
 */
public class ArrayStream {
    private final static int[] nums = {1, 2, 3};

    private final static List<Integer> list = new ArrayList<Integer>(){{add(1); add(2);add(3);}};

    public static void main(String[] args) {

        // 转换为Integer[]
        Integer[] arr = Arrays.stream(nums).boxed().toArray(Integer[]::new);

        // forEach
        Arrays.stream(nums).forEach(o -> System.out.println(o));

        // 把list转换为int[]
        int[] listToArr = list.stream().mapToInt(o -> o).toArray();

        // 把int[]转换为list
        List<int[]> ints = Arrays.asList(nums);

    }
}
