package com.example.play.chat.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Log4j2
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    // ws://yourserver.com/websocket?userName=사용자이름" 와 같이 보내면 userName 꺼내서 쓸 수 있다.
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes){
        // ServerHttpRequest를 ServletServerHttpRequest로 형 변환하여, 원래의 HTTP 요청 객체에 접근합니다.
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest req = servletRequest.getServletRequest();
        //HTTP 요청으로부터 HttpServletRequest 객체를 얻어 세션 정보(HttpSession)를 추출합니다.
        HttpSession httpSession = req.getSession();
        //세션 ID(sessionID)를 attributes 맵에 저장합니다.
        //이 맵은 웹소켓 세션과 연결되어, 핸드셰이크 동안 및 이후의 처리에서 사용될 수 있습니다.
        String sessionId = httpSession.getId();
        attributes.put("sessionId", sessionId);
        String userName = req.getParameter("userName");
        attributes.put("userName", userName);
        return true;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("Handshake complete");
    }
}
