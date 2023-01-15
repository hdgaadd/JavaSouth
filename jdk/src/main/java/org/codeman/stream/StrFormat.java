package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;
import org.codeman.stream.component.People;
import org.codeman.stream.component.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/07/17
 */
@Slf4j
public class StrFormat {

    private static final List<User> LIST = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};
    private static final List<People> PEOPLES = new ArrayList<People>() {{
        add(new People(111, "people111", "111"));
        add(new People(111, "people111Copy", "111Copy"));
        add(new People(222, "people222", "222"));
    }};

    public static void main(String[] args) {
        // SQL的in
        sqlIn();
        // 把List元素，转换为String，以","分割
        listToString();
    }

    private static void sqlIn() {
        String sqlIn = LIST.stream().map(item -> "'" + item.getId() + "'").collect(Collectors.joining(","));
        log.info("SQL的in：" + "select * from test where id in (" + sqlIn + ")");
    }

    private static void listToString() {
        String listStr = PEOPLES.stream().map(String::valueOf).collect(Collectors.joining(","));
        log.info("把List元素，转换为String，以\",\"分割，去除了toString的[]：" + listStr);
    }
}
