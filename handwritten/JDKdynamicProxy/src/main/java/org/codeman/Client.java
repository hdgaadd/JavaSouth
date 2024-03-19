package org.codeman;

import org.codeman.aspects.LikeAspect;
import org.codeman.aspects.TimeIntervalAspect;
import org.codeman.service.Service;
import org.codeman.service.User;
import org.codeman.service.ServiceImpl;
import org.codeman.service.UserImpl;
import org.codeman.utils.MyHandlerImpl;
import org.codeman.utils.ProxyUtil;

import java.lang.reflect.Proxy;

/**
 * @author hdgaadd
 *
 * description: 修改被代理对象的字节码，把所有方法都跳转到invoke()，在invoke()调用before()、after()
 */
public class Client {
    public static void main(String[] args) throws Throwable {
        System.out.println("---------------普通实现---------------");
        Service normalS = new ServiceImpl();
        normalS.service0();
        User normalU = new UserImpl();
        normalU.user();

        System.out.println("\n\r" + "---------------JDKdynamicProxy动态代理实现 + hutool的切面---------------");
        Service myS = (Service) ProxyUtil.proxy(new ServiceImpl(), LikeAspect.class); // 创建代理类
        myS.service0();
        myS.service1();
        User myU = (User) ProxyUtil.proxy(new UserImpl(), TimeIntervalAspect.class);
        myU.user();

        System.out.println("\n\r" + "---------------JDK源码动态代理实现---------------");
        Service jdkS = new ServiceImpl();
        MyHandlerImpl manHandler = new MyHandlerImpl(jdkS);
        // 把通过被代理类实现创建的内存加载对象、被代理类的接口、调用处理器传递给Proxy的newProxyInstance方法
        Service o = (Service) Proxy.newProxyInstance(jdkS.getClass().getClassLoader(), jdkS.getClass().getInterfaces(), manHandler);
        o.service0();
    }

}
