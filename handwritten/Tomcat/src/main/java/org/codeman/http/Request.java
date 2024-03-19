package org.codeman.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/04/27
 */
public class Request {

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public Request(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public String getUrl() {
        return this.req.uri();
    }

    public String getMethod() {
        return this.req.method().name();
    }

}
