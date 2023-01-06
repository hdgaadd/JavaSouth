package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/03/21
 */
@Slf4j
public class Filter {

    private static final List<Integer> NEED = new ArrayList<>(Collections.singletonList(1));

    private static final List<Integer> ONE = new ArrayList<>(Arrays.asList(1, 2, 3));

    private static final List<Integer> TWO = new ArrayList<>(Arrays.asList(1, 2, 3));

    private static final Map<Integer, String> MAP = new HashMap<>() {{
        put(1, "1");
        put(2, "2");
    }};

    public static void main(String[] args) {
        filterList();
        filterMap();
    }

    private static void filterList() {
        // way-one
        List<Integer> collect1 = ONE.stream().filter(NEED::contains).collect(Collectors.toList());
        // way-two
        List<Integer> collect2 = TWO.stream().filter(item -> {
            if (NEED.contains(item)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        log.info(String.valueOf(collect1));
        log.info(String.valueOf(collect2));
    }

    private static void filterMap() {
        Map<Integer, String> map = MAP.entrySet().stream().filter(o -> o.getKey() != 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        log.info(String.valueOf(map));
    }
}
