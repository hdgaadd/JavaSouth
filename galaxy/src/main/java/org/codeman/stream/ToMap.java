package org.codeman.stream;

import org.codeman.stream.component.People;
import org.codeman.stream.component.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/22
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
    private static Map<Integer, String> MAP = new HashMap<Integer, String>() {{
        put(1, "1");
        put(2, "2");
    }};
    private static final List<People> PEOPLES = new ArrayList<People>() {{
        add(new People(111, "people111", "111"));
        add(new People(111, "people111Copy", "111Copy"));
        add(new People(222, "people222", "222"));
    }};

    public static void main(String[] args) {
        // 封装类型-业务类型
        encapsulation_business();
        // 封装类型-封装类型
        encapsulation_encapsulation();
        // 处理stream流转换为Map，出现的key重复情况
        handleMapDuplicated();
        // Map-Map
        mapTOMap();
        // 把list值和对应的个数转换为Map
        listCountToMap();
        // 把List元素，转换为String，以","分割
        listToString();
        // SQL的in
        sqlIn();
    }

    private static void sqlIn() {
        String sqlIn = LIST.stream().map(item -> "'" + item.getId() + "'").collect(Collectors.joining(","));
        log.info("SQL的in：" + "select * from test where id in (" + sqlIn + ")");
    }
    private static void listToString() {
        String listStr = PEOPLES.stream().map(String::valueOf).collect(Collectors.joining(","));
        log.info("把List元素，转换为String，以\",\"分割，去除了toString的[]：" + listStr);
    }

    private static void listCountToMap() {
        Map<Integer, Long> collect = PEOPLES.stream().collect(Collectors.groupingBy(People::getId, Collectors.counting()));
        log.info("把list值和对应的个数转换为Map" + collect);
    }
    private static void mapTOMap() {
        Map<String, Integer> collect = MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        log.info("mapTOMap：" + collect);
    }

    private static void handleMapDuplicated() {
        Map<Integer, User> map = LIST_ERROR.stream().collect(Collectors.toMap(User::getId, Function.identity(), (V1, V2) -> V1));
        log.info("处理stream流转换为Map，出现的key重复情况：" + map.toString());
    }

    public static void encapsulation_business() { // [ɪnˌkæpsjuˈleɪʃn]
        // method1
        Map<Integer, User> map1 = LIST.stream().collect(Collectors.toMap(User::getId, o -> o));

        // method2，代替o -> o
        Map<Integer, User> map2 = LIST.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        log.info("封装类型-业务类型：" + map1.toString());
        log.info("封装类型-业务类型：" + map2.toString());
    }

    public static void encapsulation_encapsulation() {
        Map<Integer, Integer> map = LIST.stream().collect(Collectors.toMap(User::getId, User::getId));
        log.info("封装类型-封装类型：" + map.toString());
    }
}

