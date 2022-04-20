package com.codeman.stream;

import com.codeman.stream.component.User;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/22
 */
@Slf4j
public class ToMap {
    private static final List<User> list = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};
    private static final List<User> listError = new ArrayList<User>() {{
        add(new User(1));
        add(new User(1));
    }};
    private static Map<Integer, String> map = new HashMap<Integer, String>() {{
        put(1, "1");
        put(2, "2");
    }};

    public static void main(String[] args) {
        // 封装类型-业务类型
        encapsulation_business();
        // 封装类型-封装类型
        encapsulation_encapsulation();
        // 处理stream流转换为Map，出现的key重复情况
        handleMapDuplicated();
        // Map-Map
        mapTomap();
    }

    private static void mapTomap() {
        Map<String, Integer> collect = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        log.info("mapTomap：" + collect);
    }

    private static void handleMapDuplicated() {
        Map<Integer, User> map = listError.stream().collect(Collectors.toMap(User::getId, Function.identity(), (V1, V2) -> V1));
        log.info("处理stream流转换为Map，出现的key重复情况：" + map.toString());
    }

    public static void encapsulation_business() { // [ɪnˌkæpsjuˈleɪʃn]
        // method1
        Map<Integer, User> map1 = list.stream().collect(Collectors.toMap(User::getId, o -> o));

        // method2，代替o -> o
        Map<Integer, User> map2 = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        log.info("封装类型-业务类型：" + map1.toString());
        log.info("封装类型-业务类型：" + map2.toString());
    }

    public static void encapsulation_encapsulation() {
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(User::getId, User::getId));
        log.info("封装类型-封装类型：" + map.toString());
    }
}

