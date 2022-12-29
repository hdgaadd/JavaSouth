package org.codeman.registerBean.important;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 通过@important直接创建bean
 */
@Slf4j
@Import(value = User.class)
public class Client2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Client2.class);
        context.refresh();

        User user = context.getBean(User.class);
        log.info("the obtained bean is : " + user);
    }
}
