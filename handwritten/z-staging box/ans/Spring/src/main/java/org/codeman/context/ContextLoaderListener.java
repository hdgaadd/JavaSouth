package org.codeman.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author hdgaadd
 * created on 2022/03/03
 * 上下文监控器
 */
public class ContextLoaderListener implements ServletContextListener{

    private WebApplicationContext webApplicationContext;

    public static final String ROOT_CXT_ATTR = "ROOT_CXT";

    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
