package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.ChatRoom;
import com.example.play.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomChatRoomRepository{
    Page<ChatRoom> findRooms(Member member, Pageable pageable);

}
