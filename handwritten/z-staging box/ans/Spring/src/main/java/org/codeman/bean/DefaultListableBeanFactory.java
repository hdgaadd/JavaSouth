package org.codeman.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hdgaadd
 * created on 2022/03/02
 * description:
 * 底层规范实现者
 * 生产所有bean的工厂
 */
public class DefaultListableBeanFactory implements BeanFactory {

    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    /**
     * 单例bean
     */
    private Map<String, Object> singletons = new HashMap<>();


    @Override
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void setBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        this.beanDefinitions.addAll(beanDefinitions);
    }

    public Map<String, Object> getSingletons() {
        return singletons;
    }

    // TODO: some methods to do, I am tired

    /**
     * 将所保存的BeanDefinition进行实例化 -> 注册
     */
    public void instanceBeans() {

    }

    /**
     * 对所注册的BeanDefinition进行属性注入
     */
    public void injectObjects() {

    }
}
