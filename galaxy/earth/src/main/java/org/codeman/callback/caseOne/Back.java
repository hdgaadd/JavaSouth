package org.codeman.callback.caseOne;

/**
 * @author hdgaadd
 * Created on 2022/01/02
 */
public class Back { // 类的访问修饰符必须是public，否则出现java.lang.NoSuchMethodException
    public void sout0() {
        System.out.println("0");
    } // 方法访问修饰符必须是public，否则出现java.lang.NoSuchMethodException
    public void sout1() {
        System.out.println("1");
    }
    public void sout2() {
        System.out.println("2");
    }
    public void sout3() {
        System.out.println("3");
    }
    public void sout4() {
        System.out.println("4");
    }
    public void sout5() {
        System.out.println("5");
    }

    public void sout6(String name) {
        System.out.println("halo, 现在出场的是" + name);
    }
}
