package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/07/10
 */
@Slf4j
public class AnyMatch {
    private static final List<Integer> one = new ArrayList<>();

    private static final List<Integer> two = new ArrayList<>(Arrays.asList(1, 2, 3));

    public static void main(String[] args) {
        log.info("判断是否至少有一个元素不为null: " + one.stream().anyMatch(Objects::nonNull));
        log.info("判断是否至少有一个元素不为null: " + two.stream().anyMatch(Objects::nonNull));
    }
}
