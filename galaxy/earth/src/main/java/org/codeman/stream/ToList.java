package org.codeman.stream;

import org.codeman.stream.component.Doppelganger;
import org.codeman.stream.component.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/03/21
 * description: 转换数据
 */
@Slf4j
public class ToList {
    private static final List<User> userList = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};

    public static void main(String[] args) {
        // 基本数据类型
        base(userList);
        // 业务对象
        business(userList);
        // 确保传入的List不为空，否则List为空，使用stream()会抛出NullPointException
        guaranteeNotNull(userList);
        // error
        guaranteeNull(null);
    }

    private static void base(List<User> list) {
        List<Integer> users = list.stream().map(User::getId).collect(Collectors.toList());
        log.info("基本数据类型：" + users);
    }

    private static void business(List<User> list) {
        List<Doppelganger> doppelgangers = list.stream().map(user -> {
            Doppelganger doppelganger = new Doppelganger();
            doppelganger.setId(user.getId());
            return doppelganger;
        }).collect(Collectors.toList());
        log.info("业务对象：" + doppelgangers);
    }

    private static void guaranteeNotNull(List<User> list) {
        List<Integer> users = Optional.ofNullable(list).orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toList());
        log.info("确保传入的List不为空，否则List为空，使用stream()会抛出NullPointException：" + users);
    }

    public static void guaranteeNull(List<User> list) {
        List<Integer> users = list.stream()
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
