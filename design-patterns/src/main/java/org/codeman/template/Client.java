package org.codeman.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hdgaadd
 * created on 2022/12/23
 *
 * description: 模板方法模式，AbstractAdapter定义固定模板，不确定的实现交由子类实现
 */
@Slf4j
@SpringBootApplication
public class Client {
    public static void main(String[] args) {
        SpringApplication.run(Client.class);
    }
}
