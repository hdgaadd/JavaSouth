package org.codeman.channel.socket.nio;

import org.codeman.channel.Channel;
import org.codeman.channel.nio.AbstractNioChannel;
import lombok.Data;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author hdgaadd
 * created on 2022/03/25
 */
public class NioSocketChannel extends AbstractNioChannel {
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    private NioSocketChannelConfig config;

    public NioSocketChannel() {
        this(DEFAULT_SELECTOR_PROVIDER);
    }

    public NioSocketChannel(SelectorProvider provider) {
        this(newSocket(provider));
    }

    public NioSocketChannel(SocketChannel socket) {
        this(null, socket);
    }

    public NioSocketChannel(SocketChannel socketChannel, int readInterestOp) {
        super(socketChannel, readInterestOp);
    }

    public NioSocketChannel(Channel parent, SocketChannel socketChannel) {
        super(parent, socketChannel, SelectionKey.OP_READ);
        config = new NioSocketChannelConfig(this, socketChannel.socket());
    }

    private static SocketChannel newSocket(SelectorProvider provider) {
        try {
            SocketChannel socketChannel = provider.openSocketChannel();
            socketChannel.configureBlocking(false);
            return socketChannel;
        } catch (IOException e) {
            throw new RuntimeException("Failed to open a socket.", e);
        }
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new SocketChannelUnsafe();
    }

    @Override
    public SocketChannel javaChannel() {
        return (SocketChannel) channel;
    }

    @Override
    public void doBind(SocketAddress localAddress) throws Exception {
    }

    @Data
    class NioSocketChannelConfig {
        private Channel channel;
        protected final Socket javaSocket;

        public NioSocketChannelConfig(Channel channel, Socket javaSocket) {
            this.channel = channel;
            this.javaSocket = javaSocket;
        }
    }

    /**
     * SelectionKey.OP_READ
     */
    private final class SocketChannelUnsafe extends AbstractUnsafe {

        @Override
        public void read() {
            try {
                String msg = readMessage();
                if (msg == null) {
                    return;
                }

                pipeline().fireChannelRead(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 读取socket中数据
         *
         * @return
         * @throws IOException
         */
        private String readMessage() throws IOException {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = javaChannel();

            int readBytes = 0;
            try {
                readBytes = socketChannel.read(byteBuffer);
            } catch (IOException e) {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("链路断开");
                return null;
            }

            if (readBytes > 0) {
                //将缓冲区当前limit设置为position，position设置为0，用于后续缓冲区读取操作
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                return new String(bytes, "UTF-8");
            }

            return null;
        }
    }
}
