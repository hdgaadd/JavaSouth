package com.codeman.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/21
 * @decription 保留需要的
 */
public class Filter {
    public static void main(String[] args) {
        ArrayList<Integer> need = new ArrayList<>(Arrays.asList(1));
        List<Integer> one = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> two = new ArrayList<>(Arrays.asList(1, 2, 3));

        // way-one
        one = one.stream().filter(item -> need.contains(item)).collect(Collectors.toList());
        // way-two
        two = two.stream().filter(item -> {
            if (need.contains(item)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        System.out.println(one);
        System.out.println(two);
    }
}
