package org.codeman.registerBean.important;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 通过implements ImportBeanDefinitionRegistrar，同时设置bean的属性，把bean交给Spring管理
 */
@Slf4j
@Import(value = UseImportBeanDefinitionRegistrar.class)
public class Client1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Client1.class);
        context.refresh();

        User user = context.getBean(User.class);
        log.info("the obtained bean is : " + user);
    }
}
