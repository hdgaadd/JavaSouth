package org.codeman.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/23
 */
@Component
@Slf4j
public class CustomAdapter extends AbstractAdapter {

    @Override
    protected void initialized() {
        log.info("CustomAdapter is initialized!");
    }

    @Override
    protected void afterInitialized() {
        log.info("afterInitialized!");
    }
}
