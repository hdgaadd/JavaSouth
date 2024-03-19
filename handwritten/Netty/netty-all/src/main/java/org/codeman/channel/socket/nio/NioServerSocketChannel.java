package org.codeman.channel.socket.nio;

import org.codeman.channel.Channel;
import org.codeman.channel.nio.AbstractNioChannel;
import lombok.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/03/25
 * description: 第一层规范实现的继承者，添加具体功能
 */
public class NioServerSocketChannel extends AbstractNioChannel {
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    private final NioServerSocketChannelConfig config;

    public NioServerSocketChannel() {
        this(newSocket(DEFAULT_SELECTOR_PROVIDER));
    }

    public NioServerSocketChannel(ServerSocketChannel serverSocketChannel) {
        super(serverSocketChannel, SelectionKey.OP_ACCEPT);
        config = new NioServerSocketChannelConfig(this, serverSocketChannel.socket());
    }

    /**
     * 打开socket通道
     *
     * @param selectorProvider
     * @return
     */
    private static ServerSocketChannel newSocket(SelectorProvider selectorProvider) {
        try {
            ServerSocketChannel serverSocketChannel = selectorProvider.openServerSocketChannel();
            serverSocketChannel.configureBlocking(false);
            return serverSocketChannel;
        } catch (IOException e) {
            throw new RuntimeException("Failed to open a server socket.", e);
        }
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new MessageUnsafe();
    }

    @Override
    public ServerSocketChannel javaChannel() {
        return (ServerSocketChannel) channel;
    }

    @Override
    public void doBind(SocketAddress localAddress) throws Exception {
        javaChannel().bind(localAddress, config.getBacklog());
    }

    /**
     * 服务端创建一个NioSocketChannel，后续处理读写数据
     *
     * @param buf
     * @return
     * @throws Exception
     */
    protected int doReadMessages(List<Object> buf) throws Exception {
        SocketChannel ch = ((ServerSocketChannel) javaChannel()).accept();
        if (ch != null) {
            buf.add(new NioSocketChannel(this, ch));
            return 1;
        } else {
            ch.close();
            return 0;
        }
    }

    /**
     * SelectionKey.OP_ACCEPT
     */
    private final class MessageUnsafe extends AbstractUnsafe {
        private final List<Object> readBuf = new ArrayList<Object>();

        @Override
        public void read() {
            try {
                doReadMessages(readBuf);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Object o : readBuf) {
                pipeline().fireChannelRead(o);
            }
        }
    }

    @Data
    class NioServerSocketChannelConfig {
        private Channel channel;
        protected final ServerSocket javaSocket;
        private volatile int backlog = 1024;

        public NioServerSocketChannelConfig(Channel channel, ServerSocket javaSocket) {
            this.channel = channel;
            this.javaSocket = javaSocket;
        }
    }
}
