package org.codeman.bootstrap;

import lombok.Data;
import org.codeman.channel.*;
import org.codeman.utils.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * @author hdgaadd
 * created on 2022/03/24
 */
@Data
public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> {
    /**
     * 父线程组，主要用于处理客户端的链接
     */
    volatile EventLoopGroup parentGroup;
    /**
     * 工厂类，用于创建NioServerSocketChannel实例
     */
    private volatile ReflectiveChannelFactory channelFactory;
    /**
     * 自定义handler，组装pipeline
     */
    private volatile ChannelHandler handler;

    public B group(EventLoopGroup parentGroup) {
        if (this.parentGroup != null) {
            throw new IllegalStateException("group set already");
        }
        this.parentGroup = parentGroup;
        return (B) this;
    }

    public B channel(Class clazz) {
        channelFactory = new ReflectiveChannelFactory(clazz);
        return (B) this;
    }

    public void bind(int inetPort) {
        bind(new InetSocketAddress(inetPort));
    }

    /**
     * ServerSocketChannel绑定开放端口号
     *
     * @param socketAddress
     */
    private void bind(InetSocketAddress socketAddress) {
        ChannelFuture channelFuture = initAndRegister();

        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                channelFuture.channel().doBind(socketAddress);
                System.out.println("echo-server服务端开放端口：" + socketAddress);
            }
        });
    }

    abstract void init(Channel channel) throws Exception;

    /**
     * 创建NioServerSocketChannel实例，然后注册到parentGroup线程组中
     *
     * @return
     */
    protected ChannelFuture initAndRegister() {
        Channel channel = channelFactory.newChannel();
        try {
            init(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parentGroup.register(channel);
    }

    public B handler(ChannelHandler handler) {
        this.handler = handler;
        return (B) this;
    }
}
