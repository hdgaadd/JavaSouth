package org.codeman.stream;

import org.codeman.stream.component.People;
import lombok.extern.slf4j.Slf4j;
import org.codeman.stream.component.UserSort;

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

    private static final List<People> PEOPLE_LIST = new ArrayList<People>() {{
        add(new People(3, "people3", "333"));
        add(new People(1, "people1", "111"));
        add(new People(2, "people2", "222"));
    }};
    private static final List<UserSort> SORTED = new ArrayList<UserSort>() {{
        add(new UserSort(1, 2));
        add(new UserSort(1, 0));
        add(new UserSort(0, 2));
    }};

    public static void main(String[] args) {
        // 对List进行升序排序
        descSorted();

        // 对List进行降序排序
        ascSorted();

        // 根据a排序后根据b排序
        sorted();
    }

    private static void sorted() {
        List<UserSort> userSortList = SORTED.stream().sorted(Comparator.comparing(UserSort::getSortedId).thenComparing(UserSort::getAddTime, Comparator.reverseOrder())).collect(Collectors.toList());
        log.info("根据a排序后根据b排序：" + userSortList);
    }

    private static void descSorted() {
        List<People> des = PEOPLE_LIST.stream().sorted(Comparator.comparing(People::getId)).collect(Collectors.toList());
        log.info("对List进行升序排序" + des);
    }

    private static void ascSorted() { // reversed颠倒的[rɪ'vɜ:st]
        List<People> asc = PEOPLE_LIST.stream().sorted(Comparator.comparing(People::getId).reversed()).collect(Collectors.toList());
        log.info("对List进行降序排序" + asc);
    }

}
