package com.codeman;

/**
 * @author hdgaadd
 * Created on 2022/02/23
 *
 * 方法对x个参数的处理逻辑一致，以下可避免浪费代码量
 */
public class Main {
    public static void main(String[] args) {
        test("hdgaadd");

        System.out.println("=====================================");

        test("hdgaadd", "codeman");
    }

    public static void test(String... str) {
        for (String s : str) {
            System.out.println(s);
        }
    }
}
