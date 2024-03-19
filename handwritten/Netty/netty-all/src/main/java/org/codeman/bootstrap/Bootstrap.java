package org.codeman.bootstrap;

import org.codeman.channel.Channel;
import org.codeman.channel.ChannelFuture;
import org.codeman.channel.ChannelInitializer;
import org.codeman.utils.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;

/**
 * @author hdgaadd
 * created on 2022/03/25
 */
public class Bootstrap extends AbstractBootstrap{

    /**
     * 客户端组装自定义handlers到pipeline
     *
     * @param channel
     * @throws Exception
     */
    @Override
    void init(Channel channel) throws Exception {
        ((ChannelInitializer) getHandler()).initChannel(channel);
    }

    public void connect(String inetHost, int inetPort) {
        ChannelFuture channelFuture = initAndRegister();
        Channel channel = channelFuture.channel();

        // 添加监听器（回调函数），在注册完成后发起socket请求
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                try {
                    boolean connected = ((SocketChannel) channel.javaChannel()).connect(new InetSocketAddress(inetHost, inetPort));
                    if (!connected) {
                        channel.getSelectionKey().interestOps(SelectionKey.OP_CONNECT);
                    }
                    System.out.println("echo-client客户端开发端口：" + inetHost + ":" + inetPort);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
