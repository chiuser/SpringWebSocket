package com.xnpe.club.wbs.interceptor;

import com.xnpe.club.util.Constant;
import org.json.JSONObject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            String INFO = serverHttpRequest.getURI().getPath().split("INFO=")[1];
            if (INFO != null && INFO.length() > 0) {
                JSONObject jsonObject = new JSONObject(INFO);
                String command = jsonObject.getString("command");
                if (command != null && Constant.ENTER_COMMAND.equals(command)) {
                    System.out.println("当前session的ID="+ jsonObject.getString("name"));
                    ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
                    HttpSession session = request.getServletRequest().getSession();
                    map.put(Constant.WEBSOCKET_USERNAME, jsonObject.getString("name"));
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("进来webSocket的afterHandshake拦截器！");
    }
}
