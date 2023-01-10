package org.codeman.singleton;

// 懒汉式
public class LazilySingleton {

    private static LazilySingleton lazilySingleton;

    private LazilySingleton() { }

    public static /*synchronized*/ LazilySingleton getSingleton() {
        if (lazilySingleton == null) {
            lazilySingleton = new LazilySingleton();
        }
        return lazilySingleton;
    }

    public static void main(String[] args) {
        System.out.println("取到该单例为：" + LazilySingleton.getSingleton());
    }
}
