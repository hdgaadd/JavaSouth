package org.codeman.debug;

import java.util.HashMap;

/**
 * @author hdgaadd
 * created on 2023/01/10
 *
 * - Stack Over: 跳过方法
 * - Step Into: 进入项目方法
 * - Force Step Into: 进入任何方法
 * - Step Out: 跳到方法的下一行
 * - Drop Frame: 跳到方法的当前行
 */
public class Client {
    public static void main(String[] args) {
        method();
        System.out.println();
    }

    private static void method() {
        new HashMap<>().put(null, null);
    }
}
