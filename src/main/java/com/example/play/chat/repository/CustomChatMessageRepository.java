package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomChatMessageRepository {
    List<ChatMessage> findChatMessagesByChatRoom(Long chatRoomId);

    Page<ChatMessage> getChats(Pageable pageable, Long chatRoomId);

    List<ChatMessage> getChatsByRoomIdList(List<Long> chatRoomIdList);
}
