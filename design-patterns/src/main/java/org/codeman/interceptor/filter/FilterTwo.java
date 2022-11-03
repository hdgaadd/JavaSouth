package org.codeman.interceptor.filter;

/**
 * @author hdgaadd
 * created on 2022/03/28
 */
public class FilterTwo implements Filter{
    @Override
    public void execute(String requestParam) {
        System.out.println("拦截逻辑two执行，获取参数为：" + requestParam);
    }
}
