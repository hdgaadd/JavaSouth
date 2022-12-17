package org.codeman;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/17
 *
 * description: 策略模式拥有各类实现类，Ordered实现实现类的调用排序
 */
@Slf4j
@Component
public class Ordered0 implements ApplicationRunner, Ordered {

    @Override
    public void run(ApplicationArguments args) throws Exception {
       log.info("this is Ordered0");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
