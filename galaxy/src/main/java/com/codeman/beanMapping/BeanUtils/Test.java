package com.codeman.beanMapping.BeanUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author hdgaadd
 * Created on 2021/12/27
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        A a = new A();
        a.setVal(1);
        a.setMine(2);
        B b = new B();
        BeanUtils.copyProperties(a, b);

        log.info(a.toString());
        log.info(b.toString());
    }
}
