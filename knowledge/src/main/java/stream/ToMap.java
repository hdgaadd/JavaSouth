package stream;

import stream.component.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/22
 */
public class ToMap {
    public static void main(String[] args) {
        List<User> list = new ArrayList<User>(){{ add(new User(1)); add(new User(2)); }};

        Map<Integer, User> map = list.stream().collect(Collectors.toMap(User::getId, o -> o));

        System.out.println(map);
    }
}

