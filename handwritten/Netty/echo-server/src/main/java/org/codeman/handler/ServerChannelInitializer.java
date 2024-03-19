package org.codeman.handler;

import org.codeman.channel.Channel;
import org.codeman.channel.ChannelHandlerContext;
import org.codeman.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/04/21
 */
@Slf4j
@Component
public class ServerChannelInitializer extends ChannelInitializer {
    @Override
    public void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new EchoServerHandler());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }
}
