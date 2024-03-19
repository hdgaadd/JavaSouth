package org.codeman.channel;

import org.codeman.utils.concurrent.EventExecutor;

/**
 * @author hdgaadd
 * created on 2022/04/13
 */
public interface EventLoopGroup {
    EventExecutor next();

    ChannelFuture register(Channel channel);
}
