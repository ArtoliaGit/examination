package com.bsoft.examination.util;

import com.bsoft.examination.common.Result;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 工具类
 */
public class WebSocketUtil {

    private final static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 存储session
     * @param session 会话
     */
    public static void putSession(Session session) {
        sessionMap.put(session.getId(), session);
    }

    /**
     * 移除session
     * @param session 会话
     */
    public static void removeSession(Session session) {
        sessionMap.remove(session.getId());
    }

    /**
     * 发送消息
     * @param session 会话
     * @param message 消息
     * @return 发送消息状态
     */
    public static boolean sendMessage(Session session, Object message) {
        if (session == null) {
            return false;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return false;
        }
        try {
            basic.sendObject(message);
            return true;
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送消息给所有人
     * @param result 消息
     * @return 发送状态
     */
    public static boolean sendAll(Result result) {
        if (sessionMap.isEmpty()) {
            return false;
        }

        boolean flag = true;
        for (Map.Entry<String, Session> item : sessionMap.entrySet()) {
            Session session = item.getValue();
            flag = sendMessage(session, result);
        }
        return flag;
    }
}
