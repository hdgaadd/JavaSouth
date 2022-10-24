package org.codeman.interceptor;


import org.codeman.interceptor.component.FilterManager;
import org.codeman.interceptor.component.Target;
import org.codeman.interceptor.filter.FilterOne;
import org.codeman.interceptor.filter.FilterTwo;

/**
 * @author hdgaadd
 * created on 2022/03/28
 */
public class Main {
    public static void main(String[] args) {
        FilterManager filterManager = new FilterManager(new Target());

        filterManager.setFilters(new FilterOne());
        filterManager.setFilters(new FilterTwo());

        filterManager.executeFilter("parameter");
    }
}
