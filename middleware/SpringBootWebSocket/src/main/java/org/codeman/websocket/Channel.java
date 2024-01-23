package org.codeman.websocket;

/**
 * @author hdgaadd
 * created on 2024/01/23
 */

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * @author hdgaadd
 * created on 2024/01/23
 *
 * description:
 * - <a href="http://localhost:8080/">...</a>
 * - 打开浏览器控制台手动对话命令：websocket.send("message");
 */
@ServerEndpoint(value = "/channel/echo")
public class Channel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Channel.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        LOGGER.info("[websocket] 新的连接：客户端id={}", this.session.getId());
    }

    @OnMessage
    public void onMessage(String message) throws IOException{
        LOGGER.info("[websocket] 服务端收到客户端{}消息：message={}", this.session.getId(), message);
        this.session.getAsyncRemote().sendText("halo, 客户端" + this.session.getId());
    }

    @OnClose
    public void onClose(CloseReason closeReason){
        LOGGER.info("[websocket] 客户端主动连接断开：客户端id={}，reason={}", this.session.getId(), closeReason);
    }

    @OnError
    public void onError(Throwable throwable) throws IOException {
        LOGGER.info("[websocket] 连接异常：客户端id={}，throwable={}", this.session.getId(), throwable.getMessage());
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }
}
