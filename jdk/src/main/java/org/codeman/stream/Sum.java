package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;
import org.codeman.stream.component.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/05/29
 */
@Slf4j
public class Sum {

    private static final List<User> USERS = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};

    public static void main(String[] args) {
        // 计算List之和，并且转换为Long
        sumLong();
    }

    private static void sumLong() {
        long sum = USERS.stream().mapToLong(User::getId).sum();
        log.info("List的和为：" + sum);
    }
}
