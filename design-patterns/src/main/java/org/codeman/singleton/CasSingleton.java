package org.codeman.singleton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hdgaadd
 * created on 2023/01/20
 */
public class CasSingleton {

    private static AtomicReference<CasSingleton> INSTANCE = new AtomicReference();

    public static CasSingleton getInstance() {
        for ( ; ; ) {
            CasSingleton singleton = INSTANCE.get();
            if (singleton != null) {
                return singleton;
            }

            singleton = new CasSingleton();
            if (INSTANCE.compareAndSet(null, singleton)) { // CAS原理利用AtomicReference写操作的原子性
                return singleton;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("获取的单例是：" + CasSingleton.getInstance());
    }
}
