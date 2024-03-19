package org.codeman.context;

import org.codeman.bean.BeanFactory;

import java.lang.reflect.Method;

/**
 * @author hdgaadd
 * created on 2022/03/03
 * description: 第二层规范
 */
public interface ApplicationContext extends BeanFactory {
    /**
     * 初始化上下文容器
     */
    void refresh() throws Exception;

    /**
     * 根据url获取方法对象
     */
    Method getHandlerMethod(String url);

    /**
     * 根据url获取Controller对象
     */
    Object getController(String url);
}
