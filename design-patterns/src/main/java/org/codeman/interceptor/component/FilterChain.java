package org.codeman.interceptor.component;



import org.codeman.interceptor.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/03/28
 *
 * description: 拦截链
 */
public class FilterChain {
    /**
     * 核心资源：拦截器的具体逻辑
     */
    List<Filter> filters = new ArrayList<>();
    /**
     * 核心资源：所拦截对象
     */
    Target target;

    public FilterChain(Target target) {
        this.target = target;
    }

    /**
     * 核心逻辑
     * @param filter
     */
    public void setFilters(Filter filter) {
        filters.add(filter);
    }

    /**
     * 核心逻辑
     * @param requestParam
     */
    public void executeFilters(String requestParam) {
        // 拦截器执行
        filters.forEach(filter -> filter.execute(requestParam));

        // 原本逻辑执行
        this.target.execute(requestParam);
    }
}
