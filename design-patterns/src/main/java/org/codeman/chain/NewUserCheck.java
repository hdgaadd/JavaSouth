package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 * <p>
 * description: 组长
 */
@Slf4j
public class NewUserCheck extends AbstractCheck {

    @Override
    public boolean doCheck(User user) {
        if (user.getIsNewUser()) {
            log.info("new user check passed");
            return true;
        } else {
            log.error("new user check failed");
            return false;
        }
    }
}
