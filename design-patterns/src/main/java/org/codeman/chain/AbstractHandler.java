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
public abstract class AbstractHandler {

    private AbstractHandler nextHandler;

    public void handler(Employee employee) {
        boolean isDoHandlerFished = doHandler(employee);

        if (!isDoHandlerFished) {
            if (!Objects.isNull(getNextHandler())) {
                nextHandler.handler(employee);
            } else {
                log.error("handle failed, there is not next handler");
            }
        }
    }

    public AbstractHandler setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract boolean doHandler(Employee employee);

}
