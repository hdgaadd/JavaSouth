package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/03/21
 * @decription 保留需要的
 */
@Slf4j
public class Filter {
    private static final List<Integer> need = new ArrayList<>(Collections.singletonList(1));

    private static List<Integer> one = new ArrayList<>(Arrays.asList(1, 2, 3));

    private static List<Integer> two = new ArrayList<>(Arrays.asList(1, 2, 3));

    private static final Map<Integer, String> map = new HashMap<>() {{
        put(1, "1");
        put(2, "2");
    }};

    public static void main(String[] args) {
        filterList();
        filterMap(map);
    }

    private static void filterList() {
        // way-one
        one = one.stream().filter(item -> need.contains(item)).collect(Collectors.toList());
        // way-two
        two = two.stream().filter(item -> {
            if (need.contains(item)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        log.info(String.valueOf(one));
        log.info(String.valueOf(two));
    }

    private static void filterMap(Map<Integer, String> map) {
        map = map.entrySet().stream().filter(item -> {
            if (item.getKey() == 2) {
                return false;
            }
            return true;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.info(String.valueOf(map));
    }
}
