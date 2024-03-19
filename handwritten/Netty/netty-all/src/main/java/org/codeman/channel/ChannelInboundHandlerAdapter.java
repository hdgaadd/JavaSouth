package org.codeman.channel;

/**
 * @author hdgaadd
 * created on 2022/04/21
 */
public class ChannelInboundHandlerAdapter implements ChannelInboundHandler{
    /**
     * 管道激活
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    /**
     * 管道数据读取
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msg);
    }

}
