package org.codeman.interceptor.filter;

/**
 * @author hdgaadd
 * Created on 2022/03/28
 */
public class FilterOne implements Filter{
    @Override
    public void execute(String requestParam) {
        System.out.println("拦截逻辑执行，获取参数为：" + requestParam);
    }
}
