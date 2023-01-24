package org.codeman.singleton;

// 懒汉式
public class LazilySingleton {

    private static LazilySingleton lazilySingleton;

    private LazilySingleton() { }

    public static /*synchronized*/ LazilySingleton getSingleton() { // 加锁操作使同一时间只能有一个线程获取对象，消耗了性能
        if (lazilySingleton == null) {
            lazilySingleton = new LazilySingleton();
        }
        return lazilySingleton;
    }

    public static void main(String[] args) {
        System.out.println("取到该单例为：" + LazilySingleton.getSingleton());
    }
}
