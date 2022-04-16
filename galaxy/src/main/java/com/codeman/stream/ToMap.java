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
    public static void main(String[] args) {
        List<User> list = new ArrayList<User>() {{
            add(new User(1));
            add(new User(2));
        }};
        List<User> listError = new ArrayList<User>() {{
            add(new User(1));
            add(new User(1));
        }};
        // 封装类型-业务类型
        encapsulation_business(list);
        // 封装类型-封装类型
        encapsulation_encapsulation(list);
        // 处理stream流转换为Map，出现的key重复情况
        handleMapDuplicated(listError);

    }

    public static void handleMapDuplicated(List<User> list) {
        Map<Integer, User> map = list.stream().collect(Collectors.toMap(User::getId, Function.identity(), (V1, V2) -> V1));
        log.info(map.toString());
    }

    public static void encapsulation_business(List<User> list) { // [ɪnˌkæpsjuˈleɪʃn]
        // method1
        Map<Integer, User> map1 = list.stream().collect(Collectors.toMap(User::getId, o -> o));

        // method2，代替o -> o
        Map<Integer, User> map2 = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        log.info(map1.toString());
        log.info(map2.toString());
    }

    public static void encapsulation_encapsulation(List<User> list) {
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(User::getId, User::getId));
        log.info(map.toString());
    }
}

