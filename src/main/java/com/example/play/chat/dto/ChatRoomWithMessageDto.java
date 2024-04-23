package com.example.play.chat.dto;

import com.example.play.chat.domain.ChatRoom;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatRoomWithMessageDto {

    private Long chatRoomId;

    private String fromEmail;
    private String toEmail;
    private List<ChatMessageResponseDto> chatMessage;
    public ChatRoomWithMessageDto(ChatRoom chatRoom, List<ChatMessageResponseDto> chatMessage) {
        this.chatRoomId = chatRoom.getId();
        this.fromEmail = chatRoom.getMember().getEmail();
        this.toEmail = chatRoom.getOther().getEmail();
    }

    public ChatRoomWithMessageDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.fromEmail = chatRoom.getMember().getEmail();
        this.toEmail = chatRoom.getOther().getEmail();
        this.chatMessage = new ArrayList<>();
    }
}
