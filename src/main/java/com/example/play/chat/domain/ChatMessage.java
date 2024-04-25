package com.example.play.chat.domain;

import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String msg;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(ChatMessageDto chatMessageDto, ChatRoom chatRoom){
        this.nickname = chatMessageDto.getNickname();
        this.msg = chatMessageDto.getMessage();
        this.chatRoom = chatRoom;
    }
    public ChatMessageResponseDto entityToDto(){
        return ChatMessageResponseDto.builder()
                .id(id)
                .msg(msg)
                .chatRoomId(chatRoom.getId())
                .dateTime(getCreatedAt())
                .build();
    }

}
