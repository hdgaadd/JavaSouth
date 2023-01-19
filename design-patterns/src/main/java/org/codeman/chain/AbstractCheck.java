package org.codeman.chain;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/04/18
 */
@Data
@Slf4j
public abstract class AbstractCheck {

    private AbstractCheck nextChecker;

    public void check(User user) {
        boolean isCheck = doCheck(user);

        if (isCheck) {
            if (!Objects.isNull(getNextChecker())) {
                nextChecker.check(user);
            }
        }
    }

    public AbstractCheck setNextChecker(AbstractCheck nextChecker) {
        this.nextChecker = nextChecker;
        return nextChecker;
    }

    public abstract boolean doCheck(User user);

}
