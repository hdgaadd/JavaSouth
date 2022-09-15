package org.codeman.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hdgaadd
 * Created on 2022/03/21
 */
public class Foreach {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(){{ add(1); add(2); }};
        list.forEach(System.out::println);

        Map<Integer, String> map = new HashMap<>(){{ put(1, "1"); }};
        map.forEach((id, name) -> System.out.println(id + name)); // 括号内键值对
    }
}
