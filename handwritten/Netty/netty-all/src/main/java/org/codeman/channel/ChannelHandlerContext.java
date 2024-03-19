package org.codeman.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
public interface ChannelHandlerContext extends ChannelHandler{
    ChannelHandler handler();

    ChannelHandlerContext fireChannelActive();

    void fireChannelRead(Object msg);

    SelectableChannel channel();

    DefaultChannelPipeline pipeline(); // [ˈpaɪplaɪn]渠道

    /**
     * 服务端向管道写数据，响应客户端
     *
     * @param response
     */
    default void writeAndFlush(String response) {
        byte[] bytes = response.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        try {
            ((SocketChannel) channel()).write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
