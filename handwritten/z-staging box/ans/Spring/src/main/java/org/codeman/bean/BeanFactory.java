package org.codeman.bean;

/**
 * @author hdgaadd
 * created on 2022/03/02
 * description: 底层规范
 */
public interface BeanFactory {
    Object getBean(String beanName);
}
