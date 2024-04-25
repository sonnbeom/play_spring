package com.example.play.chat.dto;


import com.example.play.chat.domain.ChatMessage;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    public enum MessageType{
        ENTER, TALK
    }
    private MessageType messageType;
    private Long chatRoomId;
    private String message;
    private String nickname;
}

