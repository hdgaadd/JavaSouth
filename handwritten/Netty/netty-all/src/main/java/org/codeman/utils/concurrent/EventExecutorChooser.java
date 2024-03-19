package org.codeman.utils.concurrent;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public interface EventExecutorChooser {

    EventExecutor next();
}
