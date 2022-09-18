package org.codeman.singleton;

//饿汉式
public class EarlySingleton {
    private static EarlySingleton earlySingleton = new EarlySingleton();
    private EarlySingleton() {//构造器必须是私有的，否则主程序可通过new EarlySingleton()创造出不同的实例
    }
    public static EarlySingleton getSingleton() {
        return earlySingleton;
    }
    public static void main(String[] args) {
        System.out.println("取到该单例为：" + EarlySingleton.getSingleton());
    }
}
