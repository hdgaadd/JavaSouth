package org.codeman.spi;

import lombok.extern.slf4j.Slf4j;
import org.codeman.component.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: SPI机制，spring.factories配置UserAutoConfiguration，从而SpringBoot启动就会自动装配UserAutoConfiguration，而该类会注册User
 */
@Slf4j
@SpringBootApplication
public class Client {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Client.class);

        User user = context.getBean(User.class);
        log.info("the obtained bean is : " + user);
    }
}
