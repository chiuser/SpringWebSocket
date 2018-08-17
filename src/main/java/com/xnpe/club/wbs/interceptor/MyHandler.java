package com.xnpe.club.wbs.interceptor;

import com.xnpe.club.util.Constant;
import org.json.JSONObject;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyHandler implements WebSocketHandler {

    private static final Map<String, WebSocketSession> sUserMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立连接");
        String INFO = session.getUri().getPath().split("INFO=")[1];
        System.out.println(INFO);
        if (INFO != null && INFO.length() > 0) {
            JSONObject jsonObject = new JSONObject(INFO);
            String command = jsonObject.getString("command");
            if (command != null && Constant.ENTER_COMMAND.equals(command)) {
                sUserMap.put(jsonObject.getString("name"), session);
                session.sendMessage(new TextMessage("成功建立socket连接"));
                System.out.println(session);
            }
        }
        System.out.println("当前在线人数：" + sUserMap.size());
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        try {
            JSONObject jsonobject = new JSONObject(webSocketMessage.getPayload().toString());
            String name, command, message;
            name = jsonobject.getString("name");
            command = jsonobject.getString("command");
            message = jsonobject.getString("message");
            System.out.println(jsonobject.toString());
            System.out.println(message + ":来自" + webSocketSession.getAttributes().get(Constant.WEBSOCKET_USERNAME) + "的消息");
            if (command != null && Constant.MESSAGE_COMMAND.equals(command)) {
                sendMessageToUser(name + "", new TextMessage(
                        "服务器收到了来自 " + webSocketSession.getAttributes().get(Constant.WEBSOCKET_USERNAME) + "的消息：" + message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param name
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String name, TextMessage message) {
        if (sUserMap.get(name) == null) return false;
        WebSocketSession session = sUserMap.get(name);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> nameIds = sUserMap.keySet();
        WebSocketSession session = null;
        for (String name : nameIds) {
            try {
                session = sUserMap.get(name);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }

        return allSendSuccess;
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        System.out.println("连接出错");
        sUserMap.remove(getName(webSocketSession));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭：" + closeStatus);
        sUserMap.remove(getName(webSocketSession));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     */
    private String getName(WebSocketSession session) {
        try {
            String name = (String) session.getAttributes().get(Constant.WEBSOCKET_USERNAME);
            return name;
        } catch (Exception e) {
            return null;
        }
    }

}
