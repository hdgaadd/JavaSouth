package org.codeman.handler;

import org.codeman.channel.ChannelHandlerContext;
import org.codeman.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 管道激活，发送消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("hallo, i am echo-client");
    }

    /**
     * 管道数据读取
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("echo-client收到的消息为：" + msg);
    }
}
