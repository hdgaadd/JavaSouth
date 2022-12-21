package org.codeman.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/21
 */
@Slf4j
@Component
public class AnEventApplicationListener implements ApplicationListener<AnEvent> {
    @Override
    public void onApplicationEvent(AnEvent event) {
        log.info("i hear you!");
    }
}
