package org.codeman.bean;

import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/03/17
 */
public class ApplicationXmlResult {
    private String componentScan;

    private List<BeanDefinition> beanDefinitions;

    public String getComponentScan() {
        return componentScan;
    }

    public void setComponentScan(String componentScan) {
        this.componentScan = componentScan;
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void setBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }
}
