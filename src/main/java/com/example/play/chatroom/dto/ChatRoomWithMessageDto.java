package com.example.play.chatroom.dto;

import com.example.play.chat.dto.ChatMessageResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


// 채팅방 하나에 채팅 여러개
@Getter
@NoArgsConstructor
public class ChatRoomWithMessageDto {

    private ChatRoomDto chatRoomDto;
    private List<ChatMessageResponseDto> chatMessage;
    @Builder
    public ChatRoomWithMessageDto(ChatRoomDto chatRoomDto, List<ChatMessageResponseDto> chatMessage) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessage = chatMessage;
    }
}
