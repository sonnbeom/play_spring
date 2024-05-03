package com.example.play.chat.service;

import com.example.play.chat.dto.ChatRoomWithMessageDto;
import com.example.play.chat.dto.ChatRoomsWithChatsDto;
import com.example.play.chat.dto.RequestChatRoomDto;

import java.util.List;

public interface ChatRoomService {

    // 채팅방 생성
    ChatRoomWithMessageDto makeRoom(RequestChatRoomDto requestChatRoomDto, String email);

    // 멤버의 채팅방 리스트 가져오기
    List<ChatRoomsWithChatsDto> getChatRooms(int page, String memberEmail);
}
