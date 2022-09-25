package org.codeman.interceptor.component;

/**
 * @author hdgaadd
 * Created on 2022/03/28
 * @description 所拦截对象
 */
public class Target {
    public void execute(String requestParam) {
        System.out.println("原本逻辑执行，获取参数为：" + requestParam);
    }
}
