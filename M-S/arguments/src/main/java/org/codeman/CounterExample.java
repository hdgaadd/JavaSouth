package org.codeman;

/**
 * @author hdgaadd
 * Created on 2022/02/23
 */
public class CounterExample {
    public static void main(String[] args) {
        test1("hdgaadd");

        System.out.println("=====================================");

        test2("hdgaadd", "codeman");
    }

    public static void test1(String str) {
        System.out.println(str);
    }

    public static void test2(String str1, String str2) {
        System.out.println(str1);
        System.out.println(str2);
    }
}
