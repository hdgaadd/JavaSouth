package org.codeman.utils.concurrent;

import org.codeman.channel.Channel;
import org.codeman.channel.ChannelFuture;

import java.util.concurrent.Executor;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public interface EventExecutor extends Executor {
    ChannelFuture register(Channel channel);

    boolean inEventLoop();
}
