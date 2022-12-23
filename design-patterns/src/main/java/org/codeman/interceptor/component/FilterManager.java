package org.codeman.interceptor.component;


import org.codeman.interceptor.filter.Filter;

/**
 * @author hdgaadd
 * created on 2022/03/28
 * description: 拦截管理器
 */
public class FilterManager {
    /**
     * 拦截链
     */
    FilterChain filterChain;

    public FilterManager(Target target) {
        this.filterChain = new FilterChain(target);
    }

    /**
     * 所暴露接口
     *
     * @param filter
     */
    public void setFilters(Filter filter) {
        filterChain.setFilters(filter);
    }

    /**
     * 所暴露接口
     *
     * @param requestParam
     */
    public void executeFilter(String requestParam) {
        filterChain.executeFilters(requestParam);
    }
}
