package org.codeman.service;

public class ServiceImpl implements Service {

    public void service0() throws Throwable {
        System.out.println("org/codeman/service");

        // 自调用会失效
        service1();
    }

    @Override
    public void service1() throws Throwable {
        System.out.println("动态代理会代理所有方法");
    }

}
