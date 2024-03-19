package org.codeman.handler;

import org.codeman.aspects.Aspect;
import org.codeman.handler.MyInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyInvocationHandlerImpl implements MyInvocationHandler {

    private Object target; // 被代理类实现

    private Aspect aspect; // 切面

    public MyInvocationHandlerImpl(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    // 替代类把被代理类的method传进来
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        // 可以在这里使用切面
        aspect.before();
        Object result = method.invoke(target, args); // 使用java的反射执行被代理类的原方法
        aspect.after();
        return result;
    }

}
