package org.codeman.callback.case0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hdgaadd
 * created on 2022/01/02
 */
public class Client {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // 通过回调函数，调用n个方法
        for (int i = 1; i < 6; i++) {
            String methodName = "sout" + i;
            Method m1 = Back.class.getMethod(methodName);
            m1.invoke(Back.class.newInstance());
        }

        System.out.println("--------------------------------------");

        // 通过回调函数，调用n次方法
        String[] arr = new String[]{"hdgaadd", "codeman", "codeYourLife"};
        for (String name : arr) {
            Method m2 = Back.class.getMethod("sout6", String.class);
            m2.invoke(Back.class.newInstance(), name);
        }
    }

    static class Back { // 类的访问修饰符必须是public，否则出现java.lang.NoSuchMethodException

        // 方法访问修饰符必须是public，否则出现java.lang.NoSuchMethodException
        public void sout0() {
            System.out.println("0");
        }
        public void sout1() {
            System.out.println("1");
        }
        public void sout2() {
            System.out.println("2");
        }
        public void sout3() {
            System.out.println("3");
        }
        public void sout4() {
            System.out.println("4");
        }
        public void sout5() {
            System.out.println("5");
        }

        public void sout6(String name) {
            System.out.println("halo, 现在出场的是" + name);
        }
    }
}
