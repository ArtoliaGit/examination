package com.bsoft.examination.web.reservation;


import com.bsoft.examination.common.ServerEncoder;
import com.bsoft.examination.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * websocket 接口
 */
@Slf4j
@RestController
@ServerEndpoint(value = "/websocket", encoders = { ServerEncoder.class })
public class WebSocketController {

    @OnOpen
    public void openSession(Session session) {
        WebSocketUtil.putSession(session);
        log.info("websocket 连接: sessionId {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("接收消息内容{}", message);
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketUtil.removeSession(session);
        log.info("websocket 关闭: sessionId {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("websocket 错误: sessionId {}", session.getId());
        log.info("错误信息: {}", throwable.getMessage());
    }

}
