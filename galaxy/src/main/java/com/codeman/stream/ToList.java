package com.codeman.stream;

import com.codeman.stream.component.Doppelganger;
import com.codeman.stream.component.User;

import java.util.ArrayList;
import java.util.List;
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
        List<Integer> users = userList.stream().map(User :: getId).collect(Collectors.toList());
        System.out.println(users);

        // 业务对象
        List<Doppelganger> doppelgangers = userList.stream().map(user -> {
            Doppelganger doppelganger = new Doppelganger();
            doppelganger.setId(user.getId());
            return doppelganger;
        }).collect(Collectors.toList());
        System.out.println(doppelgangers);
    }
}
