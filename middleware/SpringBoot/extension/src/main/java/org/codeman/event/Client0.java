package org.codeman.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 发布event，SpringBoot的Listener监听
 */
@Slf4j
@SpringBootApplication
public class Client0 {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Client0.class);

        context.publishEvent(context.getBean(AnEvent.class));
    }

    @Bean
    AnEvent getEvent() {
        return new AnEvent("an event");
    }
}
