package com.codeman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hdgaadd
 * created on 2022/03/07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BanRepeatSubmit {
    /**
     * Redis锁的存活时间
     * @return
     */
    int lockTime() default 6;
}
