package org.codeman.optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author hdgaadd
 * created on 2022/03/11
 *
 * descirptino: 预防传递的业务实体为空的情况
 */
public class UseOptional {
    public static void main(String[] args) {
        List<String> result = isNull(null);
        System.out.println(result);

    }

    /**
     * 预防传递的业务实体为空的情况
     *
     * @param businessEntities
     * @return
     */
    public static List<String> isNull(List<String> businessEntities) {
        return Optional.ofNullable(businessEntities).orElse(new ArrayList<String>(){{
            add("h");
            add("d");
            add("g");
            add("a");
            add("a");
            add("d");
            add("d");
        }});
    }
}
