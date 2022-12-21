package org.codeman.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hdgaadd
 * created on 2022/12/21
 */
public class AnEvent extends ApplicationEvent {
    public AnEvent(Object source) {
        super(source);
    }
}
