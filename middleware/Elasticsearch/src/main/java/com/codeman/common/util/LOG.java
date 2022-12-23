package com.codeman.common.util;

/**
 * @author hdgaadd
 * created on 2021/12/13
 */
public class LOG {

    public static void log(Object o) {
        System.out.println("----------------" + o + "----------------");
    }

    public static void log(Object o1, Object o2) {
        System.out.println("----------------" + o1 + "：" + o2 + "----------------");
    }

    public static void main(String[] args) {
        LOG.log("successful");
        LOG.log("结果为", 1);
    }
}
