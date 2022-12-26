package org.codeman.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * description: 把if判断修改为入参Enum，把if内部的处理逻辑放到类中
 */
@Slf4j
@SpringBootApplication
public class Client {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Client.class, args);

        FileHandleManager bean = context.getBean(FileHandleManager.class);
        log.info(bean.fileHandle(FileHandleTypeEnum.A_TYPE, "test data"));
        log.info(bean.fileHandle(FileHandleTypeEnum.B_TYPE, "test data"));
    }

}
