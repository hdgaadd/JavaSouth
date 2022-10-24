package org.codeman.stream;

import org.codeman.stream.component.People;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/04/22
 */
@Slf4j
public class Sorted {
    private static final List<People> people = new ArrayList<People>() {{
        add(new People(3, "people3", "333"));
        add(new People(1, "people1", "111"));
        add(new People(2, "people2", "222"));
    }};

    public static void main(String[] args) {
        // 对List进行升序排序
        descSorted();
        // 对List进行降序排序
        ascSorted();
    }

    private static void descSorted() {
        List<People> des = people.stream().sorted(Comparator.comparing(People::getId)).collect(Collectors.toList());
        log.info("" + des);
    }

    private static void ascSorted() { // reversed颠倒的[rɪ'vɜ:st]
        List<People> asc = people.stream().sorted(Comparator.comparing(People::getId).reversed()).collect(Collectors.toList());
        log.info("" + asc);
    }

}
