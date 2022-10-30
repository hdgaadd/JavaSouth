package com.codeman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.METHOD })    //parameter参数 [pəˈræmɪtə(r)]
@Retention(RetentionPolicy.RUNTIME)       //[rɪˈtenʃn]
public @interface LogOperator {
    String operatorName() default "";
}
