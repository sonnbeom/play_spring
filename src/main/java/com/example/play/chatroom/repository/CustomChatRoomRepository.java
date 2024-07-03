package com.example.play.chatroom.repository;

import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatRoomRepository{
    Page<ChatRoom> findRooms(Member member, Pageable pageable);

}
