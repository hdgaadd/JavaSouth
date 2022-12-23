package org.codeman.interceptor;


import org.codeman.interceptor.component.FilterManager;
import org.codeman.interceptor.component.Target;
import org.codeman.interceptor.filter.FilterOne;
import org.codeman.interceptor.filter.FilterTwo;

/**
 * @author hdgaadd
 * created on 2022/03/28
 *
 * process
 * - 在原本方法执行前运行拦截器方法
 * - 过滤管理器暴露调用接口，实现核心对象拦截链不暴露
 * - 拦截链包含核心代码，内含拦截的执行逻辑、所拦截对象
 * - 过滤管理器外面可包裹一层客户端Client，给用户进行调用，而过滤管理器给内部人员调用
 *
 * knowledge
 * - why要隐藏核心逻辑
 *   避免核心逻辑被轻易调用而造成资源破坏
 */
public class Client {
    public static void main(String[] args) {
        FilterManager filterManager = new FilterManager(new Target());

        filterManager.setFilters(new FilterOne());
        filterManager.setFilters(new FilterTwo());

        filterManager.executeFilter("parameter");
    }
}
