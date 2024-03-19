package org.codeman.channel.nio;

import lombok.Data;
import org.codeman.channel.*;
import org.codeman.utils.concurrent.DefaultThreadFactory;
import org.codeman.utils.concurrent.EventExecutor;
import org.codeman.utils.concurrent.EventExecutorChooser;
import org.codeman.utils.concurrent.ThreadPerTaskExecutor;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hdgaadd
 * created on 2022/03/25
 */
@Data
public class NioEventLoopGroup implements EventLoopGroup {
    private static final int DEFAULT_EVENT_LOOP_THREADS;

    static {
        DEFAULT_EVENT_LOOP_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    }

    private EventExecutor[] children;
    private EventExecutorChooser chooser;

    public NioEventLoopGroup() {
        this(DEFAULT_EVENT_LOOP_THREADS);
    }

    public NioEventLoopGroup(int nThreads) {
        this(nThreads, null, SelectorProvider.provider(), DefaultSelectStrategy.INSTANCE);
    }

    public NioEventLoopGroup(int nThreads, Executor executor, Object... args) {
        if (executor == null) {
            executor = new ThreadPerTaskExecutor(new DefaultThreadFactory(getClass()));
        }

        this.children = new NioEventLoop[nThreads];
        for (int i = 0; i < nThreads; i++) {
            this.children[i] = newChild(executor, args);
        }

        chooser = new GenericEventExecutorChooser(children);
    }

    private EventExecutor newChild(Executor executor, Object... args) {
        return new NioEventLoop(this, executor, (SelectorProvider) args[0], ((SelectStrategy) args[1]));
    }

    @Override
    public EventExecutor next() {
        return chooser.next();
    }

    @Override
    public ChannelFuture register(Channel channel) {
        return next().register(channel);
    }

    @Data
    static class GenericEventExecutorChooser implements EventExecutorChooser {
        private final AtomicLong idx = new AtomicLong();
        private final EventExecutor[] executors;

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[(int) Math.abs(idx.getAndIncrement() % executors.length)];
        }
    }
}
