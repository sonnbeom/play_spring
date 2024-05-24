package com.example.play.chatroom.service;

import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chatroom.dto.ChatRoomWithMessageDto;
import com.example.play.chatroom.dto.ChatRoomsWithChatsDto;
import com.example.play.chatroom.dto.RequestChatRoomDto;

import java.util.List;

public interface ChatRoomService {

    // 채팅방 생성
    ChatRoomWithMessageDto makeRoom(RequestChatRoomDto requestChatRoomDto, String email);

    // 멤버의 채팅방 리스트 가져오기
    List<ChatRoomsWithChatsDto> getChatRooms(int page, String memberEmail);
    //id로 채팅방 조회
    ChatRoom findById(Long id);
}
