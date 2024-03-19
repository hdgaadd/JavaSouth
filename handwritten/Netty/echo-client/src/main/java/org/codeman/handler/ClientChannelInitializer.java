package org.codeman.handler;

import org.codeman.channel.Channel;
import org.codeman.channel.ChannelInitializer;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
@Component
public class ClientChannelInitializer extends ChannelInitializer {

    @Override
    public void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new EchoClientHandler());
    }
}
