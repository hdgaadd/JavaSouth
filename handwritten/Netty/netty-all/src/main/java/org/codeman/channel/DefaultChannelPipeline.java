package org.codeman.channel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.nio.channels.SelectableChannel;

/**
 * @author hdgaadd
 * created on 2022/04/18
 */
@Data
public class DefaultChannelPipeline implements ChannelPipeline { // [ˈpaɪplaɪn]管道

    private AbstractChannelHandlerContext head;

    private AbstractChannelHandlerContext tail;

    private Channel channel;

    public DefaultChannelPipeline(Channel channel) {
        this.channel = channel;
        head = new HeadContext(this);
        tail = new TailContext(this);
        head.next = tail;
        tail.prev = head;
    }

    public final ChannelPipeline addLast(ChannelHandler... handlers) {
        for (ChannelHandler h : handlers) {
            if (h == null) {
                break;
            }

            //添加Handler
            addLast(null, h);
        }

        return this;
    }

    public final ChannelPipeline addLast(String name, ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            //将handler封装成DefaultChannelHandlerContext放入pipeline
            newCtx = newContext(handler);
            //将封装后的handler追加到pipeline的后面（tail之前）
            addLast0(newCtx);
        }

        return this;
    }

    private AbstractChannelHandlerContext newContext(ChannelHandler handler) {
        return new DefaultChannelHandlerContext(this, handler);
    }

    /**
     * 添加handler到链路最后（tail之前）
     *
     * @param newCtx
     */
    public void addLast0(AbstractChannelHandlerContext newCtx) {
        AbstractChannelHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }

    public final ChannelPipeline fireChannelActive() { // fire激发
        AbstractChannelHandlerContext.invokeChannelActive(head);
        return this;
    }

    public final ChannelPipeline fireChannelRead(Object msg) {
        AbstractChannelHandlerContext.invokeChannelRead(head, msg);
        return this;
    }

    public SelectableChannel channel() {
        return channel.javaChannel();
    }


    @Getter
    @Setter
    class HeadContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {

        public HeadContext(DefaultChannelPipeline pipeline) {
            super(pipeline);
        }

        /**
         * HeadContext直接向后传递
         *
         * @param ctx
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.fireChannelActive();
        }

        /**
         * 不对数据做任何处理，直接向后传递
         *
         * @param ctx
         * @param msg
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ctx.fireChannelRead(msg);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public SelectableChannel channel() {
            return null;
        }

        @Override
        public DefaultChannelPipeline pipeline() {
            return null;
        }

    }

    @Getter
    @Setter
    class TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {
        public TailContext(DefaultChannelPipeline pipeline) {
            super(pipeline);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        /**
         * 到此结束传递
         *
         * @param ctx
         * @param msg
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public SelectableChannel channel() {
            return null;
        }

        @Override
        public DefaultChannelPipeline pipeline() {
            return null;
        }

    }
}
