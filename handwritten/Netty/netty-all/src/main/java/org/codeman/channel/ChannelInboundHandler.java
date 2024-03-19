package org.codeman.channel;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
public interface ChannelInboundHandler extends ChannelHandler {
    void channelActive(ChannelHandlerContext ctx) throws Exception;

    void channelRead(ChannelHandlerContext ctx, Object msg);
}
