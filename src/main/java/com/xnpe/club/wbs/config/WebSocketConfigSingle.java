package com.xnpe.club.wbs.config;

import com.xnpe.club.wbs.interceptor.MyHandler;
import com.xnpe.club.wbs.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//@Configuration
//@EnableWebSocket
public class WebSocketConfigSingle implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new MyHandler(), "/gs-guide-websocket/{INFO}").setAllowedOrigins("*")
//                .addInterceptors(new WebSocketInterceptor());
    }
}
