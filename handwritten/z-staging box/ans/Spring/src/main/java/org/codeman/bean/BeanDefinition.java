package org.codeman.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/03/02
 * description: 每个bean被封装成该类
 */
public class BeanDefinition {

    /**
     * bean的id，类似于xml的bean子标签的唯一标识id
     */
    private String beanId;

    /**
     * bean的全限定名
     */
    private String className;

    /**
     * bean的属性，如成员变量
     */
    private List<PropertyDefinition> propertyDefinitions = new ArrayList<>();

    public BeanDefinition(String beanId, String className) {
        this.beanId = beanId;
        this.className = className;
    }

    /**
     * 添加bean的属性
     * @param propertyDefinition
     */
    public void addPropertyDefinition(PropertyDefinition propertyDefinition) {
        if (!propertyDefinitions.isEmpty()) {
            this.propertyDefinitions.add(propertyDefinition);
        }
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<PropertyDefinition> getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(List<PropertyDefinition> propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }
}
