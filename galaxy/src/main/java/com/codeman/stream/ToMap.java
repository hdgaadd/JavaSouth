package com.codeman.stream;

import com.codeman.stream.component.User;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/22
 */
public class ToMap {
    public static void main(String[] args) {
        List<User> list = new ArrayList<User>() {{
            add(new User(1));
            add(new User(2));
        }};

        // 封装类型-业务类型
        encapsulation_business(list);
        // 封装类型-封装类型
        encapsulation_encapsulation(list);

    }

    public static void encapsulation_business(List<User> list) { // [ɪnˌkæpsjuˈleɪʃn]
        // method1
        Map<Integer, User> map1 = list.stream().collect(Collectors.toMap(User::getId, o -> o));

        // method2，代替o -> o
        Map<Integer, User> map2 = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        System.out.println(map1);
        System.out.println(map2);
    }

    public static void encapsulation_encapsulation(List<User> list) {
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(User::getId, User::getId));
        System.out.println(map);
    }
}

