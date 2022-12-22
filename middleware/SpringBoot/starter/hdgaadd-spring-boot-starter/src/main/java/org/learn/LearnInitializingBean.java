package org.learn;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/22
 *
 * description: 设置该bean注入属性后所执行的方法
 */
@Component
public class LearnInitializingBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("this is LearnInitializingBean!");
    }
}
