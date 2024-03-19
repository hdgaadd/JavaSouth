package org.codeman.utils.concurrent;

import java.util.EventListener;
import java.util.concurrent.Future;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public interface GenericFutureListener<F extends Future<?>> extends EventListener {

    void operationComplete(F future) throws Exception;

}
