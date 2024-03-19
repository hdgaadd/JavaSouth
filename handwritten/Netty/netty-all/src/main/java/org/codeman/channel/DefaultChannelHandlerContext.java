package org.codeman.channel;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
public class DefaultChannelHandlerContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {

    private ChannelHandler childHandler;

    public DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, ChannelHandler childHandler) {
        super(pipeline);
        this.childHandler = childHandler;
    }

    @Override
    public ChannelHandler handler() {
        return childHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
    }
}
