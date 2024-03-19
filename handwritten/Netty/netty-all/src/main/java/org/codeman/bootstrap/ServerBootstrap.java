package org.codeman.bootstrap;

import org.codeman.channel.*;
import org.codeman.channel.nio.NioEventLoopGroup;
import lombok.Data;

/**
 * @author hdgaadd
 * created on 2022/03/24
 *
 * description: 服务端的启动类
 *
 * 这种泛型内置的设计目的是什么：设计出发点是让AbstractBootstrap的B为ServerBootstrap
 */
@Data
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, Channel> { // AbstractBootstrap的B和C是该两者，且该B也继承于AbstractBootstrap

    /**
     * 子线程组，处理socket的读与写
     */
    NioEventLoopGroup childGroup;
    /**
     * 服务端组装自定义handlers到pipeline
     */
    ChannelHandler childHandler;

    public ServerBootstrap group(NioEventLoopGroup parentGroup, NioEventLoopGroup childGroup) {
        super.group(parentGroup);
        this.childGroup = childGroup;
        return this;
    }

    public ServerBootstrap childHandler(ChannelHandler childHandler) {
        this.childHandler = childHandler;
        return this;
    }

    /**
     * 组装NioServerSocketChannel的pipeline
     *
     * @param channel
     * @throws Exception
     */
    @Override
    void init(Channel channel) throws Exception {
        DefaultChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new ServerBootstrapAcceptor(childHandler, childGroup));
    }

    /**
     * 给客户端的NioSocketChannel添加自定义Handlers
     */
    @Data
    private static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter {
        private ChannelHandler childHandler;
        private NioEventLoopGroup childGroup;

        public ServerBootstrapAcceptor(ChannelHandler childHandler, NioEventLoopGroup childGroup) {
            this.childHandler = childHandler;
            this.childGroup = childGroup;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        /**
         * 组装客户端的NioSocketChannel的pipeline
         *
         * @param ctx
         * @param msg
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            final Channel child = (Channel) msg;

            try {
                //ServerChannelInitializer
                ((ChannelInitializer) childHandler).initChannel(child);
            } catch (Exception e) {
                e.printStackTrace();
            }

            childGroup.register(child);
        }

    }

}
