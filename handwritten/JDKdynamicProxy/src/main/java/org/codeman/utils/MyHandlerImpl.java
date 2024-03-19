package org.codeman.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 自己创建的增强类必须实现调用处理器
public class MyHandlerImpl implements InvocationHandler {

    private Object target;

    public MyHandlerImpl(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before......");
        method.invoke(target, null);
        System.out.println("after......");
        return null;
    }
}
