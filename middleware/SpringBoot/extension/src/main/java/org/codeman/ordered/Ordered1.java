package org.codeman.ordered;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/17
 */
@Slf4j
@Component
public class Ordered1 implements ApplicationRunner, Ordered {

    @Override
    public void run(ApplicationArguments args) throws Exception {
       log.info("this is Ordered1");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
