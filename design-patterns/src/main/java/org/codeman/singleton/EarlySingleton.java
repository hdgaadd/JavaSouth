package org.codeman.singleton;

// 饿汉式
public class EarlySingleton {
    private static EarlySingleton earlySingleton = new EarlySingleton();

    private EarlySingleton() {
    }

    public static EarlySingleton getSingleton() {
        return earlySingleton;
    }

    public static void main(String[] args) {
        System.out.println("取到该单例为：" + EarlySingleton.getSingleton());
    }
}
