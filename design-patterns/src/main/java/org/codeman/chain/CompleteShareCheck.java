package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
@Slf4j
public class CompleteShareCheck extends AbstractCheck {

    @Override
    public boolean doCheck(User user) {
        if (user.getIsCompleteShareTask()) {
            log.info("complete share check passed");
            return true;
        } else {
            log.error("complete share check failed");
            return false;
        }
    }
}
