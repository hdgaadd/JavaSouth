package org.codeman.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author hdgaadd
 * created on 2022/12/20
 *
 * description: 事件传播，子容器会传播事件到父容器
 */
@Slf4j
public class Client1 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext childCtx = new AnnotationConfigApplicationContext();
        childCtx.register(AnEventApplicationListener.class);
        childCtx.refresh();

        AnnotationConfigApplicationContext parentCtx = new AnnotationConfigApplicationContext();
        parentCtx.register(AnEventApplicationListener.class);
        parentCtx.refresh();

        childCtx.setParent(parentCtx);
        childCtx.publishEvent(new AnEvent("an event"));
    }

}
