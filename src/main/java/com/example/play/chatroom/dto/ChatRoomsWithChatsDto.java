package com.example.play.chatroom.dto;


import com.example.play.chat.dto.ChatMessageResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatRoomsWithChatsDto {
    private ChatRoomDto chatRoomDto;
    private ChatMessageResponseDto chatMessage;
    @Builder
    public ChatRoomsWithChatsDto(ChatRoomDto chatRoomDto, ChatMessageResponseDto chatMessage) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessage = chatMessage;
    }
    public void insertChatMessage(ChatMessageResponseDto chatMessage){
        this.chatMessage = chatMessage;
    }
}
