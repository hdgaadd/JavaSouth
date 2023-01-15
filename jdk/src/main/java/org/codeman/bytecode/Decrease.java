package org.codeman.bytecode;

import java.util.HashMap;

/**
 * @author hdgaadd
 * created on 2023/01/13
 *
 * description: 减少编译产生的字节码
 */
public class Decrease {

    private static final int INDEX = 0;

    public static void main(String[] args) {
        useTo();
        useFor();
    }

    private static void useTo() {
        int index;
        if ((index = INDEX) == 0) {
            System.out.println(index);
        }
    }

    private static void useFor() {
        for (;;) {
            System.out.println("useFor");
        }
    }
}
