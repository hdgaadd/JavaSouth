package org.codeman.aspects;

public class TimeIntervalAspect extends SimpleAspect {

    public void before(){
        System.out.println("------before方法------");
    }

    public void after(){
        System.out.println("------after方法------");
    }

}
