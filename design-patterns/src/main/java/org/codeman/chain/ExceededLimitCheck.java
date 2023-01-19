package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
@Slf4j
public class ExceededLimitCheck extends AbstractCheck {

    @Override
    public boolean doCheck(User user) {
        if (user.getIsExceededLimit()) {
            log.info("exceeded limit check passed");
            return true;
        } else {
            log.error("exceeded limit check failed");
            return false;
        }
    }
}
