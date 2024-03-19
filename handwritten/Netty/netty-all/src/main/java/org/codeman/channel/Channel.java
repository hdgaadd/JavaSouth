package org.codeman.channel;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * @author hdgaadd
 * created on 2022/03/24
 * description: netty网络操作底层规范
 */
public interface Channel {

    SelectionKey getSelectionKey();

    DefaultChannelPipeline pipeline();

    /**
     * 获取channel注册的多路复用器
     *
     * @return
     */
    EventLoop eventLoop();

    SelectableChannel javaChannel();

    Unsafe unsafe();

    void doBind(SocketAddress localAddress) throws Exception;

    /**
     * Channel的协助类，用于进行IO读写操作
     */
    interface Unsafe {
        void read();

        void register(EventLoop eventLoop, ChannelPromise promise);
    }

}
