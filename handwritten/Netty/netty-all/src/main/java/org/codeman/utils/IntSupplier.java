package org.codeman.utils;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
@FunctionalInterface // @FunctionalInterface修饰函数式接口，设置该类只能拥有一个抽象接口，函数式接口表示使用该对象作为入参时，必须实现该抽象接口
public interface IntSupplier { // [səˈplaɪə(r)]供应商

    int get() throws Exception;
}
