package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.dto.ChatMessageResponseDto;

import java.util.List;

public interface ChatMessageService {

    //채팅방과 연관있는 메시지 조회 (채팅방 만들시에 연관되어있는 채팅 가져오기)
    List<ChatMessageResponseDto> findByRoom(ChatRoom chatRoom);

    // 채팅 메시지 저장
    void save(ChatMessageDto chatMessageDto, Long chatRoomId);

    // 하나의 채팅방에 있는 채팅들 조회하기 (채팅방 들어갔을 시)
    List<ChatMessageResponseDto> getChats(int page, Long chatRoomId);

    // 채팅방마다 채팅 가져오기 -> 가장 최근 메시지
//    List<ChatMessageResponseDto> findByRoomChatRoomList(List<ChatRoom> chatRoomList);
    List<ChatMessage> findByRoomChatRoomList(List<ChatRoom> chatRoomList);

    //채팅 업데이트
    Long updateChat(ChatDtoUpdate chatDto, Long chatId);
}
