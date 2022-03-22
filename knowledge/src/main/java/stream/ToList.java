package stream;

import stream.component.User;

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
        List<User> users = new ArrayList<User>(){{ add(new User(1)); add(new User(2)); }};

        List<Integer> list = users.stream().map(User :: getId).collect(Collectors.toList());

        System.out.println(list);
    }
}
