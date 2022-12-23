package org.codeman.callback.case1.component;

import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/01/02
 */
public class B {
    public void same(List<String> resource) {
        for (String item : resource) {
            System.out.println(item);
        }
    }
}
