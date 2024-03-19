package org.codeman.handler;

import org.codeman.channel.ChannelHandlerContext;
import org.codeman.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/21
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 管道激活
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 管道数据读取
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("echo-server收到的消息为：" + msg.toString()); // 读取管道数据

        ctx.writeAndFlush("hallo, i am netty-server");
    }

}
