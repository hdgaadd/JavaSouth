package org.codeman.netty;

import org.codeman.bootstrap.Bootstrap;
import org.codeman.channel.nio.NioEventLoopGroup;
import org.codeman.channel.socket.nio.NioSocketChannel;
import org.codeman.handler.ClientChannelInitializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
@Slf4j
@Data
@Component
public class NettyClient implements CommandLineRunner {
    @Value("${netty.host}")
    String host;
    @Value("${netty.port}")
    int port;

    @Autowired
    private ClientChannelInitializer clientChannelInitializer;

    @Override
    public void run(String... args) throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.handler(clientChannelInitializer);
        bootstrap.connect(host, port);
    }
}
