package com.example.play.chat.domain;

import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long roomNumber;

    @Builder
    public ChatMessage(ChatMessageDto chatMessageDto){
        this.nickname = chatMessageDto.getNickname();
        this.msg = chatMessageDto.getMessage();
        this.roomNumber = chatMessageDto.getChatRoomId();
    }

}
