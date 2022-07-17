package org.codeman.stream;

import lombok.extern.slf4j.Slf4j;
import org.codeman.stream.component.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/07/17
 */
@Slf4j
public class Str {
    private static final List<User> LIST = new ArrayList<User>() {{
        add(new User(1));
        add(new User(2));
    }};

    public static void main(String[] args) {
        // SQL的in
        sqlIn();
    }

    private static void sqlIn() {
        String sqlIn = LIST.stream().map(item -> "'" + item.getId() + "'").collect(Collectors.joining(","));
        log.info("SQL的in：" + "select * from test where id in (" + sqlIn + ")");
    }
}
