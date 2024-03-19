package org.codeman.channel.nio;

import lombok.Data;
import org.codeman.channel.Channel;
import org.codeman.channel.ChannelPromise;
import org.codeman.channel.DefaultChannelPipeline;
import org.codeman.channel.EventLoop;
import org.codeman.utils.concurrent.GenericFutureListener;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * @author hdgaadd
 * created on 2022/03/25
 * description: 第一层规范实现
 */
@Data
public abstract class AbstractNioChannel implements Channel {
    /**
     * NioServerSocketChannel
     */
    private Channel parent;

    /**
     * Java自带的Channel
     */
    protected SelectableChannel channel;

    protected int ops;

    protected SelectionKey selectionKey;

    private final Unsafe unsafe;

    private final DefaultChannelPipeline pipeline; // [ˈpaɪplaɪn] 渠道

    private volatile EventLoop eventLoop;

    public AbstractNioChannel(Channel parent, SelectableChannel channel, int ops) {
        this.parent = parent;
        this.unsafe = newUnsafe();
        pipeline = new DefaultChannelPipeline(this);

        this.channel = channel;
        this.ops = ops;
        try {
            //一定要设置为非阻塞态
            this.channel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AbstractNioChannel(SelectableChannel channel, int ops) {
        this.channel = channel;
        this.ops = ops;
        this.unsafe = newUnsafe();
        pipeline = new DefaultChannelPipeline(this);
    }

    protected abstract AbstractUnsafe newUnsafe();

    @Override
    public Unsafe unsafe() {
        return this.unsafe;
    }

    @Override
    public DefaultChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public EventLoop eventLoop() {
        return eventLoop;
    }

    /**
     * socketChannel注册到selector
     *
     * @param promise
     */
    private void register0(ChannelPromise promise) {
        try {
            //提示：netty源码此处注册的ops为0，后续再去修改
            selectionKey = javaChannel().register(eventLoop().selector(), ops, this);

            Object listeners = promise.getListener();
            if (listeners != null) {
                ((GenericFutureListener) listeners).operationComplete(promise);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract class AbstractUnsafe implements Unsafe {

        /**
         * 将当前Unsafe对应的channel注册到EventLoop多路复用器上
         *
         * @param eventLoop
         * @param promise
         */
        @Override
        public void register(EventLoop eventLoop, ChannelPromise promise) {
            AbstractNioChannel.this.eventLoop = eventLoop;

            //首先判断当前线程是否channel对应的NioEventLoop线程，如果是同一线程，直接注册
            if (eventLoop.inEventLoop()) {
                register0(promise);
            } else {
                //如果是其他线程发起的注册操作，需要封装成Runnable任务，放到NioEventLoop线程的任务队列中执行
                eventLoop.execute(() -> register0(promise));
            }
        }
    }
}
