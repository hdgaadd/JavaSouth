package org.codeman.callback.caseOne;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hdgaadd
 * Created on 2022/01/02
 */
public class Main {
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
}
