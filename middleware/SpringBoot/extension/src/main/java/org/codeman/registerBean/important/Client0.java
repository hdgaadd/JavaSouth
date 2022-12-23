package org.codeman.registerBean.important;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 通过implements ImportSelector指定bean的位置，把bean交给Spring管理
 */
@Slf4j
@Import(value = UseImportSelector.class)
public class Client0 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Client0.class);
        context.refresh();

        User user = context.getBean(User.class);
        log.info("the obtained bean is : " + user);
    }
}
