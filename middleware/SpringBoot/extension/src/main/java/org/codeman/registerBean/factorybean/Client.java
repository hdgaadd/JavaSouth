package org.codeman.registerBean.factorybean;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 通过implements FactoryBean把bean交给Spring管理
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserFactoryBean.class);
        context.refresh();

        User user = context.getBean(User.class);
        log.info("the obtained bean is : " + user);
    }
}
