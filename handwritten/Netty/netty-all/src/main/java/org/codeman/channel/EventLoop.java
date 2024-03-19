package org.codeman.channel;

import org.codeman.utils.concurrent.EventExecutor;

import java.nio.channels.Selector;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public interface EventLoop extends EventExecutor {

    Selector selector(); // ??
}
