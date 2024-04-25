package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomChatMessageRepository {

    Page<ChatMessage> getChats(Pageable pageable, Long chatRoomId);

    List<ChatMessage> getChatsByRoomIdList(List<Long> chatRoomIdList);

    List<ChatMessage> findByRoomNumber(ChatRoom chatRoom);
}
