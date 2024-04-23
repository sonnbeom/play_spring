package com.example.play.chat.handler;

import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.exception.ChatRoomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    // 소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
        String url = session.getUri().toString();
        String roomNumber = url.split("/chat/")[1];
        Long chatRoomId = Long.parseLong(roomNumber);
        session.getAttributes().put("chatRoomId", chatRoomId);
        log.info("세션에 roomNumber: {}", chatRoomId);
        log.info("소켓 연결 확인 {}", session.getId());
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
        removeClosedSession(session);
        log.info("소켓 연결 종료 {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();
        log.info("payload: {}",payload);

        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        log.info("session: {}", chatMessageDto.toString());
        // 종업원이 자리를 안내해줘야 하는 상황 -> 자리가 없다면 map에 넣음으로써 자리를 마련해준다.
        Long chatRoomId = chatMessageDto.getChatRoomId();
        log.info("chatRoomId in handle{}", chatRoomId);
        //채팅방에 대한 세션이 map에 존재하지 않으면 만들어줌
        if (!chatRoomSessionMap.containsKey(chatRoomId)){
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        Set<WebSocketSession> chatSession = chatRoomSessionMap.get(chatRoomId);
        // message 타입이 ENTER와 동일하다면 키오스크에 테이블 좌석에 손님의 정보를 입력한다.
        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)){
            chatSession.add(session);
        }
        sendMessageToChatRoom(chatMessageDto, chatSession);
    }
    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatSession){
        chatSession.parallelStream().forEach(session -> sendMessage(session, chatMessageDto));
    }

    private <T> void sendMessage(WebSocketSession session, T message){
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            log.info("send Message {}", message.toString());
        } catch (IOException e){
            log.info(e.getMessage(), e);
        }
    }
    private void removeClosedSession(WebSocketSession session){
        Long chatRoomId = (Long) session.getAttributes().get("chatRoomId");

        if (chatRoomId == null){
            log.info("소켓 연결 종료 중 세션 내 chatRoomId가 존재하지 안습니다.");
            throw new ChatRoomException("소켓 연결 종료 중 세션 내 chatRoomId가 존재하지 않습니다.");
        }

        if (!chatRoomSessionMap.containsKey(chatRoomId)){
            throw new ChatRoomException("소켓 연결 종료 중 chatRoomId가 chatRoomSessionMap에 존재하지 않습니다.");
        }

        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);
        chatRoomSession.remove(session);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }
}
