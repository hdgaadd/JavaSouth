package org.codeman.stream;

import org.codeman.stream.component.People;
import org.codeman.stream.component.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/03/22
 */
@Slf4j
public class ToMap {

    private static final List<User> LIST = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};
    private static final List<User> LIST_ERROR = new ArrayList<User>() {{
        add(new User(1));
        add(new User(1));
    }};
    private static final Map<Integer, String> MAP = new HashMap<Integer, String>() {{
        put(1, "1");
        put(2, "2");
    }};
    private static final List<People> PEOPLES = new ArrayList<People>() {{
        add(new People(111, "people111", "111"));
        add(new People(111, "people111Copy", "111Copy"));
        add(new People(222, "people222", "222"));
    }};
    private static final List<User> LIST_VAL_NULL = new ArrayList<User>() {{
        add(new User(0));
        add(new User());
    }};


    public static void main(String[] args) {
        // 封装类型-业务类型
        encapsulation_business();

        // 封装类型-封装类型
        encapsulation_encapsulation();

        // Map-Map
        mapTOMap();

        // list值和对应的个数转换为Map
        listCountToMap();

        // stream流转换为Map，出现的key重复情况
        handleMapDuplicated();

        // stream转换为map的val不能为null
        nullMap();

        // 处理map的value为null
        handleNullMap();
    }

    private static void handleNullMap() {
        Map<Integer, Integer> nullMap = LIST_VAL_NULL.stream().collect(Collectors.toMap(User::hashCode, o -> Optional.ofNullable(o.getId()).orElse(666666)));
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), nullMap);
    }

    private static void nullMap() {
        try {
            Map<Integer, Integer> nullMap = LIST_VAL_NULL.stream().collect(Collectors.toMap(User::hashCode, User::getId));
        } catch (Exception e) {
            log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), e.toString());
        }
    }

    private static void listCountToMap() {
        Map<Integer, Long> collect = PEOPLES.stream().collect(Collectors.groupingBy(People::getId, Collectors.counting()));
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), collect);
    }

    private static void mapTOMap() {
        Map<String, Integer> collect0 = MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        Map<String, Integer> collect1 = MAP.entrySet().stream().collect(Collectors.toMap(o -> String.valueOf(o.getKey()), Map.Entry::getKey));

        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), collect0);
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), collect1);
    }

    private static void handleMapDuplicated() {
        Map<Integer, User> map = LIST_ERROR.stream().collect(Collectors.toMap(User::getId, Function.identity(), (V1, V2) -> V1));
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), map);
    }

    private static void encapsulation_business() { // [ɪnˌkæpsjuˈleɪʃn]
        // method1
        Map<Integer, User> map1 = LIST.stream().collect(Collectors.toMap(User::getId, o -> o));

        // method2，代替o -> o
        Map<Integer, User> map2 = LIST.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), map1);
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), map2);
    }

    private static void encapsulation_encapsulation() {
        Map<Integer, Integer> map = LIST.stream().collect(Collectors.toMap(User::getId, User::getId));
        log.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), map);
    }
}

