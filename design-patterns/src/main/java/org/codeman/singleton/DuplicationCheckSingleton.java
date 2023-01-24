package org.codeman.singleton;

// 双重检查：集合了饿汉式的线程安全、懒汉式的节省内存空间，同时相对饿汉式的性能更高
public class DuplicationCheckSingleton {

    // 防止synchronized中的代码指令重排序，可能出现以下1、3步指令执行完，但2指令没有，synchronized就释放锁
    // 1.分配内存空间就 2.初始化对象 3.将引用赋值给变量
    // synchronized保证三大特性，其中有序性指的是满足以下的as-if-serial规则，可以看成程序是按序执行，即满足有序性条件，与以上的指令重排区分，非保证指令的有序性
    // as-if-serial规则：不管指令怎么重排序，单线程情况下，执行的结果不被改变即满足
    private static volatile DuplicationCheckSingleton singletonBean;

    private DuplicationCheckSingleton() { }

    public static DuplicationCheckSingleton getSingleton() {
        if (singletonBean == null) {
            synchronized (DuplicationCheckSingleton.class) {
                if (singletonBean == null) {
                    singletonBean = new DuplicationCheckSingleton();
                }
            }
        }
        return singletonBean;
    }

    public static void main(String[] args) {
        System.out.println("取到该单例为：" + DuplicationCheckSingleton.getSingleton());
    }
}
