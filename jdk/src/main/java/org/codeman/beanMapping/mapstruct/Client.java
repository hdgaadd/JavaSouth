package org.codeman.beanMapping.mapstruct;

/**
 * @author hdgaadd
 * created on 2021/12/27
 */
public class Client {
    public static void main(String[] args) {
        A a = new A();
        a.setVal(1);
        B b = Convert.INSTANCE.aToB(a);
        System.out.println(b);
    }
}
