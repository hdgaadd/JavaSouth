package org.codeman.netty;

import org.codeman.bootstrap.ServerBootstrap;
import org.codeman.channel.nio.NioEventLoopGroup;
import org.codeman.channel.socket.nio.NioServerSocketChannel;
import org.codeman.handler.ServerChannelInitializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/04/21
 */
@Slf4j
@Data
@Component
public class NettyServer implements CommandLineRunner {
    @Value("${netty.port}")
    private int port;
    @Autowired
    private ServerChannelInitializer serverChannelInitializer;

    @Override
    public void run(String... args) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(serverChannelInitializer);
            serverBootstrap.bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
