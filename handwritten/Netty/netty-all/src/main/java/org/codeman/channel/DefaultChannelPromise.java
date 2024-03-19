package org.codeman.channel;

import org.codeman.channel.nio.NioEventLoop;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author hdgaadd
 * created on 2022/04/14
 */
public class DefaultChannelPromise extends ChannelPromise {

    private final Channel channel;

    private final NioEventLoop nioEventLoop;

    public DefaultChannelPromise(Channel channel, NioEventLoop nioEventLoop) {
        this.channel = channel;
        this.nioEventLoop = nioEventLoop;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
