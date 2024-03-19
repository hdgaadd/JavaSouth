package org.codeman.channel;

import lombok.Data;

import java.nio.channels.SelectableChannel;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
@Data
public abstract class AbstractChannelHandlerContext implements ChannelHandlerContext {
    private final DefaultChannelPipeline pipeline;

    volatile AbstractChannelHandlerContext next;

    volatile AbstractChannelHandlerContext prev;

    @Override
    public SelectableChannel channel() {
        return pipeline.channel();
    }

    @Override
    public DefaultChannelPipeline pipeline() {
        return pipeline;
    }

    static void invokeChannelActive(AbstractChannelHandlerContext next) { // 传递该对象，实现调用该对象的private方法
        next.invokeChannelActive();
    }

    private void invokeChannelActive() {
        try {
            ((ChannelInboundHandler) handler()).channelActive(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * pipeline中向后传递
     *
     * @return
     */
    @Override
    public ChannelHandlerContext fireChannelActive() {
        next.invokeChannelActive();
        return this;
    }

    public static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg) {
        next.invokeChannelRead(msg);
    }

    /**
     * 调用head，自定义，tail的channelRead方法处理数据
     *
     * @param msg
     */
    private void invokeChannelRead(Object msg) {
        ((ChannelInboundHandler) handler()).channelRead(this, msg);
    }

    @Override
    public void fireChannelRead(Object msg) {
        next.invokeChannelRead(msg);
    }
}
