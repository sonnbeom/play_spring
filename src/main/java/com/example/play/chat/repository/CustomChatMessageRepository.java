package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chatroom.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomChatMessageRepository {

    Page<ChatMessage> getChats(Pageable pageable, Long chatRoomId);

    List<ChatMessage> getChatsByRoomList(List<ChatRoom> chatRoomList);

    List<ChatMessage> findByRoomNumber(ChatRoom chatRoom);
}
