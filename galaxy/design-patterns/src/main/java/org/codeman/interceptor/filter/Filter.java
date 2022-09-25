package org.codeman.interceptor.filter;

/**
 * @author hdgaadd
 * Created on 2022/03/28
 * 拦截类之底层规范
 */
public interface Filter {
    void execute(String requestParam);
}
