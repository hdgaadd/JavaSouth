package com.codeman.stream;

import com.codeman.stream.component.Doppelganger;
import com.codeman.stream.component.User;
import org.omg.PortableInterceptor.INACTIVE;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/21
 * @description 转换数据
 */
public class ToList {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<User>(){{ add(new User(1)); add(new User(2)); }};

        // 基本数据类型
        base(userList);

        // 业务对象
        business(userList);

        // 确保传入的List不为空，否则List为空，使用stream()会抛出NullPointException
        guaranteeNotNull(userList);
        guaranteeNull(null); // error
    }

    public static void base(List<User> list) {
        List<Integer> users = list.stream().map(User :: getId).collect(Collectors.toList());
        System.out.println(users);
    }

    public static void business(List<User> list) {
        List<Doppelganger> doppelgangers = list.stream().map(user -> {

            Doppelganger doppelganger = new Doppelganger();
            doppelganger.setId(user.getId());
            return doppelganger;

        }).collect(Collectors.toList());
        System.out.println(doppelgangers);
    }

    public static void guaranteeNotNull(List<User> list) {
        List<Integer> users = Optional.ofNullable(list).orElseGet(ArrayList :: new)
                .stream()
                .filter(Objects :: nonNull)
                .map(User :: getId)
                .collect(Collectors.toList());
        System.out.println(users);
    }

    public static void guaranteeNull(List<User> list) {
        List<Integer> users = list.stream()
                .filter(Objects :: nonNull)
                .map(User :: getId)
                .collect(Collectors.toList());
    }
}
