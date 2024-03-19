package org.codeman.channel;

/**
 * @author hdgaadd
 * created on 2022/04/21
 */
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter {
    public abstract void initChannel(C ch) throws Exception;


    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
    }
}
