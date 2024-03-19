package org.codeman.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/04/27
 */
public class Response {

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public Response(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String message) throws Exception {
        if (Objects.isNull(message) || message.length() == 0) {
            return;
        } else {
            try {
                FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                        // HTTP版本号
                        HttpVersion.HTTP_1_1,
                        // HTTP状态码
                        HttpResponseStatus.OK,
                        // HTTP编码格式
                        Unpooled.wrappedBuffer(message.getBytes("UTF-8")) // [ˈræpə(r)]包装纸
                );
                ctx.write(fullHttpResponse); // 把响应消息封装到FullHttpResponse，传输到ChannelHandlerContext
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ctx.flush();
                ctx.close();
            }
        }
    }
}
