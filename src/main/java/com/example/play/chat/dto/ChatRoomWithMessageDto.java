package com.example.play.chat.dto;

import com.example.play.chat.domain.ChatRoom;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


// 채팅방 하나에 채팅 여러개
@Getter
public class ChatRoomWithMessageDto {

    private ChatRoomDto chatRoomDto;
    private List<ChatMessageResponseDto> chatMessage;
    @Builder
    public ChatRoomWithMessageDto(ChatRoomDto chatRoomDto, List<ChatMessageResponseDto> chatMessage) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessage = chatMessage;
    }

    public ChatRoomWithMessageDto(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessage = new ArrayList<>();
    }
}
