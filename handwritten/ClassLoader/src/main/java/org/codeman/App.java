package org.codeman;

import java.lang.reflect.InvocationTargetException;

/**
 * @author hdgaadd
 * created on 2022/10/09
 *
 * description: 重写findClass, 将类自定义加载成byte[]后，传递给defindClass
 */
public class App {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // class
        Class<?> clazz0 = new ClassLoader(".class").findClass(null);
        clazz0.newInstance();
        System.out.println("\r");

        Class<?> clazz1 = new ClassLoader(".xlass").findClass(null);
        Object object = clazz1.newInstance();
        System.out.println(clazz1.getMethod("hello").invoke(object));
        System.out.println("\r");

        // jar
        Class<?> clazz2 = new JarLoader("").findClass(null);
        clazz2.newInstance();
    }
}
