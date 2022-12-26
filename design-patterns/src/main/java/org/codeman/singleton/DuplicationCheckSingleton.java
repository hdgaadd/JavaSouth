package org.codeman.singleton;

// 双重检查
public class DuplicationCheckSingleton {

    private static DuplicationCheckSingleton twoSingleton;

    private DuplicationCheckSingleton() { }

    public static synchronized DuplicationCheckSingleton getSingleton() {//线程安全
        if (twoSingleton == null) {
            synchronized (DuplicationCheckSingleton.class) {
                if (twoSingleton == null) {
                    twoSingleton = new DuplicationCheckSingleton();
                }
            }
        }
        return twoSingleton;
    }

    public static void main(String[] args) {
        System.out.println("取到该单例为：" + DuplicationCheckSingleton.getSingleton());
    }
}
